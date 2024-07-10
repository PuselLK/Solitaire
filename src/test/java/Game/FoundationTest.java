package Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoundationTest {
    private Foundation foundation;

    @BeforeEach
    void setUp() {
        foundation = new Foundation();
    }

    @Test
    void placeCard_ShouldReturnTrue_WhenAceIsPlacedOnEmptyFoundation() {
        Card cardAce = new Card(Card.Suit.heart, 1);
        assertTrue(foundation.placeCard(cardAce));
    }

    @Test
    void placeCard_ShouldReturnFalse_WhenCardIsNotAceAndFoundationIsEmpty() {
        Card card = new Card(Card.Suit.heart, 2);
        assertFalse(foundation.placeCard(card));
    }

    @Test
    void placeCard_ShouldReturnTrue_WhenCardIsPlacedOnFoundationWithSameSuitAndValueOneHigher() {
        Card card1 = new Card(Card.Suit.heart, 1);
        Card card2 = new Card(Card.Suit.heart, 2);
        foundation.placeCard(card1);
        assertTrue(foundation.placeCard(card2));
    }

    @Test
    void placeCard_ShouldReturnFalse_WhenCardIsPlacedOnFoundationWithDifferentSuit() {
        Card card1 = new Card(Card.Suit.heart, 1);
        Card card2 = new Card(Card.Suit.spade, 2);
        foundation.placeCard(card1);
        assertFalse(foundation.placeCard(card2));
    }

    @Test
    void placeCard_ShouldReturnFalse_WhenCardIsPlacedOnFoundationWithSameSuitButValueNotOneHigher() {
        Card card1 = new Card(Card.Suit.heart, 1);
        Card card2 = new Card(Card.Suit.heart, 3);
        foundation.placeCard(card1);
        assertFalse(foundation.placeCard(card2));
    }

    @Test
    void pickUpCard_ShouldThrowRuntimeException_WhenFoundationIsEmpty() {
        assertThrows(RuntimeException.class, () -> foundation.pickUpCard());
    }

    @Test
    void pickUpCard_ShouldRemoveTopCardFromFoundation() {
        Card card = new Card(Card.Suit.heart, 1);
        foundation.placeCard(card);
        foundation.pickUpCard();
        assertTrue(foundation.isEmpty());
    }

    @Test
    void resetFoundation_ShouldRemoveAllCardsFromFoundation() {
        Card card = new Card(Card.Suit.heart, 1);
        foundation.placeCard(card);
        foundation.resetFoundation();
        assertTrue(foundation.isEmpty());
    }

    @Test
    void peekFoundation_ShouldReturnTopCardOfFoundation() {
        Card card = new Card(Card.Suit.heart, 1);
        foundation.placeCard(card);
        assertEquals(card, foundation.peekFoundation());
    }

    @Test
    void peekFoundation_ShouldReturnNull_WhenFoundationIsEmpty() {
        assertNull(foundation.peekFoundation());
    }

    @Test
    void isEmpty_ShouldReturnTrue_WhenFoundationIsEmpty() {
        assertTrue(foundation.isEmpty());
    }

    @Test
    void isEmpty_ShouldReturnFalse_WhenFoundationIsNotEmpty() {
        Card card = new Card(Card.Suit.heart, 1);
        foundation.placeCard(card);
        assertFalse(foundation.isEmpty());
    }
}