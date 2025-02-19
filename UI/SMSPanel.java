// SMSPanel.java
package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SMSPanel extends JPanel {
    private JTable messageTable;
    private DefaultTableModel tableModel;
    private JTextArea messageArea;
    private JTextField recipientField;

    public SMSPanel() {
        setLayout(new BorderLayout());
        
        // Create the title
        JLabel title = new JLabel("SMS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Create message composition panel
        JPanel composePanel = new JPanel(new BorderLayout());
        recipientField = new JTextField();
        messageArea = new JTextArea(4, 40);
        messageArea.setLineWrap(true);
        JButton sendButton = new JButton("Send");

        composePanel.add(new JLabel("To:"), BorderLayout.NORTH);
        composePanel.add(recipientField, BorderLayout.CENTER);
        
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.SOUTH);
        
        composePanel.add(messagePanel, BorderLayout.SOUTH);

        // Create message history table
        String[] columns = {"Time", "Contact", "Message", "Type"};
        tableModel = new DefaultTableModel(columns, 0);
        messageTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(messageTable);

        // Add components
        add(composePanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Add send button action
        sendButton.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String recipient = recipientField.getText();
        String message = messageArea.getText();
        if (!recipient.isEmpty() && !message.isEmpty()) {
            tableModel.addRow(new Object[]{
                java.time.LocalTime.now().toString(),
                recipient,
                message,
                "Sent"
            });
            recipientField.setText("");
            messageArea.setText("");
        }
    }
}
