package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoundationTest {
    private Foundation foundation;

    @BeforeEach
    void setUp() {
        foundation = new Foundation();
    }

    @Test
    void isValidMove_ShouldReturnTrue_WhenFoundationIsEmptyAndCardIsAce() {
        Card card = new Card(Card.Suit.heart, 1);
        assertTrue(foundation.isValidMove(card, true));
    }

    @Test
    void isValidMove_ShouldReturnFalse_WhenFoundationIsEmptyAndCardIsNotAce() {
        Card card = new Card(Card.Suit.heart, 2);
        Card card2 = new Card(Card.Suit.heart, 13);
        assertFalse(foundation.isValidMove(card, true));
        assertFalse(foundation.isValidMove(card2, true));
    }

    @Test
    void isValidMove_ShouldReturnTrue_WhenCardIsOneHigherAndSameSuit() {
        foundation.placeCard(new Card(Card.Suit.heart, 1));
        Card cardHeart = new Card(Card.Suit.heart, 2);
        assertTrue(foundation.isValidMove(cardHeart, true));
        foundation.placeCard(new Card(Card.Suit.club, 1));
        Card cardClub = new Card(Card.Suit.club, 2);
        assertTrue(foundation.isValidMove(cardClub, true));
    }

    @Test
    void isValidMove_ShouldReturnFalse_WhenCardIsNotOneHigher() {
        foundation.placeCard(new Card(Card.Suit.heart, 1));
        Card card = new Card(Card.Suit.heart, 3);
        assertFalse(foundation.isValidMove(card, true));
    }

    @Test
    void isValidMove_ShouldReturnFalse_WhenCardIsNotSameSuit() {
        foundation.placeCard(new Card(Card.Suit.heart, 1));
        Card card = new Card(Card.Suit.spade, 2);
        assertFalse(foundation.isValidMove(card, true));
    }

    @Test
    void isValidMove_ShouldReturnFalse_WhenIsTopCardIsFalse() {
        foundation.placeCard(new Card(Card.Suit.heart, 1));
        Card card = new Card(Card.Suit.heart, 2);
        assertFalse(foundation.isValidMove(card, false));
    }
}