package view;

import controller.SolitaireController;

import javax.swing.*;
import java.awt.*;

/**
 * The main class for the Solitaire GUI
 */
public class SolitaireView {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 800;

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
        _frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        ToolbarPanel toolbarPanel = new ToolbarPanel(_solitaireController);
        _frame.add(toolbarPanel.getToolBar(), BorderLayout.NORTH);

        GamePanel gamePanel = new GamePanel(_solitaireController);
        _frame.add(gamePanel.getMainPane(), BorderLayout.CENTER);

        _frame.setVisible(true);
        GamePanel.renderGameState();
    }
}
