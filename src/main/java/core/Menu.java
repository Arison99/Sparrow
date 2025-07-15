package core;

import javax.swing.*;
import java.awt.*;

public class Menu {
    private final String title;
    private final String[] options;

    public Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    public void display(MenuSystem menuSystem, String menuId) {
        // ...existing code from Menu class...
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        for (String option : options) {
            JLabel optionLabel = new JLabel(option);
            optionLabel.setForeground(Color.WHITE);
            optionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(optionLabel);
        }
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLUE);
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(separator);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
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
        JTextField inputField = new JTextField(5);
        inputField.setMaximumSize(new Dimension(100, 25));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel promptLabel = new JLabel("Enter your selection:");
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(promptLabel);
        panel.add(inputField);
        JDialog dialog = new JDialog();
        dialog.setTitle("MTN MoMo USSD");
        dialog.setModal(true);
        dialog.setContentPane(panel);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(null);
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
