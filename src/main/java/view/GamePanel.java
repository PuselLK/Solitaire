package view;

import listener.ICardDraggedListener;
import listener.ICardPressedListener;
import listener.ICardReleasedListener;
import model.Deck;
import model.DiscardPile;
import model.Foundation;
import model.Tableau;

import javax.swing.*;
import java.awt.*;

import static view.SolitaireView.SCREEN_HEIGHT;
import static view.SolitaireView.SCREEN_WIDTH;

/**
 * GamePanel class that contains the main game area including tableau, foundation, deck, and discard pile panels.
 */
public class GamePanel {

    private static JLayeredPane _mainPane;
    private final FoundationPanel _foundationPanel;
    private final DiscardPilePanel _discardPilePanel;
    private final DeckPanel _deckPanel;
    private final TableauPanel _tableauPanel;

    /**
     * Creates a new game panel with the given solitaire game and screen dimensions
     */
    public GamePanel() {
        _mainPane = new JLayeredPane();
        _mainPane.setLayout(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 80, 0));
        topPanel.setBounds(0, 0, SCREEN_WIDTH, (int) (SCREEN_HEIGHT * 0.25));
        topPanel.setBackground(new Color(0, 128, 0));
        _mainPane.add(topPanel, JLayeredPane.DEFAULT_LAYER);

        _foundationPanel = new FoundationPanel();
        topPanel.add(_foundationPanel.getFoundationPanel());

        _discardPilePanel = new DiscardPilePanel();
        topPanel.add(_discardPilePanel.getDiscardPilePanel(), BorderLayout.EAST);

        _deckPanel = new DeckPanel();
        topPanel.add(_deckPanel.getDeckPanel(), BorderLayout.EAST);

        _tableauPanel = new TableauPanel();
        _mainPane.add(_tableauPanel.getTableauPanel(), JLayeredPane.DEFAULT_LAYER);
    }

    public JLayeredPane getMainPane() {
        return _mainPane;
    }

    /**
     * Clears the entire game state and re-renders it based on the current state of the game
     */
    public void renderGameState(Deck deck, DiscardPile discardPile, Foundation[] foundations, Tableau[] tableaus) {
        clearPanels();

        _foundationPanel.renderFoundation(foundations);
        _deckPanel.renderDeck(deck);
        _discardPilePanel.renderDiscardPile(discardPile);
        _tableauPanel.renderTableau(tableaus);

        _mainPane.revalidate();
        _mainPane.repaint();
    }

    /**
     * Removes all Components from the foundation, tableau, deck and discard pile panels
     */
    private void clearPanels() {
        for (Component component : _tableauPanel.getTableauPanel().getComponents()) {
            if (component instanceof JLayeredPane) {
                ((JLayeredPane) component).removeAll();
            }
        }
        for (Component component : _foundationPanel.getFoundationPanel().getComponents()) {
            if (component instanceof JPanel) {
                ((JPanel) component).removeAll();
            }
        }
        _deckPanel.getDeckPanel().removeAll();
        _discardPilePanel.getDiscardPilePanel().removeAll();
    }

    public void registerCardListeners(ICardPressedListener pressedListener,
                                      ICardReleasedListener releasedListener,
                                      ICardDraggedListener draggedListener) {

        _deckPanel.setCardListeners(pressedListener, releasedListener, draggedListener);
        _discardPilePanel.setCardListeners(pressedListener, releasedListener, draggedListener);
        _foundationPanel.setCardListeners(pressedListener, releasedListener, draggedListener);
        _tableauPanel.setCardListeners(pressedListener, releasedListener, draggedListener);
    }

    /**
     * Gets the FoundationPanel instance
     *
     * @return The FoundationPanel
     */
    public FoundationPanel getFoundationPanel() {
        return _foundationPanel;
    }

    /**
     * Gets the DiscardPilePanel instance
     *
     * @return The DiscardPilePanel
     */
    public DiscardPilePanel getDiscardPilePanel() {
        return _discardPilePanel;
    }

    /**
     * Gets the DeckPanel instance
     *
     * @return The DeckPanel
     */
    public DeckPanel getDeckPanel() {
        return _deckPanel;
    }

    /**
     * Gets the TableauPanel instance
     *
     * @return The TableauPanel
     */
    public TableauPanel getTableauPanel() {
        return _tableauPanel;
    }
}
