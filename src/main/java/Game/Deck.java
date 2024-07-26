package Game;

import java.util.Collections;
import java.util.Stack;

/**
 * The Deck class represents a deck of cards and the discardPile
 * It contains a deck and a discardPile
 * The deck is a stack of cards that is shuffled at the beginning of the game
 * The discardPile is a stack of cards that is empty at the beginning of the game
 * The discardPile is used to store the cards that are drawn from the deck and can be played
 */
public class Deck implements CardHolder {
    private final Stack<Card> _deck;
    private final Stack<Card> _discardPile;

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

        _discardPile = new Stack<>();
    }

    /**
     * Draws a card from the deck
     *
     * @return The card drawn from the deck if the deck is not empty, null otherwise
     */
    public Card drawCardFromDeck() {
        if (!_deck.empty()) {
            return _deck.pop();
        }
        return null;
    }

    /**
     * Puts all the cards from the discardPile back into the deck
     * and sets their visibility to false
     */
    public void reDealCards() {
        while (!_discardPile.empty()) {
            _discardPile.peek().set_isVisible(false);
            _deck.push(_discardPile.pop());
        }
    }

    /**
     * Places the given card on the discardPile and makes it visible
     *
     * @param card The card to be placed
     */
    public void placeCardOnDiscardPile(Card card) {
        card.set_isVisible(true);
        _discardPile.push(card);
    }

    /**
     * Pops a Card from the discardPile.
     * throws an Exception if the discardPile is empty which should not happen
     * because it can only be accessed via GUI when there is a ClickableLabel
     * and therefore a Card
     */
    public void removeCardFromDiscardPile() {
        if (_discardPile.empty()) {
            throw new RuntimeException("Discard pile is empty");
        }
        _discardPile.pop();
    }

    /**
     * Pops a card from the deck for the initial setup
     * throws an Exception if the deck is empty which should not happen
     *
     * @return The card drawn from the deck
     */
    public Card drawCardForInit() {
        if (_deck.empty()) {
            throw new RuntimeException("Deck is empty");
        }
        return _deck.pop();
    }

    /**
     * Used to go one Step back for the Deck and DiscardPile
     * If the discardPile is empty, all cards from the deck are moved to the discardPile
     * If the discardPile is not empty, the top card is moved back to the deck
     */
    public void stepBack() {
        if (_discardPile.empty()) {
            while (!_deck.empty()) {
                _deck.peek().set_isVisible(true);
                _discardPile.push(_deck.pop());
            }
        } else {
            Card card = _discardPile.pop();
            card.set_isVisible(false);
            _deck.push(card);
        }
    }

    /**
     * Returns the card on top of the deck without removing it
     *
     * @return The card on top of the deck
     */
    public Card peekDeck() {
        if (_deck.empty()) {
            return null;
        }
        return _deck.peek();
    }

    /**
     * Returns the card on top of the discardPile without removing it
     *
     * @return The card on top of the discardPile
     */
    public Card peekDiscardPile() {
        if (_discardPile.empty()) {
            return null;
        }
        return _discardPile.peek();
    }

    /**
     * Checks if the deck is empty
     *
     * @return True if the deck is empty, false otherwise
     */
    public boolean isDeckEmpty() {
        return _deck.empty();
    }

    /**
     * Checks if the discardPile is empty
     *
     * @return True if the discardPile is empty, false otherwise
     */
    public boolean isDiscardPileEmpty() {
        return _discardPile.empty();
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
