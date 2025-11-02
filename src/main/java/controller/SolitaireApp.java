package controller;

import view.SolitaireView;
import javax.swing.*;

/**
 * The main entry point for the Solitaire application.
 */
public class SolitaireApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SolitaireController controller = SolitaireController.getInstance();
            controller.initialize();
        });
    }
}
