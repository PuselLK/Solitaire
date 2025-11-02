package view;

import listener.ICardDraggedListener;
import listener.ICardPressedListener;
import listener.ICardReleasedListener;
import model.DiscardPile;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static view.SolitaireView.SCREEN_HEIGHT;
import static view.SolitaireView.SCREEN_WIDTH;

public class DiscardPilePanel {
    private final JPanel _discardPilePanel;
    private ICardPressedListener _cardPressedListener;
    private ICardReleasedListener _cardReleasedListener;
    private ICardDraggedListener _cardDraggedListener;
    public static final String DISCARD_PILE = "Discard Pile";

    public DiscardPilePanel() {
        _discardPilePanel = new JPanel();
        _discardPilePanel.setPreferredSize(new Dimension(SCREEN_WIDTH / 8, (int) (SCREEN_HEIGHT * 0.25)));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(DISCARD_PILE);
        titledBorder.setTitleColor(Color.BLACK);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
        titledBorder.setBorder(lineBorder);

        _discardPilePanel.setBorder(titledBorder);
        _discardPilePanel.setBackground(new Color(0, 128, 0));
    }

    public void renderDiscardPile(DiscardPile discardPile) {
        if (!discardPile.isEmpty()) {
            CardLabel cardLabel = new CardLabel(discardPile.peek(), DISCARD_PILE, _discardPilePanel);
            cardLabel.setListeners(_cardPressedListener, _cardReleasedListener, _cardDraggedListener);
            _discardPilePanel.add(cardLabel.getCardLabel());
        }
    }

    public void setCardListeners(ICardPressedListener pressedListener,
                                 ICardReleasedListener releasedListener,
                                 ICardDraggedListener draggedListener) {

        _cardPressedListener = pressedListener;
        _cardReleasedListener = releasedListener;
        _cardDraggedListener = draggedListener;
    }

    /**
     * Gets the discard pile panel.
     *
     * @return The discard pile panel
     */
    public JPanel getDiscardPilePanel() {
        return _discardPilePanel;
    }
}
