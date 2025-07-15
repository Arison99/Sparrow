package core;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BackendService {
    // User data store (in-memory, can be replaced with DB later)
    private static final Map<String, User> users = new HashMap<>();
    private static String currentUserPhone = null; // No default user

    // --- User Management ---
    public static boolean addUser(String phone, String pin, double initialBalance) {
        if (users.containsKey(phone)) return false;
        users.put(phone, new User(phone, pin, initialBalance, new ArrayList<>()));
        return true;
    }

    public static boolean setCurrentUser(String phone) {
        if (users.containsKey(phone)) {
            currentUserPhone = phone;
            return true;
        }
        return false;
    }

    public static String getCurrentUserPhone() {
        return currentUserPhone;
    }

    // --- Transaction Simulation ---
    public static boolean addTransaction(String phone, String id, String description, double amount, Date date) {
        User user = users.get(phone);
        if (user == null) return false;
        user.addTransaction(new Transaction(id, description, amount, date));
        return true;
    }

    // --- Business Logic ---
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
            if (count >= 5) break;
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
        if (idNumber.length() < 6) {
            return "Invalid ID number.";
        }
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
                return "Reversal initiated for transaction " + transactionId +
                       ". You will receive an SMS confirmation shortly.";
            }
        }
        return "Transaction ID not found.";
    }

    // --- Expose users for adapters and UI ---
    public static Map<String, User> getUsers() {
        return users;
    }

    // --- Helper ---
    private static User getUser() {
        if (currentUserPhone == null) return null;
        return users.get(currentUserPhone);
    }

    // --- Data Models ---
    public static class User {
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
        public String getPhoneNumber() { return phoneNumber; }
        public boolean validatePin(String inputPin) { return pin.equals(inputPin); }
        public void setPin(String newPin) { this.pin = newPin; }
        public double getBalance() { return balance; }
        public List<Transaction> getTransactions() { return transactions; }
        public void addTransaction(Transaction transaction) {
            transactions.add(0, transaction);
            balance += transaction.getAmount();
        }
        public List<String> getApprovals() { return approvals; }
    }
    public static class Transaction {
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
        public String getId() { return id; }
        public String getDescription() { return description; }
        public double getAmount() { return amount; }
        public Date getDate() { return date; }
    }

    // --- For future: REST API endpoints can be added here or in a controller class ---
}