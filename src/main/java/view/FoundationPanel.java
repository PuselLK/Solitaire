package view;

import listener.ICardDraggedListener;
import listener.ICardPressedListener;
import listener.ICardReleasedListener;
import model.Foundation;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static view.SolitaireView.SCREEN_HEIGHT;
import static view.SolitaireView.SCREEN_WIDTH;

public class FoundationPanel {

    private final JPanel _foundationPanel;
    private ICardPressedListener _cardPressedListener;
    private ICardReleasedListener _cardReleasedListener;
    private ICardDraggedListener _cardDraggedListener;
    public static final String FOUNDATION = "Foundation";

    public FoundationPanel() {
        _foundationPanel = new JPanel(new GridLayout(1, 4));
        _foundationPanel.setPreferredSize(new Dimension((int) (SCREEN_WIDTH * 2.0 / 4), (int) (SCREEN_HEIGHT * 0.25)));

        for (int i = 0; i < 4; i++) {
            TitledBorder titledBorder = BorderFactory.createTitledBorder(FOUNDATION + " " + (i + 1));
            titledBorder.setTitleColor(Color.BLACK);
            Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
            titledBorder.setBorder(lineBorder);

            JPanel panel = new JPanel();
            panel.setBorder(titledBorder);
            panel.setBackground(new Color(0, 128, 0));
            _foundationPanel.add(panel, BorderLayout.CENTER);
        }
    }

    /**
     * Renders the foundation piles based on the current state of the game
     */
    public void renderFoundation(Foundation[] foundations) {
        for (int i = 0; i < foundations.length; i++) {
            Foundation foundation = foundations[i];
            JPanel panel = (JPanel) _foundationPanel.getComponent(i);
            if (!foundation.isEmpty()) {
                CardLabel cardLabel = new CardLabel(foundation.peek(), FOUNDATION, panel);
                cardLabel.setListeners(_cardPressedListener, _cardReleasedListener, _cardDraggedListener);
                panel.add(cardLabel.getCardLabel());
            }
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
     * Gets the foundation panel.
     *
     * @return The foundation panel
     */
    public JPanel getFoundationPanel() {
        return _foundationPanel;
    }
}
