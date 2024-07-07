import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void drawCardFromDeck_ShouldReturnCard_WhenDeckIsNotEmpty() {
        assertNotNull(deck.drawCardFromDeck());
    }

    @Test
    void drawCardFromDeck_ShouldReturnNull_WhenDeckIsEmpty() {
        for (int i = 0; i < 52; i++) {
            deck.drawCardFromDeck(); // Empty the deck
        }
        assertNull(deck.drawCardFromDeck());
    }

    @Test
    void reDealCards_ShouldMoveAllCardsFromDiscardPileToDeck() {
        Card card = deck.drawCardFromDeck();
        deck.placeCardOnDiscardPile(card);
        deck.reDealCards();
        assertFalse(deck.isDeckEmpty());
        assertTrue(deck.isDiscardPileEmpty());
    }

    @Test
    void placeCardOnDiscardPile_ShouldAddCardToDiscardPile() {
        Card card = new Card(Card.Suit.heart, 1);
        deck.placeCardOnDiscardPile(card);
        assertFalse(deck.isDiscardPileEmpty());
    }

    @Test
    void removeCardFromDiscardPile_ShouldThrowException_WhenDiscardPileIsEmpty() {
        assertThrows(RuntimeException.class, deck::removeCardFromDiscardPile);
    }

    @Test
    void drawCardForInit_ShouldReturnCard_WhenDeckIsNotEmpty() {
        assertNotNull(deck.drawCardForInit());
    }

    @Test
    void drawCardForInit_ShouldThrowException_WhenDeckIsEmpty() {
        for (int i = 0; i < 52; i++) {
            deck.drawCardFromDeck(); // Empty the deck
        }
        assertThrows(RuntimeException.class, deck::drawCardForInit);
    }

    @Test
    void peekDeck_ShouldReturnTopCard_WithoutRemovingIt() {
        Card topCard = deck.peekDeck();
        assertNotNull(topCard);
        assertFalse(deck.isDeckEmpty()); // Deck should still be full
    }

    @Test
    void peekDeck_ShouldReturnNull_WhenDeckIsEmpty() {
        for (int i = 0; i < 52; i++) {
            deck.drawCardFromDeck(); // Empty the deck
        }
        assertNull(deck.peekDeck());
    }

    @Test
    void peekDiscardPile_ShouldReturnTopCard_WithoutRemovingIt() {
        Card card = deck.drawCardFromDeck();
        deck.placeCardOnDiscardPile(card);
        assertNotNull(deck.peekDiscardPile());
    }

    @Test
    void peekDiscardPile_ShouldReturnNull_WhenDiscardPileIsEmpty() {
        assertNull(deck.peekDiscardPile());
    }

    @Test
    void isDeckEmpty_ShouldReturnFalse_WhenDeckIsNotEmpty() {
        assertFalse(deck.isDeckEmpty());
    }

    @Test
    void isDeckEmpty_ShouldReturnTrue_WhenDeckIsEmpty() {
        for (int i = 0; i < 52; i++) {
            deck.drawCardFromDeck(); // Empty the deck
        }
        assertTrue(deck.isDeckEmpty());
    }

    @Test
    void isDiscardPileEmpty_ShouldReturnTrue_WhenDiscardPileIsEmpty() {
        assertTrue(deck.isDiscardPileEmpty());
    }

    @Test
    void isDiscardPileEmpty_ShouldReturnFalse_WhenDiscardPileIsNotEmpty() {
        Card card = deck.drawCardFromDeck();
        deck.placeCardOnDiscardPile(card);
        assertFalse(deck.isDiscardPileEmpty());
    }
}