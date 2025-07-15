package core;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class MenuSystemImpl implements MenuSystem {
    private final Map<String, Menu> menus;
    private String currentPin;
    private String newPin;

    public MenuSystemImpl() {
        menus = new HashMap<>();
        initializeMenus();
    }

    private void initializeMenus() {
        // Main Menu
        Menu mainMenu = new Menu("MTN MoMo:", new String[] {
            "1) Send Money",
            "2) Airtime & Bundles",
            "3) Pay with MoMo",
            "4) Payments",
            "5) Savings & Loans",
            "6) Financial Services",
            "7) Invest & Insure",
            "8) My Account"
        });
        // Submenus for all main options
        menus.put("main", mainMenu);
        menus.put("sendMoney", new Menu("Send Money", new String[]{
            "1) To MoMo User", "2) To Bank", "3) To Other Networks", "0) Back"
        }));
        menus.put("airtimeBundles", new Menu("Airtime & Bundles", new String[]{
            "1) Buy Airtime", "2) Buy Data Bundle", "0) Back"
        }));
        menus.put("payWithMoMo", new Menu("Pay with MoMo", new String[]{
            "1) MoMoPay", "2) Pay Bill", "0) Back"
        }));
        menus.put("payments", new Menu("Payments", new String[]{
            "1) School Fees", "2) Utilities", "0) Back"
        }));
        menus.put("savingsLoans", new Menu("Savings & Loans", new String[]{
            "1) Save Money", "2) Request Loan", "0) Back"
        }));
        menus.put("financialServices", new Menu("Financial Services", new String[]{
            "1) Insurance", "2) Investments", "0) Back"
        }));
        menus.put("investInsure", new Menu("Invest & Insure", new String[]{
            "1) Buy Insurance", "2) Invest Now", "0) Back"
        }));
        // My Account and its submenus (as before, but separated)
        menus.put("myAccount", new Menu("My Account", new String[]{
            "1) Check Balance", "2) Fees Calculator", "3) My Approvals", "4) My Transactions", "5) Change PIN", "6) PIN reset", "7) Initiate Reversal", "0) Back", "00) Next"
        }));
        menus.put("myAccountNext", new Menu("My Account (continued)", new String[]{
            "8) Pre Approval", "9) Info", "10) MoMoPay Registration", "11) Change Language", "12) Interest Optout", "13) My Business Wallet", "0) Back"
        }));
        menus.put("checkBalance", new Menu("Check Balance", new String[]{
            "Please enter your PIN to view balance:", "0) Back to My Account"
        }));
        menus.put("feesCalculator", new Menu("Fees Calculator", new String[]{
            "Enter amount to calculate fees:", "0) Back to My Account"
        }));
        menus.put("myApprovals", new Menu("My Approvals", new String[]{
            "Loading your approvals...", "0) Back to My Account"
        }));
        menus.put("myTransactions", new Menu("My Transactions", new String[]{
            "Loading your recent transactions...", "0) Back to My Account"
        }));
        menus.put("changePin", new Menu("Change PIN", new String[]{
            "Enter your current PIN:", "0) Back to My Account"
        }));
        menus.put("newPin", new Menu("Enter New PIN", new String[]{
            "Enter your new 4-digit PIN:", "0) Back to My Account"
        }));
        menus.put("confirmPin", new Menu("Confirm New PIN", new String[]{
            "Confirm your new 4-digit PIN:", "0) Back to My Account"
        }));
        menus.put("pinReset", new Menu("PIN Reset", new String[]{
            "Enter your ID number for verification:", "0) Back to My Account"
        }));
        menus.put("initiateReversal", new Menu("Initiate Reversal", new String[]{
            "Enter transaction ID to reverse:", "0) Back to My Account"
        }));
    }

    @Override
    public void displayMenu(String menuId) {
        Menu menu = menus.get(menuId);
        if (menu != null) {
            menu.display(this, menuId);
        } else {
            JOptionPane.showMessageDialog(null, "Menu not found: " + menuId, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void processUserInput(String menuId, String input) {
        switch (menuId) {
            case "main":
                switch (input) {
                    case "1": displayMenu("sendMoney"); break;
                    case "2": displayMenu("airtimeBundles"); break;
                    case "3": displayMenu("payWithMoMo"); break;
                    case "4": displayMenu("payments"); break;
                    case "5": displayMenu("savingsLoans"); break;
                    case "6": displayMenu("financialServices"); break;
                    case "7": displayMenu("investInsure"); break;
                    case "8": displayMenu("myAccount"); break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option: " + input, "Info", JOptionPane.INFORMATION_MESSAGE);
                        displayMenu("main");
                }
                break;
            case "sendMoney":
            case "airtimeBundles":
            case "payWithMoMo":
            case "payments":
            case "savingsLoans":
            case "financialServices":
            case "investInsure":
                if ("0".equals(input)) displayMenu("main");
                else JOptionPane.showMessageDialog(null, "Option not implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "myAccount":
                processMyAccountInput(input);
                break;
            case "myAccountNext":
                if ("0".equals(input)) displayMenu("myAccount");
                else JOptionPane.showMessageDialog(null, "Option not implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "checkBalance":
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    String result = BackendService.checkBalance(input);
                    JOptionPane.showMessageDialog(null, result, "Balance Information", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
            case "feesCalculator":
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    try {
                        double amount = Double.parseDouble(input);
                        String fees = BackendService.calculateFees(amount);
                        JOptionPane.showMessageDialog(null, fees, "Fee Information", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    displayMenu("feesCalculator");
                }
                break;
            case "myApprovals":
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    String approvals = BackendService.getApprovals();
                    JOptionPane.showMessageDialog(null, approvals, "Approvals", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
            case "myTransactions":
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    String transactions = BackendService.getTransactions();
                    JOptionPane.showMessageDialog(null, transactions, "Recent Transactions", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
            case "changePin":
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    currentPin = input;
                    displayMenu("newPin");
                }
                break;
            case "newPin":
                if ("0".equals(input)) displayMenu("myAccount");
                else if (input.length() != 4 || !input.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(null, "PIN must be 4 digits", "Error", JOptionPane.ERROR_MESSAGE);
                    displayMenu("newPin");
                } else {
                    newPin = input;
                    displayMenu("confirmPin");
                }
                break;
            case "confirmPin":
                if ("0".equals(input)) displayMenu("myAccount");
                else if (!input.equals(newPin)) {
                    JOptionPane.showMessageDialog(null, "PINs do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    displayMenu("newPin");
                } else {
                    String result = BackendService.changePin(currentPin, newPin);
                    JOptionPane.showMessageDialog(null, result, "PIN Change", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
            case "pinReset":
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    String result = BackendService.resetPin(input);
                    JOptionPane.showMessageDialog(null, result, "PIN Reset", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
            case "initiateReversal":
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    String result = BackendService.initiateReversal(input);
                    JOptionPane.showMessageDialog(null, result, "Reversal Request", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
            default:
                if ("0".equals(input)) displayMenu("myAccount");
                else {
                    JOptionPane.showMessageDialog(null, "Option not implemented.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu(menuId);
                }
                break;
        }
    }

    private void processMyAccountInput(String input) {
        switch (input) {
            case "1": displayMenu("checkBalance"); break;
            case "2": displayMenu("feesCalculator"); break;
            case "3": displayMenu("myApprovals"); break;
            case "4": displayMenu("myTransactions"); break;
            case "5": displayMenu("changePin"); break;
            case "6": displayMenu("pinReset"); break;
            case "7": displayMenu("initiateReversal"); break;
            case "0": displayMenu("main"); break;
            case "00": displayMenu("myAccountNext"); break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid option: " + input, "Error", JOptionPane.ERROR_MESSAGE);
                displayMenu("myAccount");
        }
    }
}
