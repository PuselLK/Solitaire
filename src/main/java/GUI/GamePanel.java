package GUI;

import Game.Solitaire;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GamePanel {

    private static Solitaire _solitaire;
    private static JLayeredPane _mainPane;
    private static JPanel _tableauPanels;
    private static JPanel _foundationPanels;
    private static JPanel _deckPanel;
    private static JPanel _discardPilePanel;

    private static final String FOUNDATION = "Foundation";
    private static final String DISCARD_PILE = "Discard Pile";
    private static final String DECK = "Deck";
    private static final String TABLEAU = "Tableau";

    /**
     * Creates a new game panel with the given solitaire game and screen dimensions
     *
     * @param solitaire    The solitaire game to be displayed
     * @param screenWidth  The width of the screen
     * @param screenHeight The height of the screen
     */
    public GamePanel(Solitaire solitaire, double screenWidth, double screenHeight) {
        _solitaire = solitaire;

        _mainPane = new JLayeredPane();
        _mainPane.setLayout(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 80, 0));
        topPanel.setBounds(0, 0, (int) screenWidth, (int) (screenHeight * 0.25));
        topPanel.setBackground(new Color(0, 128, 0));
        _mainPane.add(topPanel, JLayeredPane.DEFAULT_LAYER);

        _foundationPanels = createFoundationPanels(screenWidth, screenHeight);
        topPanel.add(_foundationPanels);

        _discardPilePanel = createDeckPanel(DISCARD_PILE, screenWidth, screenHeight);
        topPanel.add(_discardPilePanel, BorderLayout.EAST);

        _deckPanel = createDeckPanel(DECK, screenWidth, screenHeight);
        topPanel.add(_deckPanel, BorderLayout.EAST);

        _tableauPanels = createTableauPanels(screenWidth, screenHeight);
        _mainPane.add(_tableauPanels, JLayeredPane.DEFAULT_LAYER);
    }

    public JLayeredPane getMainPane() {
        return _mainPane;
    }

    /**
     * Clears the entire game state and re-renders it based on the current state of the game
     */
    public static void renderGameState() {
        clearPanels();

        CardPanel cardPanel = new CardPanel(_solitaire, _mainPane, _foundationPanels, _deckPanel, _discardPilePanel, _tableauPanels);
        cardPanel.renderFoundation();
        cardPanel.renderDeckAndDiscardPile();
        cardPanel.renderTableau();

        _mainPane.revalidate();
        _mainPane.repaint();

        if (_solitaire.isGameFinished()) {
            JOptionPane.showOptionDialog(_mainPane, "Would you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (JOptionPane.YES_OPTION == 0) {
                _solitaire.restart();
                ToolbarPanel.resetTimer();
                GamePanel.renderGameState();
            } else if (JOptionPane.NO_OPTION == 1) {
                System.exit(0);
            }
        }
    }

    /**
     * Removes all Components from the foundation, tableau, deck and discard pile panels
     */
    private static void clearPanels() {
        for (Component component : _tableauPanels.getComponents()) {
            if (component instanceof JLayeredPane) {
                ((JLayeredPane) component).removeAll();
            }
        }
        for (Component component : _foundationPanels.getComponents()) {
            if (component instanceof JPanel) {
                ((JPanel) component).removeAll();
            }
        }
        _deckPanel.removeAll();
        _discardPilePanel.removeAll();
    }

    /**
     * Creates the foundation panels
     *
     * @param screenWidth  The width of the screen
     * @param screenHeight The height of the screen
     * @return The foundation panels
     */
    private JPanel createFoundationPanels(double screenWidth, double screenHeight) {
        JPanel foundationPanels = new JPanel(new GridLayout(1, 4));
        foundationPanels.setPreferredSize(new Dimension((int) (screenWidth * 2.0 / 4), (int) (screenHeight * 0.25)));

        for (int i = 0; i < 4; i++) {
            TitledBorder titledBorder = BorderFactory.createTitledBorder(FOUNDATION + " " + (i + 1));
            titledBorder.setTitleColor(Color.BLACK);
            Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
            titledBorder.setBorder(lineBorder);

            JPanel panel = new JPanel();
            panel.setBorder(titledBorder);
            panel.setBackground(new Color(0, 128, 0));
            foundationPanels.add(panel, BorderLayout.CENTER);
        }
        return foundationPanels;
    }

    /**
     * Creates the tableau panels
     *
     * @param screenWidth  The width of the screen
     * @param screenHeight The height of the screen
     * @return The tableau panels
     */
    private JPanel createTableauPanels(double screenWidth, double screenHeight) {
        JPanel tableauPanels = new JPanel(new GridLayout(1, 7));
        tableauPanels.setBounds(0, (int) (screenHeight * 0.25), (int) screenWidth, (int) (screenHeight * 0.65));
        tableauPanels.setBackground(new Color(0, 128, 0));

        for (int i = 0; i < 7; i++) {
            TitledBorder titledBorder = BorderFactory.createTitledBorder(TABLEAU + " " + (i + 1));
            titledBorder.setTitleColor(Color.BLACK);
            Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
            titledBorder.setBorder(lineBorder);

            JLayeredPane tableauPane = new JLayeredPane();
            tableauPane.setBorder(titledBorder);
            tableauPane.setLayout(null);
            tableauPanels.add(tableauPane);
        }
        return tableauPanels;
    }

    /**
     * Creates a deck panel with the given title
     * used for the deck and discard pile
     *
     * @param title        The title of the panel
     * @param screenWidth  The width of the screen
     * @param screenHeight The height of the screen
     * @return The border panel
     */
    private JPanel createDeckPanel(String title, double screenWidth, double screenHeight) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (screenWidth / 8), (int) (screenHeight * 0.25)));

        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleColor(Color.BLACK);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
        titledBorder.setBorder(lineBorder);

        panel.setBorder(titledBorder);
        panel.setBackground(new Color(0, 128, 0));

        return panel;
    }
}
