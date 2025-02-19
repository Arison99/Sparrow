// MobileMoneyPanel.java
package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MobileMoneyPanel extends JPanel {
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTextField recipientField, amountField;
    private JComboBox<String> transactionType;

    public MobileMoneyPanel() {
        setLayout(new BorderLayout());
        
        // Create the title
        JLabel title = new JLabel("Mobile Money", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Create transaction form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        recipientField = new JTextField();
        amountField = new JTextField();
        String[] types = {"Send Money", "Request Money", "Buy Airtime", "Pay Bill"};
        transactionType = new JComboBox<>(types);
        JButton submitButton = new JButton("Submit Transaction");

        formPanel.add(new JLabel("Transaction Type:"));
        formPanel.add(transactionType);
        formPanel.add(new JLabel("Recipient/Account:"));
        formPanel.add(recipientField);
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel(""));
        formPanel.add(submitButton);

        // Create transaction history table
        String[] columns = {"Time", "Type", "Recipient", "Amount", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);

        // Add components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);

        // Add submit button action
        submitButton.addActionListener(e -> submitTransaction());
    }

    private void submitTransaction() {
        String type = (String) transactionType.getSelectedItem();
        String recipient = recipientField.getText();
        String amount = amountField.getText();
        if (!recipient.isEmpty() && !amount.isEmpty()) {
            tableModel.addRow(new Object[]{
                java.time.LocalTime.now().toString(),
                type,
                recipient,
                amount,
                "Completed"
            });
            recipientField.setText("");
            amountField.setText("");
        }
    }
}
