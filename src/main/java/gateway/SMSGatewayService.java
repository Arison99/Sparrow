package gateway;

import hardware.ModemDetector;
import hardware.ATCommandInterface;
import hardware.TelcoProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;

@Service
public class SMSGatewayService {
    
    @Autowired
    private ModemDetector modemDetector;
    
    @Autowired
    private TelcoProviderService telcoProviderService;
    
    private Map<String, ATCommandInterface> smsConnections = new ConcurrentHashMap<>();
    private ExecutorService smsExecutor = Executors.newFixedThreadPool(20);
    private Queue<SMSRequest> smsQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean isProcessingQueue = false;
    
    public static class SMSRequest {
        private String messageId;
        private String phoneNumber;
        private String message;
        private String preferredProvider;
        private int priority = 1; // 1=low, 2=medium, 3=high
        private long scheduledTime = 0; // 0 = send immediately
        private int maxRetries = 3;
        private String callbackUrl;
        
        public SMSRequest(String phoneNumber, String message) {
            this.messageId = "SMS-" + System.currentTimeMillis() + "-" + 
                            UUID.randomUUID().toString().substring(0, 8);
            this.phoneNumber = phoneNumber;
            this.message = message;
        }
        
        // Getters and setters
        public String getMessageId() { return messageId; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getMessage() { return message; }
        public String getPreferredProvider() { return preferredProvider; }
        public void setPreferredProvider(String preferredProvider) { this.preferredProvider = preferredProvider; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public long getScheduledTime() { return scheduledTime; }
        public void setScheduledTime(long scheduledTime) { this.scheduledTime = scheduledTime; }
        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        public String getCallbackUrl() { return callbackUrl; }
        public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
    }
    
    public static class SMSResponse {
        private String messageId;
        private String phoneNumber;
        private boolean success;
        private String error;
        private String modemPort;
        private String provider;
        private long timestamp;
        private int attempts;
        private String status; // QUEUED, SENDING, SENT, FAILED, DELIVERED
        
        public SMSResponse(String messageId, String phoneNumber) {
            this.messageId = messageId;
            this.phoneNumber = phoneNumber;
            this.timestamp = System.currentTimeMillis();
            this.status = "QUEUED";
            this.attempts = 0;
        }
        
        // Getters and setters
        public String getMessageId() { return messageId; }
        public String getPhoneNumber() { return phoneNumber; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getModemPort() { return modemPort; }
        public void setModemPort(String modemPort) { this.modemPort = modemPort; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public long getTimestamp() { return timestamp; }
        public int getAttempts() { return attempts; }
        public void setAttempts(int attempts) { this.attempts = attempts; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    public static class BulkSMSRequest {
        private String batchId;
        private List<String> phoneNumbers;
        private String message;
        private String preferredProvider;
        private int priority = 1;
        private long scheduledTime = 0;
        private String callbackUrl;
        
        public BulkSMSRequest(List<String> phoneNumbers, String message) {
            this.batchId = "BULK-" + System.currentTimeMillis() + "-" + 
                          UUID.randomUUID().toString().substring(0, 8);
            this.phoneNumbers = phoneNumbers;
            this.message = message;
        }
        
        // Getters and setters
        public String getBatchId() { return batchId; }
        public List<String> getPhoneNumbers() { return phoneNumbers; }
        public String getMessage() { return message; }
        public String getPreferredProvider() { return preferredProvider; }
        public void setPreferredProvider(String preferredProvider) { this.preferredProvider = preferredProvider; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
        public long getScheduledTime() { return scheduledTime; }
        public void setScheduledTime(long scheduledTime) { this.scheduledTime = scheduledTime; }
        public String getCallbackUrl() { return callbackUrl; }
        public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
    }
    
    public static class BulkSMSResponse {
        private String batchId;
        private List<SMSResponse> responses;
        private int totalCount;
        private int successCount;
        private int failedCount;
        private long timestamp;
        private String status; // QUEUED, PROCESSING, COMPLETED, FAILED
        
        public BulkSMSResponse(String batchId, int totalCount) {
            this.batchId = batchId;
            this.totalCount = totalCount;
            this.responses = new ArrayList<>();
            this.timestamp = System.currentTimeMillis();
            this.status = "QUEUED";
        }
        
        // Getters and setters
        public String getBatchId() { return batchId; }
        public List<SMSResponse> getResponses() { return responses; }
        public void setResponses(List<SMSResponse> responses) { this.responses = responses; }
        public int getTotalCount() { return totalCount; }
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        public int getFailedCount() { return failedCount; }
        public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
        public long getTimestamp() { return timestamp; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    private Map<String, SMSResponse> smsStatus = new ConcurrentHashMap<>();
    private Map<String, BulkSMSResponse> bulkSmsStatus = new ConcurrentHashMap<>();
    
    public CompletableFuture<SMSResponse> sendSMS(SMSRequest request) {
        SMSResponse response = new SMSResponse(request.getMessageId(), request.getPhoneNumber());
        smsStatus.put(request.getMessageId(), response);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                response.setStatus("SENDING");
                
                // Find available modem
                ModemDetector.ModemInfo selectedModem = selectBestModemForSMS(request);
                if (selectedModem == null) {
                    response.setSuccess(false);
                    response.setError("No suitable modem available");
                    response.setStatus("FAILED");
                    return response;
                }
                
                response.setModemPort(selectedModem.getPort());
                
                // Set provider info
                TelcoProviderService.ProviderInfo provider = telcoProviderService.getProviderByMccMnc(
                    selectedModem.getMcc(), selectedModem.getMnc());
                if (provider != null) {
                    response.setProvider(provider.getName());
                }
                
                // Get or create AT command interface
                ATCommandInterface atInterface = getOrCreateSMSConnection(selectedModem.getPort());
                
                if (!atInterface.isConnected()) {
                    response.setSuccess(false);
                    response.setError("Failed to connect to modem");
                    response.setStatus("FAILED");
                    return response;
                }
                
                // Send SMS
                response.setAttempts(response.getAttempts() + 1);
                boolean sent = atInterface.sendSMS(request.getPhoneNumber(), request.getMessage());
                
                if (sent) {
                    response.setSuccess(true);
                    response.setStatus("SENT");
                } else {
                    response.setSuccess(false);
                    response.setError("SMS sending failed");
                    response.setStatus("FAILED");
                }
                
            } catch (Exception e) {
                response.setSuccess(false);
                response.setError("Error sending SMS: " + e.getMessage());
                response.setStatus("FAILED");
            }
            
            return response;
        }, smsExecutor);
    }
    
    public CompletableFuture<BulkSMSResponse> sendBulkSMS(BulkSMSRequest request) {
        BulkSMSResponse bulkResponse = new BulkSMSResponse(request.getBatchId(), 
            request.getPhoneNumbers().size());
        bulkSmsStatus.put(request.getBatchId(), bulkResponse);
        
        return CompletableFuture.supplyAsync(() -> {
            bulkResponse.setStatus("PROCESSING");
            
            List<CompletableFuture<SMSResponse>> futures = new ArrayList<>();
            
            for (String phoneNumber : request.getPhoneNumbers()) {
                SMSRequest smsRequest = new SMSRequest(phoneNumber, request.getMessage());
                smsRequest.setPreferredProvider(request.getPreferredProvider());
                smsRequest.setPriority(request.getPriority());
                smsRequest.setCallbackUrl(request.getCallbackUrl());
                
                futures.add(sendSMS(smsRequest));
            }
            
            // Wait for all SMS to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
            // Collect results
            List<SMSResponse> responses = futures.stream()
                .map(CompletableFuture::join)
                .toList();
            
            bulkResponse.setResponses(responses);
            bulkResponse.setSuccessCount((int) responses.stream().filter(SMSResponse::isSuccess).count());
            bulkResponse.setFailedCount(responses.size() - bulkResponse.getSuccessCount());
            bulkResponse.setStatus("COMPLETED");
            
            return bulkResponse;
        }, smsExecutor);
    }
    
    private ModemDetector.ModemInfo selectBestModemForSMS(SMSRequest request) {
        List<ModemDetector.ModemInfo> availableModems = modemDetector.getDetectedModems();
        
        if (availableModems.isEmpty()) {
            return null;
        }
        
        // If preferred provider is specified, try to find matching modem
        if (request.getPreferredProvider() != null) {
            for (ModemDetector.ModemInfo modem : availableModems) {
                if (modem.isConnected() && modem.getNetworkOperator() != null &&
                    modem.getNetworkOperator().toLowerCase().contains(
                        request.getPreferredProvider().toLowerCase())) {
                    return modem;
                }
            }
        }
        
        // Select modem with best signal strength and lowest usage
        return availableModems.stream()
            .filter(ModemDetector.ModemInfo::isConnected)
            .filter(modem -> modem.getSignalStrength() != null && 
                    !modem.getSignalStrength().contains("No Signal"))
            .max(Comparator.comparing(this::getModemScore))
            .orElse(availableModems.stream()
                .filter(ModemDetector.ModemInfo::isConnected)
                .findFirst()
                .orElse(null));
    }
    
    private int getModemScore(ModemDetector.ModemInfo modem) {
        int signalScore = getSignalStrengthValue(modem);
        int usageScore = getModemUsageScore(modem.getPort());
        return signalScore * 10 - usageScore; // Prefer high signal, low usage
    }
    
    private int getSignalStrengthValue(ModemDetector.ModemInfo modem) {
        String signal = modem.getSignalStrength();
        if (signal == null) return 0;
        
        if (signal.contains("Excellent")) return 5;
        if (signal.contains("Good")) return 4;
        if (signal.contains("Fair")) return 3;
        if (signal.contains("Poor")) return 2;
        if (signal.contains("Very Poor")) return 1;
        return 0;
    }
    
    private int getModemUsageScore(String port) {
        // Count how many active SMS operations are using this modem
        return (int) smsStatus.values().stream()
            .filter(response -> port.equals(response.getModemPort()))
            .filter(response -> "SENDING".equals(response.getStatus()))
            .count();
    }
    
    private ATCommandInterface getOrCreateSMSConnection(String port) {
        return smsConnections.computeIfAbsent(port, p -> {
            ATCommandInterface atInterface = new ATCommandInterface(p);
            atInterface.connect();
            return atInterface;
        });
    }
    
    // Queue management for bulk operations
    public void addToQueue(SMSRequest request) {
        smsQueue.offer(request);
        if (!isProcessingQueue) {
            processQueue();
        }
    }
    
    private void processQueue() {
        if (isProcessingQueue) return;
        
        isProcessingQueue = true;
        CompletableFuture.runAsync(() -> {
            while (!smsQueue.isEmpty()) {
                SMSRequest request = smsQueue.poll();
                if (request != null) {
                    if (request.getScheduledTime() <= System.currentTimeMillis()) {
                        sendSMS(request);
                    } else {
                        smsQueue.offer(request); // Put back if not time yet
                    }
                }
                
                try {
                    Thread.sleep(1000); // Process every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            isProcessingQueue = false;
        }, smsExecutor);
    }
    
    // Status checking methods
    public SMSResponse getSMSStatus(String messageId) {
        return smsStatus.get(messageId);
    }
    
    public BulkSMSResponse getBulkSMSStatus(String batchId) {
        return bulkSmsStatus.get(batchId);
    }
    
    public List<SMSResponse> getAllSMSStatus() {
        return new ArrayList<>(smsStatus.values());
    }
    
    public List<BulkSMSResponse> getAllBulkSMSStatus() {
        return new ArrayList<>(bulkSmsStatus.values());
    }
    
    // Statistics
    public Map<String, Object> getSMSStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalSMS = smsStatus.size();
        long successfulSMS = smsStatus.values().stream()
            .filter(SMSResponse::isSuccess)
            .count();
        long failedSMS = totalSMS - successfulSMS;
        
        stats.put("totalSMS", totalSMS);
        stats.put("successfulSMS", successfulSMS);
        stats.put("failedSMS", failedSMS);
        stats.put("successRate", totalSMS > 0 ? (double) successfulSMS / totalSMS * 100 : 0);
        stats.put("queueSize", smsQueue.size());
        stats.put("activeConnections", smsConnections.size());
        
        return stats;
    }
    
    public void closeAllSMSConnections() {
        smsConnections.values().forEach(ATCommandInterface::disconnect);
        smsConnections.clear();
        smsExecutor.shutdown();
    }
}
