import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BackendService {
    // Simulated user data
    private static final Map<String, User> users = new HashMap<>();
    private static String currentUserPhone = "0767708272"; // Default user for simulation
    
    // Initialize with sample data
    static {
        User defaultUser = new User(
            "0767708272", 
            "1234", 
            500000.0, 
            new ArrayList<>()
        );
        
        // Add some sample transactions
        defaultUser.addTransaction(new Transaction("T123456", "Sent to 0987654321", -100.0, new Date()));
        defaultUser.addTransaction(new Transaction("T234567", "Received from 0567891234", 250.0, new Date(System.currentTimeMillis() - 86400000)));
        defaultUser.addTransaction(new Transaction("T345678", "Airtime purchase", -50.0, new Date(System.currentTimeMillis() - 172800000)));
        
        users.put(defaultUser.getPhoneNumber(), defaultUser);
    }
    
    public static String checkBalance(String pin) {
        User user = getUser();
        if (user != null && user.validatePin(pin)) {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            return "Your balance is $" + df.format(user.getBalance());
        }
        return "Invalid PIN. Please try again.";
    }

    public static String calculateFees(double amount) {
        if (amount <= 0) {
            return "Please enter a valid amount.";
        }
        
        // Simulate fee calculation (1.5% with min $1, max $15)
        double fee = Math.max(1, Math.min(15, amount * 0.015));
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        return "Amount: $" + df.format(amount) + "\nFee: $" + df.format(fee) + 
               "\nTotal: $" + df.format(amount + fee);
    }

    public static String getApprovals() {
        User user = getUser();
        if (user == null || user.getApprovals().isEmpty()) {
            return "You have no pending approvals.";
        }
        
        StringBuilder result = new StringBuilder("Pending Approvals:\n");
        for (String approval : user.getApprovals()) {
            result.append("- ").append(approval).append("\n");
        }
        return result.toString();
    }

    public static String getTransactions() {
        User user = getUser();
        if (user == null) {
            return "Error retrieving transactions.";
        }
        
        List<Transaction> transactions = user.getTransactions();
        if (transactions.isEmpty()) {
            return "You have no recent transactions.";
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("+#,##0.00;-#,##0.00");
        
        StringBuilder result = new StringBuilder("Recent Transactions:\n");
        int count = 0;
        for (Transaction t : transactions) {
            result.append(sdf.format(t.getDate()))
                  .append(": ")
                  .append(t.getDescription())
                  .append(" ($")
                  .append(df.format(t.getAmount()))
                  .append(")\n");
            count++;
            if (count >= 5) break; // Show only last 5 transactions
        }
        return result.toString();
    }

    public static String changePin(String oldPin, String newPin) {
        User user = getUser();
        if (user == null) {
            return "User not found.";
        }
        
        if (!user.validatePin(oldPin)) {
            return "Current PIN is incorrect.";
        }
        
        if (newPin.length() != 4 || !newPin.matches("\\d{4}")) {
            return "New PIN must be 4 digits.";
        }
        
        user.setPin(newPin);
        return "PIN changed successfully.";
    }

    public static String resetPin(String idNumber) {
        User user = getUser();
        if (user == null) {
            return "User not found.";
        }
        
        // In a real system, this would verify the ID against stored data
        if (idNumber.length() < 6) {
            return "Invalid ID number.";
        }
        
        // Generate a temporary PIN
        String tempPin = String.format("%04d", new Random().nextInt(10000));
        user.setPin(tempPin);
        
        return "Your PIN has been reset. Your temporary PIN is: " + tempPin;
    }

    public static String initiateReversal(String transactionId) {
        User user = getUser();
        if (user == null) {
            return "User not found.";
        }
        
        for (Transaction t : user.getTransactions()) {
            if (t.getId().equals(transactionId)) {
                // In a real system, this would start a reversal workflow
                return "Reversal initiated for transaction " + transactionId + 
                       ". You will receive an SMS confirmation shortly.";
            }
        }
        
        return "Transaction ID not found.";
    }
    
    // Helper method to get current user
    private static User getUser() {
        return users.get(currentUserPhone);
    }
    
    // Inner classes for data modeling
    
    static class User {
        private final String phoneNumber;
        private String pin;
        private double balance;
        private final List<Transaction> transactions;
        private final List<String> approvals;
        
        public User(String phoneNumber, String pin, double balance, List<Transaction> transactions) {
            this.phoneNumber = phoneNumber;
            this.pin = pin;
            this.balance = balance;
            this.transactions = transactions;
            this.approvals = new ArrayList<>();
        }
        
        public String getPhoneNumber() {
            return phoneNumber;
        }
        
        public boolean validatePin(String inputPin) {
            return pin.equals(inputPin);
        }
        
        public void setPin(String newPin) {
            this.pin = newPin;
        }
        
        public double getBalance() {
            return balance;
        }
        
        public List<Transaction> getTransactions() {
            return transactions;
        }
        
        public void addTransaction(Transaction transaction) {
            transactions.add(0, transaction); // Add at the beginning for reverse chronological order
            balance += transaction.getAmount();
        }
        
        public List<String> getApprovals() {
            return approvals;
        }
    }
    
    static class Transaction {
        private final String id;
        private final String description;
        private final double amount;
        private final Date date;
        
        public Transaction(String id, String description, double amount, Date date) {
            this.id = id;
            this.description = description;
            this.amount = amount;
            this.date = date;
        }
        
        public String getId() {
            return id;
        }
        
        public String getDescription() {
            return description;
        }
        
        public double getAmount() {
            return amount;
        }
        
        public Date getDate() {
            return date;
        }
    }
}