package Game;

import java.util.Stack;

/**
 * Represents a tableau in a game of solitaire. A tableau is a stack of cards, where each card
 * must be of the opposite color and one value lower than the previous card.
 */
public class Tableau implements CardHolder {
    private final Stack<Card> _tableau;

    /**
     * Creates a new tableau and initializes it with a given stack of cards
     *
     * @param tableau The stack of cards to initialize the tableau with
     */
    public Tableau(Stack<Card> tableau) {
        _tableau = new Stack<>();
        while (!tableau.isEmpty()) {
            _tableau.push(tableau.pop());
        }
    }

    /**
     * Places a given card on the tableau Stack if the move is valid
     *
     * @param card The card to be placed
     */
    @Override
    public void placeCard(Card card) {
        _tableau.push(card);
    }

    /**
     * Removes the top card from the tableau
     * Also sets the next card, if there, visible
     *
     * @return The card that was removed
     * @throws RuntimeException if the tableau is empty
     */
    @Override
    public Card pickUpCard() {
        if (_tableau.isEmpty()) {
            throw new RuntimeException("Tried to pick up a card from an empty tableau.");
        }
        return _tableau.pop();
    }

    /**
     * Checks if a card can be placed on the tableau
     *
     * @param card The card to be placed
     * @return True if the card can be placed on the tableau, false otherwise
     */
    public boolean isValidMove(Card card) {
        if (_tableau.isEmpty()) {
            return card.getValue() == 13;
        } else {
            Card topCard = _tableau.peek();
            return topCard.isOppositeColor(card) && topCard.getValue() == card.getValue() + 1;
        }
    }

    @Override
    public Card peek() {
        if (_tableau.isEmpty()) {
            return null;
        }
        return _tableau.peek();
    }

    /**
     * Checks if the tableau is empty
     *
     * @return True if the tableau is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return _tableau.isEmpty();
    }

    /**
     * Returns the size of the tableau
     *
     * @return The size of the tableau
     */
    public int getTableauSize() {
        return _tableau.size();
    }

    /**
     * Returns the tableau
     *
     * @return The tableau represented as a stack of cards
     */
    public Stack<Card> get_tableau() {
        return _tableau;
    }
}
