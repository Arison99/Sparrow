package gateway;

import hardware.ModemDetector;
import hardware.ATCommandInterface;
import hardware.TelcoProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class USSDGatewayService {
    
    @Autowired
    private ModemDetector modemDetector;
    
    @Autowired
    private TelcoProviderService telcoProviderService;
    
    private Map<String, ATCommandInterface> activeConnections = new ConcurrentHashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    public static class USSDResponse {
        private String sessionId;
        private String response;
        private boolean success;
        private String error;
        private String modemPort;
        private String provider;
        private long timestamp;
        
        public USSDResponse(String sessionId, String modemPort) {
            this.sessionId = sessionId;
            this.modemPort = modemPort;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters and setters
        public String getSessionId() { return sessionId; }
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getModemPort() { return modemPort; }
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class USSDRequest {
        private String ussdCode;
        private String preferredProvider;
        private String phoneNumber;
        private int timeoutSeconds = 30;
        
        public USSDRequest(String ussdCode) {
            this.ussdCode = ussdCode;
        }
        
        // Getters and setters
        public String getUssdCode() { return ussdCode; }
        public void setUssdCode(String ussdCode) { this.ussdCode = ussdCode; }
        public String getPreferredProvider() { return preferredProvider; }
        public void setPreferredProvider(String preferredProvider) { this.preferredProvider = preferredProvider; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    }
    
    public CompletableFuture<USSDResponse> sendUSSD(USSDRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            String sessionId = generateSessionId();
            
            // Find available modem
            ModemDetector.ModemInfo selectedModem = selectBestModem(request);
            if (selectedModem == null) {
                USSDResponse response = new USSDResponse(sessionId, "none");
                response.setSuccess(false);
                response.setError("No suitable modem available");
                return response;
            }
            
            USSDResponse response = new USSDResponse(sessionId, selectedModem.getPort());
            
            try {
                // Get or create AT command interface
                ATCommandInterface atInterface = getOrCreateConnection(selectedModem.getPort());
                
                if (!atInterface.isConnected()) {
                    response.setSuccess(false);
                    response.setError("Failed to connect to modem");
                    return response;
                }
                
                // Set provider info
                TelcoProviderService.ProviderInfo provider = telcoProviderService.getProviderByMccMnc(
                    selectedModem.getMcc(), selectedModem.getMnc());
                if (provider != null) {
                    response.setProvider(provider.getName());
                }
                
                // Send USSD command
                String ussdResponse = atInterface.sendUSSD(request.getUssdCode(), 
                    request.getTimeoutSeconds() * 1000);
                
                if (ussdResponse != null) {
                    response.setResponse(ussdResponse);
                    response.setSuccess(true);
                } else {
                    response.setSuccess(false);
                    response.setError("USSD request timed out or failed");
                }
                
            } catch (Exception e) {
                response.setSuccess(false);
                response.setError("Error executing USSD: " + e.getMessage());
            }
            
            return response;
        }, executorService);
    }
    
    private ModemDetector.ModemInfo selectBestModem(USSDRequest request) {
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
        
        // Select modem with best signal strength
        return availableModems.stream()
            .filter(ModemDetector.ModemInfo::isConnected)
            .filter(modem -> modem.getSignalStrength() != null && 
                    !modem.getSignalStrength().contains("No Signal"))
            .max(Comparator.comparing(this::getSignalStrengthValue))
            .orElse(availableModems.stream()
                .filter(ModemDetector.ModemInfo::isConnected)
                .findFirst()
                .orElse(null));
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
    
    private ATCommandInterface getOrCreateConnection(String port) {
        return activeConnections.computeIfAbsent(port, p -> {
            ATCommandInterface atInterface = new ATCommandInterface(p);
            atInterface.connect();
            return atInterface;
        });
    }
    
    private String generateSessionId() {
        return "USSD-" + System.currentTimeMillis() + "-" + 
               UUID.randomUUID().toString().substring(0, 8);
    }
    
    public CompletableFuture<List<USSDResponse>> sendBulkUSSD(List<USSDRequest> requests) {
        List<CompletableFuture<USSDResponse>> futures = requests.stream()
            .map(this::sendUSSD)
            .toList();
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .toList());
    }
    
    // Mobile Money specific USSD operations
    public CompletableFuture<USSDResponse> checkBalance(String provider) {
        TelcoProviderService.ProviderInfo providerInfo = 
            telcoProviderService.getProviderByName(provider);
        
        if (providerInfo == null || providerInfo.getBalanceUSSD() == null) {
            USSDResponse response = new USSDResponse("BAL-" + System.currentTimeMillis(), "none");
            response.setSuccess(false);
            response.setError("Balance USSD not available for provider: " + provider);
            return CompletableFuture.completedFuture(response);
        }
        
        USSDRequest request = new USSDRequest(providerInfo.getBalanceUSSD());
        request.setPreferredProvider(provider);
        return sendUSSD(request);
    }
    
    public CompletableFuture<USSDResponse> accessMobileMoneyMenu(String provider) {
        TelcoProviderService.ProviderInfo providerInfo = 
            telcoProviderService.getProviderByName(provider);
        
        if (providerInfo == null || !providerInfo.supportsMobileMoney() || 
            providerInfo.getMobileMoneyUSSD() == null) {
            USSDResponse response = new USSDResponse("MM-" + System.currentTimeMillis(), "none");
            response.setSuccess(false);
            response.setError("Mobile money not available for provider: " + provider);
            return CompletableFuture.completedFuture(response);
        }
        
        USSDRequest request = new USSDRequest(providerInfo.getMobileMoneyUSSD());
        request.setPreferredProvider(provider);
        return sendUSSD(request);
    }
    
    public List<ModemDetector.ModemInfo> getAvailableModems() {
        return modemDetector.getDetectedModems();
    }
    
    public List<TelcoProviderService.ProviderInfo> getSupportedProviders() {
        return telcoProviderService.getAllProviders();
    }
    
    public void refreshModems() {
        modemDetector.detectModems();
    }
    
    public void closeAllConnections() {
        activeConnections.values().forEach(ATCommandInterface::disconnect);
        activeConnections.clear();
        executorService.shutdown();
    }
}
