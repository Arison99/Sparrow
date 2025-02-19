package UI;

import javax.swing.*;
import java.awt.*;
import UI.PhonebookPanel;
import UI.CallsPanel;
import UI.SMSPanel;
import UI.MobileMoneyPanel;
import UI.SettingsPanel;


public class UI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public UI() {
        setTitle("Sparrow");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the navigation panel
        JPanel navPanel = new JPanel();
        JButton phonebookBtn = new JButton("Phonebook");
        JButton callsBtn = new JButton("Calls");
        JButton smsBtn = new JButton("SMS");
        JButton mobileMoneyBtn = new JButton("Mobile Money");
        JButton settingsBtn = new JButton("Settings");

        // Add buttons to navigation panel
        navPanel.add(phonebookBtn);
        navPanel.add(callsBtn);
        navPanel.add(smsBtn);
        navPanel.add(mobileMoneyBtn);
        navPanel.add(settingsBtn);

        // Add navigation panel to the top of the frame
        add(navPanel, BorderLayout.NORTH);

        // Create the main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add the component panels
        mainPanel.add(new PhonebookPanel(), "Phonebook");
        mainPanel.add(new CallsPanel(), "Calls");
        mainPanel.add(new SMSPanel(), "SMS");
        mainPanel.add(new MobileMoneyPanel(), "Mobile Money");
        mainPanel.add(new SettingsPanel(), "Settings");

        // Add main panel to the center of the frame
        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners to buttons
        phonebookBtn.addActionListener(e -> cardLayout.show(mainPanel, "Phonebook"));
        callsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Calls"));
        smsBtn.addActionListener(e -> cardLayout.show(mainPanel, "SMS"));
        mobileMoneyBtn.addActionListener(e -> cardLayout.show(mainPanel, "Mobile Money"));
        settingsBtn.addActionListener(e -> cardLayout.show(mainPanel, "Settings"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            ui.setVisible(true);
        });
    }
}