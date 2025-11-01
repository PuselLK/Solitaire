package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a move in the game. This can be a card drawn or moved around the game
 */
public class GameMove {
    private final List<Card> _movedCards;
    private final ICardHolder _origin;
    private ICardHolder _destination;
    private boolean _tableauCardWasVisible;

    public GameMove(ICardHolder origin, ICardHolder destination) {
        _movedCards = new ArrayList<>();
        _origin = origin;
        _destination = destination;
        _tableauCardWasVisible = false;
    }

    public void addCard(Card card) {
        _movedCards.add(card);
    }

    public void set_destination(ICardHolder destination) {
        _destination = destination;
    }

    public List<Card> getMovedCards() {
        return _movedCards;
    }

    public ICardHolder getOrigin() {
        return _origin;
    }

    public ICardHolder getDestination() {
        return _destination;
    }

    public void setTableauCardWasVisible(boolean tableauCardWasVisible) {
        _tableauCardWasVisible = tableauCardWasVisible;
    }

    public boolean getTableauCardWasVisible() {
        return _tableauCardWasVisible;
    }
}
