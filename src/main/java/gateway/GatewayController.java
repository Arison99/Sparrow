package gateway;

import hardware.ModemDetector;
import hardware.TelcoProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/gateway")
@CrossOrigin(origins = "*")
public class GatewayController {
    
    @Autowired
    private ModemDetector modemDetector;
    
    @Autowired
    private TelcoProviderService telcoProviderService;
    
    @Autowired
    private USSDGatewayService ussdGatewayService;
    
    @Autowired
    private SMSGatewayService smsGatewayService;
    
    // Modem Management APIs
    @GetMapping("/modems/detect")
    public ResponseEntity<Map<String, Object>> detectModems() {
        try {
            List<ModemDetector.ModemInfo> modems = modemDetector.detectModems();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("modems", modems);
            response.put("count", modems.size());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    @GetMapping("/modems")
    public ResponseEntity<List<ModemDetector.ModemInfo>> getModems() {
        return ResponseEntity.ok(modemDetector.getDetectedModems());
    }
    
    @GetMapping("/modems/{port}")
    public ResponseEntity<ModemDetector.ModemInfo> getModem(@PathVariable String port) {
        ModemDetector.ModemInfo modem = modemDetector.getModemByPort(port);
        if (modem != null) {
            return ResponseEntity.ok(modem);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Provider Management APIs
    @GetMapping("/providers")
    public ResponseEntity<List<TelcoProviderService.ProviderInfo>> getAllProviders() {
        return ResponseEntity.ok(telcoProviderService.getAllProviders());
    }
    
    @GetMapping("/providers/country/{country}")
    public ResponseEntity<List<TelcoProviderService.ProviderInfo>> getProvidersByCountry(
            @PathVariable String country) {
        return ResponseEntity.ok(telcoProviderService.getProvidersByCountry(country));
    }
    
    @GetMapping("/providers/mobile-money")
    public ResponseEntity<List<TelcoProviderService.ProviderInfo>> getMobileMoneyProviders() {
        return ResponseEntity.ok(telcoProviderService.getMobileMoneyProviders());
    }
    
    @GetMapping("/providers/countries")
    public ResponseEntity<Set<String>> getSupportedCountries() {
        return ResponseEntity.ok(telcoProviderService.getSupportedCountries());
    }
    
    // USSD Gateway APIs
    @PostMapping("/ussd/send")
    public CompletableFuture<ResponseEntity<USSDGatewayService.USSDResponse>> sendUSSD(
            @RequestBody Map<String, String> request) {
        
        USSDGatewayService.USSDRequest ussdRequest = new USSDGatewayService.USSDRequest(
            request.get("ussdCode"));
        
        if (request.containsKey("provider")) {
            ussdRequest.setPreferredProvider(request.get("provider"));
        }
        if (request.containsKey("phoneNumber")) {
            ussdRequest.setPhoneNumber(request.get("phoneNumber"));
        }
        if (request.containsKey("timeoutSeconds")) {
            ussdRequest.setTimeoutSeconds(Integer.parseInt(request.get("timeoutSeconds")));
        }
        
        return ussdGatewayService.sendUSSD(ussdRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    @PostMapping("/ussd/balance/{provider}")
    public CompletableFuture<ResponseEntity<USSDGatewayService.USSDResponse>> checkBalance(
            @PathVariable String provider) {
        return ussdGatewayService.checkBalance(provider)
            .thenApply(ResponseEntity::ok);
    }
    
    @PostMapping("/ussd/mobile-money/{provider}")
    public CompletableFuture<ResponseEntity<USSDGatewayService.USSDResponse>> accessMobileMoneyMenu(
            @PathVariable String provider) {
        return ussdGatewayService.accessMobileMoneyMenu(provider)
            .thenApply(ResponseEntity::ok);
    }
    
    // SMS Gateway APIs
    @PostMapping("/sms/send")
    public CompletableFuture<ResponseEntity<SMSGatewayService.SMSResponse>> sendSMS(
            @RequestBody Map<String, Object> request) {
        
        SMSGatewayService.SMSRequest smsRequest = new SMSGatewayService.SMSRequest(
            (String) request.get("phoneNumber"),
            (String) request.get("message"));
        
        if (request.containsKey("provider")) {
            smsRequest.setPreferredProvider((String) request.get("provider"));
        }
        if (request.containsKey("priority")) {
            smsRequest.setPriority((Integer) request.get("priority"));
        }
        if (request.containsKey("scheduledTime")) {
            smsRequest.setScheduledTime((Long) request.get("scheduledTime"));
        }
        if (request.containsKey("callbackUrl")) {
            smsRequest.setCallbackUrl((String) request.get("callbackUrl"));
        }
        
        return smsGatewayService.sendSMS(smsRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    @PostMapping("/sms/bulk")
    public CompletableFuture<ResponseEntity<SMSGatewayService.BulkSMSResponse>> sendBulkSMS(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        List<String> phoneNumbers = (List<String>) request.get("phoneNumbers");
        String message = (String) request.get("message");
        
        SMSGatewayService.BulkSMSRequest bulkRequest = new SMSGatewayService.BulkSMSRequest(
            phoneNumbers, message);
        
        if (request.containsKey("provider")) {
            bulkRequest.setPreferredProvider((String) request.get("provider"));
        }
        if (request.containsKey("priority")) {
            bulkRequest.setPriority((Integer) request.get("priority"));
        }
        if (request.containsKey("scheduledTime")) {
            bulkRequest.setScheduledTime((Long) request.get("scheduledTime"));
        }
        if (request.containsKey("callbackUrl")) {
            bulkRequest.setCallbackUrl((String) request.get("callbackUrl"));
        }
        
        return smsGatewayService.sendBulkSMS(bulkRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    @GetMapping("/sms/status/{messageId}")
    public ResponseEntity<SMSGatewayService.SMSResponse> getSMSStatus(@PathVariable String messageId) {
        SMSGatewayService.SMSResponse response = smsGatewayService.getSMSStatus(messageId);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/sms/bulk/status/{batchId}")
    public ResponseEntity<SMSGatewayService.BulkSMSResponse> getBulkSMSStatus(@PathVariable String batchId) {
        SMSGatewayService.BulkSMSResponse response = smsGatewayService.getBulkSMSStatus(batchId);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/sms/statistics")
    public ResponseEntity<Map<String, Object>> getSMSStatistics() {
        return ResponseEntity.ok(smsGatewayService.getSMSStatistics());
    }
    
    // Integration with existing Sparrow services
    @PostMapping("/payment/send-money")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> sendMoneyViaMobileMoney(
            @RequestBody Map<String, Object> request) {
        
        String provider = (String) request.get("provider");
        String fromPhone = (String) request.get("fromPhone");
        String toPhone = (String) request.get("toPhone");
        String amount = request.get("amount").toString();
        String pin = (String) request.get("pin");
        
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> response = new HashMap<>();
            
            try {
                // Access mobile money menu
                USSDGatewayService.USSDRequest ussdRequest = new USSDGatewayService.USSDRequest(
                    telcoProviderService.getProviderByName(provider).getMobileMoneyUSSD());
                ussdRequest.setPreferredProvider(provider);
                
                USSDGatewayService.USSDResponse ussdResponse = ussdGatewayService.sendUSSD(ussdRequest).get();
                
                if (ussdResponse.isSuccess()) {
                    response.put("success", true);
                    response.put("sessionId", ussdResponse.getSessionId());
                    response.put("message", "Mobile money transaction initiated");
                    response.put("ussdResponse", ussdResponse.getResponse());
                } else {
                    response.put("success", false);
                    response.put("error", ussdResponse.getError());
                }
                
            } catch (Exception e) {
                response.put("success", false);
                response.put("error", "Failed to initiate mobile money transaction: " + e.getMessage());
            }
            
            return ResponseEntity.ok(response);
        });
    }
    
    @PostMapping("/payment/buy-airtime")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> buyAirtimeViaMobileMoney(
            @RequestBody Map<String, Object> request) {
        
        String provider = (String) request.get("provider");
        String phoneNumber = (String) request.get("phoneNumber");
        String amount = request.get("amount").toString();
        
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> response = new HashMap<>();
            
            try {
                // Get provider info
                TelcoProviderService.ProviderInfo providerInfo = telcoProviderService.getProviderByName(provider);
                
                if (providerInfo != null && providerInfo.supportsMobileMoney()) {
                    // Access mobile money menu for airtime purchase
                    USSDGatewayService.USSDRequest ussdRequest = new USSDGatewayService.USSDRequest(
                        providerInfo.getMobileMoneyUSSD());
                    ussdRequest.setPreferredProvider(provider);
                    
                    USSDGatewayService.USSDResponse ussdResponse = ussdGatewayService.sendUSSD(ussdRequest).get();
                    
                    if (ussdResponse.isSuccess()) {
                        response.put("success", true);
                        response.put("sessionId", ussdResponse.getSessionId());
                        response.put("message", "Airtime purchase initiated");
                        response.put("ussdResponse", ussdResponse.getResponse());
                    } else {
                        response.put("success", false);
                        response.put("error", ussdResponse.getError());
                    }
                } else {
                    response.put("success", false);
                    response.put("error", "Mobile money not supported by provider: " + provider);
                }
                
            } catch (Exception e) {
                response.put("success", false);
                response.put("error", "Failed to buy airtime: " + e.getMessage());
            }
            
            return ResponseEntity.ok(response);
        });
    }
    
    // Health check and system status
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            List<ModemDetector.ModemInfo> modems = modemDetector.getDetectedModems();
            long connectedModems = modems.stream().filter(ModemDetector.ModemInfo::isConnected).count();
            
            health.put("status", "UP");
            health.put("totalModems", modems.size());
            health.put("connectedModems", connectedModems);
            health.put("supportedProviders", telcoProviderService.getAllProviders().size());
            health.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            return ResponseEntity.status(500).body(health);
        }
    }
}
