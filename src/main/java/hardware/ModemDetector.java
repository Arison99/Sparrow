package hardware;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.concurrent.CompletableFuture;

@Service
public class ModemDetector {
    
    public static class ModemInfo {
        private String port;
        private String manufacturer;
        private String model;
        private String imei;
        private String simStatus;
        private String networkOperator;
        private String signalStrength;
        private boolean isConnected;
        private String firmwareVersion;
        private String phoneNumber;
        private String mcc; // Mobile Country Code
        private String mnc; // Mobile Network Code
        
        public ModemInfo(String port) {
            this.port = port;
            this.isConnected = false;
        }
        
        // Getters and setters
        public String getPort() { return port; }
        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public String getImei() { return imei; }
        public void setImei(String imei) { this.imei = imei; }
        public String getSimStatus() { return simStatus; }
        public void setSimStatus(String simStatus) { this.simStatus = simStatus; }
        public String getNetworkOperator() { return networkOperator; }
        public void setNetworkOperator(String networkOperator) { this.networkOperator = networkOperator; }
        public String getSignalStrength() { return signalStrength; }
        public void setSignalStrength(String signalStrength) { this.signalStrength = signalStrength; }
        public boolean isConnected() { return isConnected; }
        public void setConnected(boolean connected) { isConnected = connected; }
        public String getFirmwareVersion() { return firmwareVersion; }
        public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getMcc() { return mcc; }
        public void setMcc(String mcc) { this.mcc = mcc; }
        public String getMnc() { return mnc; }
        public void setMnc(String mnc) { this.mnc = mnc; }
    }
    
    private List<ModemInfo> detectedModems = new ArrayList<>();
    private Map<String, ModemInfo> activeModems = new HashMap<>();      public List<ModemInfo> detectModems() {
        detectedModems.clear();
        
        // Use jSerialComm to detect all available serial ports
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Found " + ports.length + " serial ports available");
        
        for (SerialPort port : ports) {
            String portName = port.getSystemPortName();
            String description = port.getPortDescription();
            String vendor = port.getPortLocation();
            
            System.out.println("Checking port: " + portName + " - Description: " + description + " - Location: " + vendor);
            
            // Check if this port might be a modem based on description OR test all COM ports
            if (isPotentialModem(description) || isCommonModemPort(portName)) {
                ModemInfo modem = new ModemInfo(portName);
                modem.setModel(description);
                detectedModems.add(modem);
                System.out.println("Added as potential modem: " + portName);
            } else {
                // For unrecognized ports, still test if they respond to AT commands
                System.out.println("Testing unrecognized port " + portName + " for AT command response...");
                if (testATResponse(portName)) {
                    ModemInfo modem = new ModemInfo(portName);
                    modem.setModel(description != null ? description : "Unknown Device");
                    detectedModems.add(modem);
                    System.out.println("Port " + portName + " responds to AT commands - added as modem");
                }
            }
        }
        
        System.out.println("Total potential modems found: " + detectedModems.size());
        
        // Query each detected modem for detailed information
        for (ModemInfo modem : detectedModems) {
            System.out.println("Querying detailed info for modem on port: " + modem.getPort());
            queryModemInfo(modem);
        }
        
        return detectedModems;
    }
      private boolean isPotentialModem(String description) {
        if (description == null) return false;
        String desc = description.toLowerCase();
        
        return desc.contains("modem") || 
               desc.contains("mobile") || 
               desc.contains("cellular") ||
               desc.contains("gsm") || 
               desc.contains("3g") || 
               desc.contains("4g") || 
               desc.contains("lte") || 
               desc.contains("5g") ||
               desc.contains("wwan") ||
               desc.contains("usb serial") ||
               desc.contains("serial port") ||
               desc.contains("communication device") ||
               desc.contains("huawei") || 
               desc.contains("zte") || 
               desc.contains("sierra") || 
               desc.contains("telit") || 
               desc.contains("quectel") ||
               desc.contains("simcom") ||
               desc.contains("u-blox") ||
               desc.contains("fibocom") ||
               desc.contains("gemalto") ||
               desc.contains("novatel") ||
               desc.contains("option") ||
               desc.contains("anydata") ||
               desc.contains("cinterion") ||
               desc.contains("ftdi") ||  // FTDI USB-Serial chips commonly used in modems
               desc.contains("prolific") || // Prolific USB-Serial chips
               desc.contains("cp210x") || // Silicon Labs USB-Serial chips
               desc.contains("ch340") || // WCH USB-Serial chips
               desc.contains("pl2303"); // Prolific PL2303 USB-Serial chips
    }
      private boolean isCommonModemPort(String portName) {
        if (portName == null) return false;
        
        // On Windows, check common COM ports (most USB modems use COM ports)
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            // Accept all COM ports as potential modems since many USB modems appear as generic COM ports
            return portName.matches("COM\\d+");
        } else {
            // On Linux, check common device paths
            return portName.startsWith("ttyUSB") || 
                   portName.startsWith("ttyACM") || 
                   portName.startsWith("ttyS") ||
                   portName.startsWith("ttyAMA") ||
                   portName.startsWith("ttyHS") ||
                   portName.startsWith("ttyXRUSB");
        }
    }
    private void queryModemInfo(ModemInfo modem) {
        try {
            ATCommandInterface atInterface = new ATCommandInterface(modem.getPort());
            if (atInterface.connect()) {
                modem.setConnected(true);
                
                // Query basic modem information
                String manufacturer = atInterface.sendCommand("AT+CGMI");
                if (manufacturer != null && !manufacturer.contains("ERROR")) {
                    modem.setManufacturer(manufacturer.trim());
                }
                
                String model = atInterface.sendCommand("AT+CGMM");
                if (model != null && !model.contains("ERROR")) {
                    modem.setModel(model.trim());
                }
                
                String imei = atInterface.sendCommand("AT+CGSN");
                if (imei != null && !imei.contains("ERROR")) {
                    modem.setImei(imei.trim());
                }
                
                String firmware = atInterface.sendCommand("AT+CGMR");
                if (firmware != null && !firmware.contains("ERROR")) {
                    modem.setFirmwareVersion(firmware.trim());
                }
                
                // Query SIM information
                String simStatus = atInterface.sendCommand("AT+CPIN?");
                if (simStatus != null) {
                    modem.setSimStatus(simStatus.trim());
                }
                
                // Query network operator
                String operator = atInterface.sendCommand("AT+COPS?");
                if (operator != null && !operator.contains("ERROR")) {
                    modem.setNetworkOperator(parseOperatorName(operator));
                    parseOperatorCodes(operator, modem);
                }
                
                // Query signal strength
                String signal = atInterface.sendCommand("AT+CSQ");
                if (signal != null && !signal.contains("ERROR")) {
                    modem.setSignalStrength(parseSignalStrength(signal));
                }
                
                // Query phone number
                String phoneNumber = atInterface.sendCommand("AT+CNUM");
                if (phoneNumber != null && !phoneNumber.contains("ERROR")) {
                    modem.setPhoneNumber(parsePhoneNumber(phoneNumber));
                }
                
                atInterface.disconnect();
                activeModems.put(modem.getPort(), modem);
            }
        } catch (Exception e) {
            System.err.println("Error querying modem info for " + modem.getPort() + ": " + e.getMessage());
            modem.setConnected(false);
        }
    }
    
    private String parseOperatorName(String response) {
        // Parse response like: +COPS: 0,0,"MTN NG",7
        Pattern pattern = Pattern.compile("\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }
    
    private void parseOperatorCodes(String response, ModemInfo modem) {
        // Parse MCC and MNC from operator response
        try {
            // First try to get from network registration
            ATCommandInterface atInterface = new ATCommandInterface(modem.getPort());
            if (atInterface.connect()) {
                String regInfo = atInterface.sendCommand("AT+CREG?");
                if (regInfo != null && regInfo.contains(",")) {
                    // Parse registration info for MCC/MNC
                    Pattern pattern = Pattern.compile("\"([0-9A-F]+)\"");
                    Matcher matcher = pattern.matcher(regInfo);
                    if (matcher.find()) {
                        String cellInfo = matcher.group(1);
                        if (cellInfo.length() >= 4) {
                            // Extract MCC/MNC from cell info
                            modem.setMcc(cellInfo.substring(0, 3));
                            modem.setMnc(cellInfo.substring(3));
                        }
                    }
                }
                atInterface.disconnect();
            }
        } catch (Exception e) {
            System.err.println("Error parsing operator codes: " + e.getMessage());
        }
    }
    
    private String parseSignalStrength(String response) {
        // Parse response like: +CSQ: 15,99
        Pattern pattern = Pattern.compile("\\+CSQ: (\\d+),");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            int rssi = Integer.parseInt(matcher.group(1));
            if (rssi == 99) {
                return "No Signal";
            } else {
                int dbm = -113 + (rssi * 2);
                return dbm + " dBm (" + getSignalQuality(rssi) + ")";
            }
        }
        return "Unknown";
    }
    
    private String getSignalQuality(int rssi) {
        if (rssi >= 20) return "Excellent";
        else if (rssi >= 15) return "Good";
        else if (rssi >= 10) return "Fair";
        else if (rssi >= 5) return "Poor";
        else return "Very Poor";
    }
    
    private String parsePhoneNumber(String response) {
        // Parse response like: +CNUM: ,"2348012345678",145
        Pattern pattern = Pattern.compile("\"([+0-9]+)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }
    
    public List<ModemInfo> getDetectedModems() {
        return detectedModems;
    }
    
    public Map<String, ModemInfo> getActiveModems() {
        return activeModems;
    }
    
    public ModemInfo getModemByPort(String port) {
        return activeModems.get(port);
    }
    
    public CompletableFuture<List<ModemInfo>> detectModemsAsync() {
        return CompletableFuture.supplyAsync(this::detectModems);
    }
    
    private boolean testATResponse(String portName) {
        try {
            ATCommandInterface atInterface = new ATCommandInterface(portName);
            if (atInterface.connect()) {
                String response = atInterface.sendCommand("AT");
                atInterface.disconnect();
                return response != null && (response.contains("OK") || response.contains("AT"));
            }
        } catch (Exception e) {
            System.out.println("Port " + portName + " failed AT test: " + e.getMessage());
        }
        return false;
    }
}
