// CallsPanel.java
package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CallsPanel extends JPanel {
    private JTable callsTable;
    private DefaultTableModel tableModel;

    public CallsPanel() {
        setLayout(new BorderLayout());
        
        // Create the title
        JLabel title = new JLabel("Calls", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Create the dialpad
        JPanel dialpadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        JTextField numberField = new JTextField();
        String[] dialpadNumbers = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"
        };

        for (String number : dialpadNumbers) {
            JButton button = new JButton(number);
            button.addActionListener(e -> numberField.setText(numberField.getText() + number));
            dialpadPanel.add(button);
        }

        // Create call controls
        JPanel controlsPanel = new JPanel(new FlowLayout());
        JButton callButton = new JButton("Call");
        JButton endButton = new JButton("End");
        controlsPanel.add(numberField);
        controlsPanel.add(callButton);
        controlsPanel.add(endButton);

        // Create call history table
        String[] columns = {"Time", "Number", "Duration", "Type"};
        tableModel = new DefaultTableModel(columns, 0);
        callsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(callsTable);

        // Add components
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlsPanel, BorderLayout.NORTH);
        topPanel.add(dialpadPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
}