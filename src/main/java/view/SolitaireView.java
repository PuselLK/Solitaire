package view;

import model.Deck;
import model.DiscardPile;
import model.Foundation;
import model.Tableau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main class for the Solitaire GUI
 */
public class SolitaireView {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 800;
    private final ToolbarPanel _toolbarPanel;
    private final GamePanel _gamePanel;
    private ActionListener _gameOverListener;

    /**
     * Creates a new instance of the game GUI
     */
    public SolitaireView() {
        JFrame frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        _toolbarPanel = new ToolbarPanel();
        frame.add(_toolbarPanel.getToolBar(), BorderLayout.NORTH);

        _gamePanel = new GamePanel();
        frame.add(_gamePanel.getMainPane(), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Updates the game state by re-rendering the deck, discard pile, foundations, and tableaus
     *
     * @param deck        The deck of cards
     * @param discardPile The discard pile
     * @param foundations The array of foundation piles
     * @param tableaus    The array of tableau piles
     */
    public void updateGameState(Deck deck, DiscardPile discardPile, Foundation[] foundations, Tableau[] tableaus) {
        _gamePanel.renderGameState(deck, discardPile, foundations, tableaus);
    }

    /**
     * Adds a listener for the game over event
     *
     * @param listener The listener to add
     */
    public void addGameOverListener(ActionListener listener) {
        _gameOverListener = listener;
    }

    /**
     * Shows the game over dialog with options to play again or exit
     */
    public void showGameOverDialog() {
        int result = JOptionPane.showOptionDialog(
                _gamePanel.getMainPane(),
                "Would you like to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        if (result == JOptionPane.YES_OPTION && _gameOverListener != null) {
            _gameOverListener.actionPerformed(null);
        } else if (result == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Gets the game panel
     *
     * @return The game panel
     */
    public GamePanel getGamePanel() {
        return _gamePanel;
    }

    /**
     * Gets the toolbar panel
     *
     * @return The toolbar panel
     */
    public ToolbarPanel getToolbarPanel() {
        return _toolbarPanel;
    }
}
