package model;

import java.util.Stack;

public class DiscardPile implements ICardHolder {
    private final Stack<Card> _discardPile;

    public DiscardPile() {
        _discardPile = new Stack<>();
    }

    /**
     * Places the given card on the discardPile and makes it visible
     *
     * @param card The card to be placed
     */
    @Override
    public void placeCard(Card card) {
        card.setVisibility(true);
        _discardPile.push(card);
    }

    /**
     * Pops a Card from the discardPile.
     * throws an Exception if the discardPile is empty which should not happen
     * because it can only be accessed via GUI when there is a ClickableLabel
     * and therefore a Card
     *
     * @return The card drawn from the discardPile
     */
    @Override
    public Card pickUpCard() {
        if (_discardPile.empty()) {
            throw new RuntimeException("Discard pile is empty");
        }
        return _discardPile.pop();
    }

    /**
     * Returns the card on top of the discardPile without removing it
     *
     * @return The card on top of the discardPile
     */
    @Override
    public Card peek() {
        if (_discardPile.empty()) {
            return null;
        }
        return _discardPile.peek();
    }

    /**
     * Checks if the discardPile is empty
     *
     * @return True if the discardPile is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return _discardPile.empty();
    }

    /**
     * Returns the number of cards in the discardPile
     * Used for testing purposes
     *
     * @return The number of cards in the discardPile
     */
    public Stack<Card> get_discardPile() {
        return _discardPile;
    }

}
