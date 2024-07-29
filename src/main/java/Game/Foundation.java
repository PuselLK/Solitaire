package Game;

import java.util.Stack;

/**
 * Represents a foundation in a game of solitaire. A foundation is a stack of cards, where each card
 * must be of the same suit and one value higher than the previous card.
 */
public class Foundation implements CardHolder {
    private final Stack<Card> _foundation;

    /**
     * Creates a new foundation with an empty stack of cards
     */
    public Foundation() {
        _foundation = new Stack<>();
    }

    /**
     * Places a given card on the foundation
     *
     * @param card The card to be placed
     */
    @Override
    public void placeCard(Card card) {
        _foundation.push(card);
    }


    /**
     * Removes the top card from the foundation
     *
     * @throws RuntimeException if the foundation is empty
     */
    @Override
    public Card pickUpCard() {
        if (_foundation.isEmpty()) {
            throw new RuntimeException("Tried to pick up a card from an empty foundation.");
        }
        return _foundation.pop();
    }

    /**
     * Checks if a card can be placed on the foundation
     * A card can be placed on the foundation if it is the same suit as the top card of the foundation
     * and its value is one higher
     * it also has to be the top most card of a tableau
     *
     * @param card      The card to be placed
     * @param isTopCard Has to be true to be a valid move.
     * @return True if the card can be placed on the foundation, false otherwise
     */
    public boolean isValidMove(Card card, boolean isTopCard) {
        if (!isTopCard) {
            return false;
        }
        if (_foundation.isEmpty()) {
            return card.getValue() == 1;
        } else {
            Card topCard = _foundation.peek();
            return topCard.getSuit() == card.getSuit() && topCard.getValue() == card.getValue() - 1;
        }
    }

    /**
     * Returns the top card of the foundation without removing it
     *
     * @return The top card of the foundation, or null if the foundation is empty
     */
    @Override
    public Card peek() {
        if (_foundation.isEmpty()) {
            return null;
        }
        return _foundation.peek();
    }

    /**
     * Checks if the foundation is empty
     *
     * @return True if the foundation is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return _foundation.isEmpty();
    }
}
