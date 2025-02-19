// SettingsPanel.java
package UI;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel() {
        setLayout(new BorderLayout());
        
        // Create the title
        JLabel title = new JLabel("Settings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Create settings options
        JPanel settingsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        
        // Profile Settings
        JPanel profilePanel = createSettingSection("Profile Settings");
        profilePanel.add(new JLabel("Name:"));
        profilePanel.add(new JTextField(20));
        profilePanel.add(new JLabel("Phone Number:"));
        profilePanel.add(new JTextField(20));
        settingsPanel.add(profilePanel);

        // Notification Settings
        JPanel notificationPanel = createSettingSection("Notification Settings");
        notificationPanel.add(new JCheckBox("Enable SMS Notifications"));
        notificationPanel.add(new JCheckBox("Enable Call Notifications"));
        notificationPanel.add(new JCheckBox("Enable Mobile Money Alerts"));
        settingsPanel.add(notificationPanel);

        // Display Settings
        JPanel displayPanel = createSettingSection("Display Settings");
        String[] themes = {"Light", "Dark", "System Default"};
        displayPanel.add(new JLabel("Theme:"));
        displayPanel.add(new JComboBox<>(themes));
        displayPanel.add(new JLabel("Font Size:"));
        displayPanel.add(new JSlider(JSlider.HORIZONTAL, 8, 24, 12));
        settingsPanel.add(displayPanel);

        // Security Settings
        JPanel securityPanel = createSettingSection("Security Settings");
        securityPanel.add(new JCheckBox("Enable PIN Protection"));
        securityPanel.add(new JCheckBox("Enable Biometric Authentication"));
        securityPanel.add(new JButton("Change PIN"));
        settingsPanel.add(securityPanel);

        // Add save button
        JButton saveButton = new JButton("Save Settings");
        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(
            this, "Settings saved successfully!", "Success", 
            JOptionPane.INFORMATION_MESSAGE));

        // Add components to main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(settingsPanel), BorderLayout.CENTER);
        mainPanel.add(saveButton, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createSettingSection(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }
}