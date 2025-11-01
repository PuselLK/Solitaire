package model;

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
    void DeckConstructor_shouldHave52Cards() {
        assertEquals(52, deck.get_deck().size());
    }

    @Test
    void DeckConstructor_ShouldAddEveryCardOnce() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (int rank = 1; rank <= 13; rank++) {
                assertTrue(deck.get_deck().contains(new Card(suit, rank)));
            }
        }
    }

    @Test
    void drawCardFromDeck_shouldReturnCardIfDeckIsNotEmpty() {
        assertNotNull(deck.pickUpCard());
    }

    @Test
    void drawCardFromDeck_shouldReturnNullIfDeckIsEmpty() {
        //Remove all cards from the deck
        for (int i = 0; i < 52; i++) {
            deck.pickUpCard();
        }
        assertNull(deck.pickUpCard());
    }
}