import java.util.Stack;

public class Tableau {
    private final Stack<Card> _tableau;

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
     * @return True if the card has been placed on the tableau, false otherwise
     */
    public boolean placeCard(Card card) {
        if (isValidMove(card)) {
            _tableau.push(card);
            return true;
        }
        return false;
    }

    /**
     * remove the top card from the tableau
     * Also sets the next card, if there, visible
     */
    public Card pickUpCard() {
        if (_tableau.isEmpty()) {
            throw new RuntimeException("Tried to pick up a card from an empty tableau.");
        }
        Card card = _tableau.pop();
        Card topCard = peekTableau();
        if (topCard != null) {
            topCard.set_isVisible(true);
        }
        return card;
    }

    /**
     * Checks if a card can be placed on the tableau
     *
     * @param card The card to be placed
     * @return True if the card can be placed on the tableau, false otherwise
     */
    private boolean isValidMove(Card card) {
        if (_tableau.isEmpty()) {
            return card.getValue() == 13;
        } else {
            Card topCard = _tableau.peek();
            return topCard.isOppositeColor(card) && topCard.getValue() == card.getValue() + 1;
        }
    }

    /**
     * Removes all Cards from a tableau
     */
    public void resetTableau() {
        _tableau.clear();
    }

    public Card peekTableau() {
        if (_tableau.isEmpty()) {
            return null;
        }
        return _tableau.peek();
    }

    /**
     * Returns true if the tableau is empty
     *
     * @return True if the tableau is empty, false otherwise
     */
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

    public Stack<Card> get_tableau() {
        return _tableau;
    }
}
