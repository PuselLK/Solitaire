package Game;

import java.util.ArrayList;
import java.util.List;

public class GameMove {
    private final List<Card> _movedCards;
    private Object _origin;
    private Object _destination;
    private boolean _tableauCardWasVisible;

    public GameMove(Object origin, Object destination) {
        _movedCards = new ArrayList<>();
        _origin = origin;
        _destination = destination;
    }

    public void addCard(Card card) {
        _movedCards.add(card);
    }

    public void set_origin(Object origin) {
        _origin = origin;
    }

    public void set_destination(Object destination) {
        _destination = destination;
    }

    public List<Card> getMovedCards() {
        return _movedCards;
    }

    public Object getOrigin() {
        return _origin;
    }

    public Object getDestination() {
        return _destination;
    }

    public void setTableauCardWasVisible(boolean tableauCardWasVisible) {
        _tableauCardWasVisible = tableauCardWasVisible;
    }

    public boolean getTableauCardWasVisible() {
        return _tableauCardWasVisible;
    }
}
