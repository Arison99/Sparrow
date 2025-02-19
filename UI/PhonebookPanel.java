// PhonebookPanel.java
package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PhonebookPanel extends JPanel {
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, phoneField;

    public PhonebookPanel() {
        setLayout(new BorderLayout());
        
        // Create the title
        JLabel title = new JLabel("Phonebook", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Create the contact form
        JPanel formPanel = new JPanel(new FlowLayout());
        nameField = new JTextField(15);
        phoneField = new JTextField(15);
        JButton addButton = new JButton("Add Contact");

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(addButton);

        // Create the contact table
        String[] columns = {"Name", "Phone Number"};
        tableModel = new DefaultTableModel(columns, 0);
        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);

        // Add components to panel
        add(formPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Add button action
        addButton.addActionListener(e -> addContact());
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        if (!name.isEmpty() && !phone.isEmpty()) {
            tableModel.addRow(new Object[]{name, phone});
            nameField.setText("");
            phoneField.setText("");
        }
    }
}

