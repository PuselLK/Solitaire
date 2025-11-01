package view;

import controller.SolitaireController;

import javax.swing.*;
import java.awt.*;

/**
 * The main class for the Solitaire GUI
 */
public class SolitaireView {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SolitaireView::new);
    }

    /**
     * Creates a new instance of the game GUI
     */
    public SolitaireView() {
        SolitaireController _solitaireController = new SolitaireController();

        JFrame _frame = new JFrame("Solitaire");
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setLayout(new BorderLayout());
        double _screenWidth = 1200;
        double _screenHeight = 800;
        _frame.setSize((int) _screenWidth, (int) _screenHeight);

        ToolbarPanel toolbarPanel = new ToolbarPanel(_solitaireController);
        _frame.add(toolbarPanel.getToolBar(), BorderLayout.NORTH);

        GamePanel gamePanel = new GamePanel(_solitaireController, _screenWidth, _screenHeight);
        _frame.add(gamePanel.getMainPane(), BorderLayout.CENTER);

        _frame.setVisible(true);
        GamePanel.renderGameState();
    }
}
