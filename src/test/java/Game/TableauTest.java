package Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableauTest {
    Tableau tableau;

    @BeforeEach
    void setUp() {
        Stack<Card> initCards = new Stack<>();
        tableau = new Tableau(initCards);
    }

    @Test
    void isValidMove_ShouldReturnTrue_WhenTableauIsEmptyAndCardIsKing() {
        Card cardKingHeart = new Card(Card.Suit.heart, 13);
        assertTrue(tableau.isValidMove(cardKingHeart));
    }

    @Test
    void isValidMove_ShouldReturnFalse_WhenTableauIsEmptyAndCardIsNotKing() {
        Card card = new Card(Card.Suit.heart, 1);
        Card card2 = new Card(Card.Suit.heart, 12);
        assertFalse(tableau.isValidMove(card));
        assertFalse(tableau.isValidMove(card2));
    }

    @Test
    void isValidMove_ShouldReturnTrue_WhenTableauIsNotEmptyAndCardIsOppositeColorAndOneValueLower() {
        Card card = new Card(Card.Suit.heart, 13);
        Card card2 = new Card(Card.Suit.spade, 12);
        tableau.placeCard(card);
        assertTrue(tableau.isValidMove(card2));
    }

    @Test
    void isValidMove_ShouldReturnFalse_WhenTableauIsNotEmptyAndCardIsNotOppositeColor() {
        Card card = new Card(Card.Suit.heart, 13);
        Card card2 = new Card(Card.Suit.heart, 12);
        tableau.placeCard(card);
        assertFalse(tableau.isValidMove(card2));
    }

    @Test
    void isValidMove_ShouldReturnFalse_WhenTableauIsNotEmptyAndCardIsNotOneValueLower() {
        Card card = new Card(Card.Suit.heart, 13);
        Card card2 = new Card(Card.Suit.spade, 10);
        tableau.placeCard(card);
        assertFalse(tableau.isValidMove(card2));
    }
}