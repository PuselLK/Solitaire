package Game;

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
        assertNotNull(deck.drawCardFromDeck());
    }

    @Test
    void drawCardFromDeck_shouldReturnNullIfDeckIsEmpty() {
        //Remove all cards from the deck
        for (int i = 0; i < 52; i++) {
            deck.drawCardFromDeck();
        }
        assertNull(deck.drawCardFromDeck());
    }

    @Test
    void reDealCards_shouldMoveAllCardsFromDiscardPileToDeck() {
        //Remove all cards from the deck and add them to the discardPile
        for (int i = 0; i < 52; i++) {
            deck.placeCardOnDiscardPile(deck.drawCardFromDeck());
        }
        assertEquals(0, deck.get_deck().size());
        assertEquals(52, deck.get_discardPile().size());
        deck.reDealCards();
        assertEquals(52, deck.get_deck().size());
        assertEquals(0, deck.get_discardPile().size());
    }

    @Test
    void stepBack_ShouldMoveAllCardsToDiscardPile_WhenDiscardPileIsEmpty() {
        for (int i = 0; i < 52; i++) {
            deck.placeCardOnDiscardPile(deck.drawCardFromDeck());
        }
        deck.reDealCards();
        deck.stepBack();
        for (int i = 0; i < 52; i++) {
            assertTrue(deck.get_discardPile().get(i).isVisible());
        }
        assertEquals(0, deck.get_deck().size());
        assertEquals(52, deck.get_discardPile().size());
    }

    @Test
    void stepBack_ShouldPutTopDiscardPileCardBackToDeck_WhenDiscardPileIsNotEmpty() {
        deck.placeCardOnDiscardPile(deck.drawCardFromDeck());
        deck.stepBack();
        assertEquals(1, deck.get_deck().size());
        assertEquals(0, deck.get_discardPile().size());
    }

}