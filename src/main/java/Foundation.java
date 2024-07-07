import java.util.Stack;

public class Foundation {
    private final Stack<Card> _foundation;

    public Foundation() {
        _foundation = new Stack<>();
    }

    /**
     * Places a given card on the foundation Stack if the move is valid
     *
     * @param card The card to be placed
     * @return True if the card has been placed on the foundation, false otherwise
     */
    public boolean placeCard(Card card) {
        if (isValidMove(card)) {
            _foundation.push(card);
            return true;
        }
        return false;
    }


    /**
     * Gets the top card from the foundation, removes and returns it.
     */
    public void pickUpCard() {
        if (_foundation.isEmpty()) {
            throw new RuntimeException("Tried to pick up a card from an empty foundation.");
        }
        _foundation.pop();
    }

    /**
     * Checks if a card can be placed on the foundation
     *
     * @param card The card to be placed
     * @return True if the card can be placed on the foundation, false otherwise
     */
    private boolean isValidMove(Card card) {
        if (_foundation.isEmpty()) {
            return card.getValue() == 1;
        } else {
            Card topCard = _foundation.peek();
            return topCard.getSuit() == card.getSuit() && topCard.getValue() == card.getValue() - 1;
        }
    }

    /**
     * Removes all Cards from a foundation
     */
    public void resetFoundation() {
        _foundation.clear();
    }

    public Card peekFoundation() {
        if (_foundation.isEmpty()) {
            return null;
        }
        return _foundation.peek();
    }

    public boolean isEmpty() {
        return _foundation.isEmpty();
    }
}
