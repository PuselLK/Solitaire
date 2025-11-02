package view;

import listener.ICardDraggedListener;
import listener.ICardPressedListener;
import listener.ICardReleasedListener;
import model.Card;
import service.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardLabel {
    private final JLabel _cardLabel;
    private ICardPressedListener _cardPressedListener;
    private ICardReleasedListener _cardReleasedListener;
    private ICardDraggedListener _cardDraggedListener;
    public static final String CARD_BACK_PATH = "src/main/resources/back/card_back.png";

    public CardLabel(Card card, String source, Container parentContainer) {
        _cardLabel = new JLabel();
        setCardLabelIcon(card);

        if (card.isVisible() || source.equals(DeckPanel.DECK)) {
            addMouseListener(card, source, parentContainer);
        }
    }

    /**
     * Returns the JLabel representing the card
     *
     * @return The JLabel of the card
     */
    public JLabel getCardLabel() {
        return _cardLabel;
    }

    /**
     * Sets the icon of a card label based on the visibility of the card
     *
     * @param card      The card to get the icon from
     */
    private void setCardLabelIcon(Card card) {
        if (card.isVisible()) {
            String fileName = "src/main/resources/" + card.getSuit() + "/" + card.getRank() + "_" + card.getSuit() + ".png";
            _cardLabel.setIcon(ImageLoader.loadCardImage(fileName));
        } else {
            _cardLabel.setIcon(ImageLoader.loadCardImage(CARD_BACK_PATH));
        }
    }

    /**
     * Adds mouse listeners to a card label
     *
     * @param card            The card that the label represents
     * @param source          The source of the card
     * @param parentContainer The container in which the card is placed
     */
    private void addMouseListener(Card card, String source, Container parentContainer) {
        _cardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //handleMousePressed(e, _cardLabel, source, parentContainer);
                if (_cardPressedListener != null) {
                    _cardPressedListener.onCardPressed(e, _cardLabel, source, parentContainer);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //handleMouseReleased(card, source, parentContainer);
                if (_cardReleasedListener != null) {
                    _cardReleasedListener.onCardReleased(card, source, parentContainer);
                }
            }
        });

        _cardLabel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //handleMouseDragged(e, source);
                if (_cardDraggedListener != null) {
                    _cardDraggedListener.onCardDragged(e, source);
                }
            }
        });
    }

    public void setListeners(ICardPressedListener pressedListener,
                             ICardReleasedListener releasedListener,
                             ICardDraggedListener draggedListener) {
        if (pressedListener != null) {
            setCardPressedListener(pressedListener);
        }
        if (releasedListener != null) {
            setCardReleasedListener(releasedListener);
        }
        if (draggedListener != null) {
            setCardDraggedListener(draggedListener);
        }
    }

    public void setCardPressedListener(ICardPressedListener listener) {
        _cardPressedListener = listener;
    }

    public void setCardReleasedListener(ICardReleasedListener listener) {
        _cardReleasedListener = listener;
    }

    public void setCardDraggedListener(ICardDraggedListener listener) {
        _cardDraggedListener = listener;
    }
}
