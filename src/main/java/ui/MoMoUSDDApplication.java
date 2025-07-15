package ui;

import core.*;
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