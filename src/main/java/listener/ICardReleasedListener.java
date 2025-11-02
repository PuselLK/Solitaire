package listener;

import model.Card;

import java.awt.*;

public interface ICardReleasedListener {
    void onCardReleased(Card card, String source, Container parentContainer);
}
