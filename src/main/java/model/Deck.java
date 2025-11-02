package model;

import java.util.Collections;
import java.util.Stack;

/**
 * The Deck class represents a deck of cards and the discardPile
 * It contains a deck and a discardPile
 * The deck is a stack of cards that is shuffled at the beginning of the game
 * The discardPile is a stack of cards that is empty at the beginning of the game
 * The discardPile is used to store the cards that are drawn from the deck and can be played
 */
public class Deck implements ICardHolder {
    private final Stack<Card> _deck;

    /**
     * Constructor for the Deck class
     * Initializes the deck with all 52 cards and shuffles them
     * Initializes the discardPile
     */
    public Deck() {
        _deck = new Stack<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (int rank = 1; rank <= 13; rank++) {
                _deck.push(new Card(suit, rank));
            }
        }
        Collections.shuffle(_deck);
    }

    /**
     * Draws a card from the deck
     *
     * @return The card drawn from the deck if the deck is not empty, null otherwise
     */
    @Override
    public Card pickUpCard() {
        if (!_deck.empty()) {
            return _deck.pop();
        }
        return null;
    }

    /**
     * Places the given card on the deck and sets its visibility to false
     *
     * @param card The card to be placed
     */
    @Override
    public void placeCard(Card card) {
        card.setVisibility(false);
        _deck.push(card);
    }

    /**
     * Returns the card on top of the deck without removing it
     *
     * @return The card on top of the deck
     */
    @Override
    public Card peek() {
        if (_deck.empty()) {
            return null;
        }
        return _deck.peek();
    }

    /**
     * Checks if the deck is empty
     *
     * @return True if the deck is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return _deck.empty();
    }

    /**
     * Returns the number of cards in the deck
     * Used for testing purposes
     *
     * @return The number of cards in the deck
     */
    public Stack<Card> get_deck() {
        return _deck;
    }
}
