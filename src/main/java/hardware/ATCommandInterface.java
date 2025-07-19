package hardware;

import com.fazecast.jSerialComm.SerialPort;

public class ATCommandInterface {
    private String portName;
    private SerialPort serialPort;
    private boolean isConnected = false;
    
    public ATCommandInterface(String portName) {
        this.portName = portName;
    }
      public boolean connect() {
        try {
            // Get the serial port
            serialPort = SerialPort.getCommPort(portName);
            
            // Configure the port
            serialPort.setBaudRate(115200);
            serialPort.setNumDataBits(8);
            serialPort.setNumStopBits(1);
            serialPort.setParity(SerialPort.NO_PARITY);
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 
                                        2000, 2000); // Reduced timeout for faster detection
            
            // Open the port
            if (serialPort.openPort()) {
                // Wait for port to stabilize
                Thread.sleep(500); // Reduced stabilization time
                
                // Clear any pending data
                if (serialPort.bytesAvailable() > 0) {
                    byte[] buffer = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(buffer, buffer.length);
                }
                
                // Test connection with basic AT command - try multiple times
                boolean testPassed = false;
                for (int i = 0; i < 3 && !testPassed; i++) {
                    String response = sendCommand("AT", 2000);
                    if (response != null && (response.contains("OK") || response.contains("AT"))) {
                        testPassed = true;
                    } else {
                        Thread.sleep(200); // Wait between attempts
                    }
                }
                
                isConnected = testPassed;
                
                if (!isConnected) {
                    serialPort.closePort();
                }
                
                return isConnected;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("Error connecting to modem on " + portName + ": " + e.getMessage());
            if (serialPort != null && serialPort.isOpen()) {
                serialPort.closePort();
            }
            return false;
        }
    }
    public String sendCommand(String command) {
        return sendCommand(command, 5000); // 5 second timeout
    }
    
    public String sendCommand(String command, int timeoutMs) {
        if (!isConnected || serialPort == null || !serialPort.isOpen()) {
            return null;
        }
        
        try {
            // Clear any pending input
            while (serialPort.bytesAvailable() > 0) {
                serialPort.readBytes(new byte[serialPort.bytesAvailable()], serialPort.bytesAvailable());
            }
            
            // Send AT command
            String fullCommand = command + "\r\n";
            byte[] commandBytes = fullCommand.getBytes();
            int bytesWritten = serialPort.writeBytes(commandBytes, commandBytes.length);
            
            if (bytesWritten != commandBytes.length) {
                return null;
            }
            
            // Read response with timeout
            StringBuilder response = new StringBuilder();
            long startTime = System.currentTimeMillis();
            byte[] buffer = new byte[1024];
            
            while (System.currentTimeMillis() - startTime < timeoutMs) {
                int bytesRead = serialPort.readBytes(buffer, buffer.length);
                if (bytesRead > 0) {
                    String chunk = new String(buffer, 0, bytesRead);
                    response.append(chunk);
                    
                    // Check if we have a complete response (ends with OK, ERROR, or similar)
                    String responseStr = response.toString();
                    if (responseStr.contains("OK\r") || responseStr.contains("ERROR\r") || 
                        responseStr.contains(">\r") || responseStr.contains("+CME ERROR") ||
                        responseStr.contains("+CMS ERROR")) {
                        break;
                    }
                } else {
                    // No data available, wait a bit
                    Thread.sleep(50);
                }
            }
            
            return response.toString().trim();
            
        } catch (Exception e) {
            System.err.println("Error sending command '" + command + "': " + e.getMessage());
            return null;
        }
    }    
    public String sendUSSD(String ussdCode) {
        return sendUSSD(ussdCode, 30000); // 30 second timeout for USSD
    }
    
    public String sendUSSD(String ussdCode, int timeoutMs) {
        if (!isConnected) {
            return null;
        }
        
        try {
            // Enable USSD result notifications
            sendCommand("AT+CUSD=1");
            Thread.sleep(500);
            
            // Send USSD command
            String command = String.format("AT+CUSD=1,\"%s\",15", ussdCode);
            String response = sendCommand(command, timeoutMs);
            
            // Parse USSD response
            if (response != null && response.contains("+CUSD:")) {
                return parseUSSDResponse(response);
            }
            
            return response;
            
        } catch (Exception e) {
            System.err.println("Error sending USSD code '" + ussdCode + "': " + e.getMessage());
            return null;
        }
    }
    
    private String parseUSSDResponse(String response) {
        try {
            // Parse response like: +CUSD: 0,"Your balance is UGX 1000",15
            String[] lines = response.split("\n");
            for (String line : lines) {
                if (line.contains("+CUSD:")) {
                    int firstQuote = line.indexOf('"');
                    int lastQuote = line.lastIndexOf('"');
                    if (firstQuote != -1 && lastQuote != -1 && firstQuote != lastQuote) {
                        return line.substring(firstQuote + 1, lastQuote);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing USSD response: " + e.getMessage());
        }
        return response;
    }
    
    public boolean sendSMS(String phoneNumber, String message) {
        if (!isConnected || serialPort == null || !serialPort.isOpen()) {
            return false;
        }
        
        try {
            // Set SMS text mode
            String response1 = sendCommand("AT+CMGF=1");
            if (response1 == null || !response1.contains("OK")) {
                return false;
            }
            
            // Set SMS destination
            String response2 = sendCommand("AT+CMGS=\"" + phoneNumber + "\"");
            if (response2 == null || !response2.contains(">")) {
                return false;
            }
            
            // Send message content and Ctrl+Z
            String messageWithCtrlZ = message + "\u001A"; // \u001A is Ctrl+Z
            byte[] messageBytes = messageWithCtrlZ.getBytes();
            int bytesWritten = serialPort.writeBytes(messageBytes, messageBytes.length);
            
            if (bytesWritten != messageBytes.length) {
                return false;
            }
            
            // Wait for confirmation
            StringBuilder response = new StringBuilder();
            long startTime = System.currentTimeMillis();
            byte[] buffer = new byte[1024];
            
            while (System.currentTimeMillis() - startTime < 30000) { // 30 second timeout
                int bytesRead = serialPort.readBytes(buffer, buffer.length);
                if (bytesRead > 0) {
                    String chunk = new String(buffer, 0, bytesRead);
                    response.append(chunk);
                    
                    if (response.toString().contains("OK") || response.toString().contains("ERROR")) {
                        break;
                    }
                } else {
                    Thread.sleep(100);
                }
            }
            
            return response.toString().contains("OK");
            
        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
            return false;
        }
    }
    
    public void disconnect() {
        try {
            isConnected = false;
            
            if (serialPort != null && serialPort.isOpen()) {
                serialPort.closePort();
            }
            
        } catch (Exception e) {
            System.err.println("Error disconnecting from modem: " + e.getMessage());
        }
    }
      public boolean isConnected() {
        return isConnected && serialPort != null && serialPort.isOpen();
    }
    
    public String getPortName() {
        return portName;
    }
}
