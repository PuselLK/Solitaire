package view;

import listener.ICardDraggedListener;
import listener.ICardPressedListener;
import listener.ICardReleasedListener;
import model.Card;
import model.Tableau;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static view.SolitaireView.SCREEN_HEIGHT;
import static view.SolitaireView.SCREEN_WIDTH;

public class TableauPanel {

    private final JPanel _tableauPanel;
    private ICardPressedListener _cardPressedListener;
    private ICardReleasedListener _cardReleasedListener;
    private ICardDraggedListener _cardDraggedListener;
    public static final String TABLEAU = "Tableau";

    public TableauPanel() {
        _tableauPanel = new JPanel(new GridLayout(1, 7));
        _tableauPanel.setBounds(0, (int) (SCREEN_HEIGHT * 0.25), SCREEN_WIDTH, (int) (SCREEN_HEIGHT * 0.65));
        _tableauPanel.setBackground(new Color(0, 128, 0));

        for (int i = 0; i < 7; i++) {
            TitledBorder titledBorder = BorderFactory.createTitledBorder(TABLEAU + " " + (i + 1));
            titledBorder.setTitleColor(Color.BLACK);
            Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
            titledBorder.setBorder(lineBorder);

            JLayeredPane tableauPane = new JLayeredPane();
            tableauPane.setBorder(titledBorder);
            tableauPane.setLayout(null);
            _tableauPanel.add(tableauPane);
        }
    }

    public void renderTableau(Tableau[] tableaus) {
        int cardOffset = 30;
        int tableauX_Coordinate = (_tableauPanel.getComponent(1).getWidth() - 100) / 2;

        for (int i = 0; i < tableaus.length; i++) {
            Tableau tableau = tableaus[i];

            int layer = 0;
            int yPos = 20;

            for (Card card : tableau.get_tableau()) {
                CardLabel cardLabel = new CardLabel(card, TABLEAU, (Container) _tableauPanel.getComponent(i));

                cardLabel.getCardLabel().setBounds(tableauX_Coordinate, yPos, 100, 144);
                JLayeredPane pane = (JLayeredPane) _tableauPanel.getComponent(i);
                cardLabel.setListeners(_cardPressedListener, _cardReleasedListener, _cardDraggedListener);
                pane.add(cardLabel.getCardLabel(), Integer.valueOf(layer));
                yPos += cardOffset;
                layer++;
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
     * Gets the tableau panel.
     *
     * @return The tableau panel
     */
    public JPanel getTableauPanel() {
        return _tableauPanel;
    }
}
