import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MoMoUSDDApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuSystem menuSystem = new MenuSystemImpl();
            UIManager.put("Panel.background", Color.DARK_GRAY);
            UIManager.put("OptionPane.background", Color.DARK_GRAY);
            UIManager.put("Panel.foreground", Color.WHITE);
            UIManager.put("OptionPane.foreground", Color.WHITE);
            UIManager.put("TextField.background", Color.DARK_GRAY);
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("Button.background", Color.DARK_GRAY);
            UIManager.put("Button.foreground", Color.CYAN);
            
            // Start with main menu
            menuSystem.displayMenu("main");
        });
    }
}

// Interface for menu system to follow SOLID principles
interface MenuSystem {
    void displayMenu(String menuId);
    void processUserInput(String menuId, String input);
}

// Implementation of menu system
class MenuSystemImpl implements MenuSystem {
    private final Map<String, Menu> menus;
    
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
        
        // My Account Menu
        Menu myAccountMenu = new Menu("My Account", new String[] {
            "1) Check Balance",
            "2) Fees Calculator",
            "3) My Approvals",
            "4) My Transactions",
            "5) Change PIN",
            "6) PIN reset",
            "7) Initiate Reversal",
            "0) Back",
            "00) Next"
        });
        
        // My Account Menu (continued)
        Menu myAccountNextMenu = new Menu("My Account (continued)", new String[] {
            "8) Pre Approval",
            "9) Info",
            "10) MoMoPay Registration",
            "11) Change Language",
            "12) Interest Optout",
            "13) My Business Wallet",
            "0) Back"
        });
        
        // Submenus for My Account options with improved functionality
        Menu checkBalanceMenu = new Menu("Check Balance", new String[] {
            "Please enter your PIN to view balance:",
            "0) Back to My Account"
        });
        
        Menu feesCalculatorMenu = new Menu("Fees Calculator", new String[] {
            "Enter amount to calculate fees:",
            "0) Back to My Account"
        });
        
        Menu myApprovalsMenu = new Menu("My Approvals", new String[] {
            "Loading your approvals...",
            "0) Back to My Account"
        });
        
        Menu myTransactionsMenu = new Menu("My Transactions", new String[] {
            "Loading your recent transactions...",
            "0) Back to My Account"
        });
        
        Menu changePINMenu = new Menu("Change PIN", new String[] {
            "Enter your current PIN:",
            "0) Back to My Account"
        });
        
        Menu pinResetMenu = new Menu("PIN Reset", new String[] {
            "Enter your ID number for verification:",
            "0) Back to My Account"
        });
        
        Menu initiateReversalMenu = new Menu("Initiate Reversal", new String[] {
            "Enter transaction ID to reverse:",
            "0) Back to My Account"
        });
        
        // Add new menus for multi-step operations
        Menu newPinMenu = new Menu("Enter New PIN", new String[] {
            "Enter your new 4-digit PIN:",
            "0) Back to My Account"
        });
        
        Menu confirmPinMenu = new Menu("Confirm New PIN", new String[] {
            "Confirm your new 4-digit PIN:",
            "0) Back to My Account"
        });
        
        // Add menus to the map
        menus.put("main", mainMenu);
        menus.put("myAccount", myAccountMenu);
        menus.put("myAccountNext", myAccountNextMenu);
        menus.put("checkBalance", checkBalanceMenu);
        menus.put("feesCalculator", feesCalculatorMenu);
        menus.put("myApprovals", myApprovalsMenu);
        menus.put("myTransactions", myTransactionsMenu);
        menus.put("changePin", changePINMenu);
        menus.put("newPin", newPinMenu);
        menus.put("confirmPin", confirmPinMenu);
        menus.put("pinReset", pinResetMenu);
        menus.put("initiateReversal", initiateReversalMenu);
    }
    
    @Override
    public void displayMenu(String menuId) {
        Menu menu = menus.get(menuId);
        if (menu != null) {
            menu.display(this, menuId);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Menu not found: " + menuId, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Temporary storage for multi-step operations
    private String currentPin;
    private String newPin;
    
    @Override
    public void processUserInput(String menuId, String input) {
        switch (menuId) {
            case "main":
                if ("8".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Please select option 8 (My Account) for this simulation",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("main");
                }
                break;
                
            case "myAccount":
                processMyAccountInput(input);
                break;
                
            case "myAccountNext":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Option " + input + " is not implemented in this simulation",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccountNext");
                }
                break;
                
            case "checkBalance":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    // Call backend service to check balance with PIN
                    String result = BackendService.checkBalance(input);
                    JOptionPane.showMessageDialog(null, result, "Balance Information", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
                
            case "feesCalculator":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
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
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    // We don't need input for approvals, just show the result
                    String approvals = BackendService.getApprovals();
                    JOptionPane.showMessageDialog(null, approvals, "Approvals", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
                
            case "myTransactions":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    // We don't need input for transactions, just show the result
                    String transactions = BackendService.getTransactions();
                    JOptionPane.showMessageDialog(null, transactions, "Recent Transactions", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
                
            case "changePin":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    // Store current PIN and go to new PIN menu
                    currentPin = input;
                    displayMenu("newPin");
                }
                break;
                
            case "newPin":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else if (input.length() != 4 || !input.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(null, "PIN must be 4 digits", "Error", JOptionPane.ERROR_MESSAGE);
                    displayMenu("newPin");
                } else {
                    // Store new PIN and go to confirmation
                    newPin = input;
                    displayMenu("confirmPin");
                }
                break;
                
            case "confirmPin":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else if (!input.equals(newPin)) {
                    JOptionPane.showMessageDialog(null, "PINs do not match", "Error", JOptionPane.ERROR_MESSAGE);
                    displayMenu("newPin");
                } else {
                    // Confirm and change the PIN
                    String result = BackendService.changePin(currentPin, newPin);
                    JOptionPane.showMessageDialog(null, result, "PIN Change", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
                
            case "pinReset":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    String result = BackendService.resetPin(input);
                    JOptionPane.showMessageDialog(null, result, "PIN Reset", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
                
            case "initiateReversal":
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    String result = BackendService.initiateReversal(input);
                    JOptionPane.showMessageDialog(null, result, "Reversal Request", JOptionPane.INFORMATION_MESSAGE);
                    displayMenu("myAccount");
                }
                break;
                
            default:
                if ("0".equals(input)) {
                    displayMenu("myAccount");
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Option " + input + " is not implemented in this simulation",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                    displayMenu(menuId);
                }
                break;
        }
    }
    
    private void processMyAccountInput(String input) {
        switch (input) {
            case "1":
                displayMenu("checkBalance");
                break;
            case "2":
                displayMenu("feesCalculator");
                break;
            case "3":
                displayMenu("myApprovals");
                break;
            case "4":
                displayMenu("myTransactions");
                break;
            case "5":
                displayMenu("changePin");
                break;
            case "6":
                displayMenu("pinReset");
                break;
            case "7":
                displayMenu("initiateReversal");
                break;
            case "0":
                displayMenu("main");
                break;
            case "00":
                displayMenu("myAccountNext");
                break;
            default:
                JOptionPane.showMessageDialog(null, 
                    "Invalid option: " + input,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                displayMenu("myAccount");
                break;
        }
    }
}

// Menu class representing a single USSD menu
class Menu {
    private final String title;
    private final String[] options;
    
    public Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }
    
    public void display(MenuSystem menuSystem, String menuId) {
        // Create custom panel for USSD style
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        
        // Add title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Add options
        for (String option : options) {
            JLabel optionLabel = new JLabel(option);
            optionLabel.setForeground(Color.WHITE);
            optionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(optionLabel);
        }
        
        // Add separator line
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLUE);
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBackground(Color.DARK_GRAY);
        
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setForeground(Color.CYAN);
        cancelButton.setBackground(Color.DARK_GRAY);
        cancelButton.setBorderPainted(false);
        
        JButton sendButton = new JButton("SEND");
        sendButton.setForeground(Color.CYAN);
        sendButton.setBackground(Color.DARK_GRAY);
        sendButton.setBorderPainted(false);
        
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        buttonsPanel.add(sendButton);
        
        panel.add(buttonsPanel);
        
        // Setup input field
        JTextField inputField = new JTextField(5);
        inputField.setMaximumSize(new Dimension(100, 25));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel promptLabel = new JLabel("Enter your selection:");
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(promptLabel);
        panel.add(inputField);
        
        // Setup dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("MTN MoMo USSD");
        dialog.setModal(true);
        dialog.setContentPane(panel);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(null);
        
        // Add action listeners
        cancelButton.addActionListener(e -> dialog.dispose());
        
        sendButton.addActionListener(e -> {
            String input = inputField.getText().trim();
            dialog.dispose();
            menuSystem.processUserInput(menuId, input);
        });
        
        inputField.addActionListener(e -> {
            String input = inputField.getText().trim();
            dialog.dispose();
            menuSystem.processUserInput(menuId, input);
        });
        
        dialog.setVisible(true);
    }
}