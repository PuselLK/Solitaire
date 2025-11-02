package view;

import listener.ICardDraggedListener;
import listener.ICardPressedListener;
import listener.ICardReleasedListener;
import model.Deck;
import service.ImageLoader;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static view.SolitaireView.SCREEN_HEIGHT;
import static view.SolitaireView.SCREEN_WIDTH;

public class DeckPanel {

    private final JPanel _deckPanel;
    private ActionListener _redrawListener;
    private ICardPressedListener _cardPressedListener;
    private ICardReleasedListener _cardReleasedListener;
    private ICardDraggedListener _cardDraggedListener;
    public static final String DECK = "Deck";
    public static final String REDRAW_IMAGE_PATH = "src/main/resources/redraw/refresh.png";

    public DeckPanel() {
        _deckPanel = new JPanel();
        _deckPanel.setPreferredSize(new Dimension(SCREEN_WIDTH / 8, (int) (SCREEN_HEIGHT * 0.25)));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(DECK);
        titledBorder.setTitleColor(Color.BLACK);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
        titledBorder.setBorder(lineBorder);

        _deckPanel.setBorder(titledBorder);
        _deckPanel.setBackground(new Color(0, 128, 0));
    }

    public void renderDeck(Deck deck) {
        if (deck.isEmpty()) {
            JLabel redrawLabel = createRedrawLabel();
            _deckPanel.add(redrawLabel);
        } else {
            CardLabel cardLabel = new CardLabel(deck.peek(), DECK, _deckPanel);
            cardLabel.setListeners(_cardPressedListener, _cardReleasedListener, _cardDraggedListener);
            _deckPanel.add(cardLabel.getCardLabel());
        }
    }

    /**
     * Creates a JLabel for the redraw button
     *
     * @return The created JLabel
     */
    private JLabel createRedrawLabel() {
        JLabel redrawLabel = new JLabel();
        redrawLabel.setIcon(ImageLoader.loadCardImage(REDRAW_IMAGE_PATH));
        redrawLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _redrawListener.actionPerformed(null);
            }
        });
        return redrawLabel;
    }

    public void addRedrawListener(ActionListener listener) {
        _redrawListener = listener;
    }

    public void setCardListeners(ICardPressedListener pressedListener,
                                 ICardReleasedListener releasedListener,
                                 ICardDraggedListener draggedListener) {

        _cardPressedListener = pressedListener;
        _cardReleasedListener = releasedListener;
        _cardDraggedListener = draggedListener;
    }

    /**
     * Gets the deck panel.
     *
     * @return The deck panel
     */
    public JPanel getDeckPanel() {
        return _deckPanel;
    }
}
