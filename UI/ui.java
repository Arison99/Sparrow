package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public UI() {
        setTitle("Phone Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the navbar
        JMenuBar menuBar = new JMenuBar();
        JMenu phonebookMenu = new JMenu("Phonebook");
        JMenu callsMenu = new JMenu("Calls");
        JMenu smsMenu = new JMenu("SMS");
        JMenu mobileMoneyMenu = new JMenu("Mobile Money");
        JMenu settingsMenu = new JMenu("Settings");

        menuBar.add(phonebookMenu);
        menuBar.add(callsMenu);
        menuBar.add(smsMenu);
        menuBar.add(mobileMoneyMenu);
        menuBar.add(settingsMenu);

        setJMenuBar(menuBar);

        // Create the main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add different panels to the main panel
        mainPanel.add(createPhonebookPanel(), "Phonebook");
        mainPanel.add(createCallsPanel(), "Calls");
        mainPanel.add(createSMSPanel(), "SMS");
        mainPanel.add(createMobileMoneyPanel(), "Mobile Money");
        mainPanel.add(createSettingsPanel(), "Settings");

        add(mainPanel);

        // Add action listeners to menu items
        phonebookMenu.addActionListener(e -> cardLayout.show(mainPanel, "Phonebook"));
        callsMenu.addActionListener(e -> cardLayout.show(mainPanel, "Calls"));
        smsMenu.addActionListener(e -> cardLayout.show(mainPanel, "SMS"));
        mobileMoneyMenu.addActionListener(e -> cardLayout.show(mainPanel, "Mobile Money"));
        settingsMenu.addActionListener(e -> cardLayout.show(mainPanel, "Settings"));
    }

    private JPanel createPhonebookPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Phonebook"));
        // Add more components as needed
        return panel;
    }

    private JPanel createCallsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Calls"));
        // Add more components as needed
        return panel;
    }

    private JPanel createSMSPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("SMS"));
        // Add more components as needed
        return panel;
    }

    private JPanel createMobileMoneyPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Mobile Money"));
        // Add more components as needed
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Settings"));
        // Add more components as needed
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            ui.setVisible(true);
        });
    }
}