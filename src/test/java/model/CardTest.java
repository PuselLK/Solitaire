package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card cardHeart;
    Card cardSpade;
    Card cardClub;
    Card cardDiamond;

    @BeforeEach
    void setUp() {
        cardHeart = new Card(Card.Suit.HEART, 1);
        cardSpade = new Card(Card.Suit.SPADE, 2);
        cardClub = new Card(Card.Suit.CLUB, 3);
        cardDiamond = new Card(Card.Suit.DIAMOND, 4);
    }

    @Test
    void CardConstructor_ShouldSetImagePathCorrectly_WhenRankIsBetween_0_And_14() {
        Card cardRankOne = new Card(Card.Suit.HEART, 1);
        Card cardRank13 = new Card(Card.Suit.HEART, 13);
        assertEquals("src/main/resources/heart/1_heart.png", cardRankOne.get_imagePath());
        assertEquals("src/main/resources/heart/13_heart.png", cardRank13.get_imagePath());
    }

    @Test
    void CardConstructor_ShouldSetImagePathToCardBack_WhenRankIsNotBetween_0_And_14() {
        Card cardRank0 = new Card(Card.Suit.HEART, 0);
        Card cardRank14 = new Card(Card.Suit.HEART, 14);
        assertEquals("src/main/resources/card back/card_back.png", cardRank0.get_imagePath());
        assertEquals("src/main/resources/card back/card_back.png", cardRank14.get_imagePath());
    }

    @Test
    void isOppositeColor_ShouldReturnTrue_WhenCardsHaveOppositeColors() {
        assertTrue(cardClub.isOppositeColor(cardDiamond));
        assertTrue(cardDiamond.isOppositeColor(cardSpade));
        assertTrue(cardSpade.isOppositeColor(cardHeart));
        assertTrue(cardHeart.isOppositeColor(cardClub));
    }

    @Test
    void isOppositeColor_ShouldReturnFalse_WhenCardsHaveSameColor() {
        assertFalse(cardClub.isOppositeColor(cardSpade));
        assertFalse(cardClub.isOppositeColor(cardClub));
        assertFalse(cardDiamond.isOppositeColor(cardHeart));
        assertFalse(cardDiamond.isOppositeColor(cardDiamond));
    }

    @Test
    void isRed_ShouldReturnTrue_WhenCardIsHeartOrDiamond() {
        assertTrue(cardHeart.isRed());
        assertTrue(cardDiamond.isRed());
    }

    @Test
    void isRed_ShouldReturnFalse_WhenCardIsSpadeOrClub() {
        assertFalse(cardSpade.isRed());
        assertFalse(cardClub.isRed());
    }

    @Test
    void isBlack_ShouldReturnTrue_WhenCardIsSpadeOrClub() {
        assertTrue(cardSpade.isBlack());
        assertTrue(cardClub.isBlack());
    }

    @Test
    void isBlack_ShouldReturnFalse_WhenCardIsHeartOrDiamond() {
        assertFalse(cardHeart.isBlack());
        assertFalse(cardDiamond.isBlack());
    }

    @Test
    void equalsOtherCard_ShouldReturnTrue_WhenCardsHaveSameSuitAndRank() {
        Card card1 = new Card(Card.Suit.HEART, 1);
        Card card2 = new Card(Card.Suit.HEART, 1);
        assertEquals(card1, card2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenCardsHaveDifferentSuit() {
        Card card1 = new Card(Card.Suit.HEART, 1);
        Card card2 = new Card(Card.Suit.SPADE, 1);
        Card card3 = new Card(Card.Suit.SPADE, 2);
        Card card4 = new Card(Card.Suit.CLUB, 3);
        assertNotEquals(card1, card2); //Same rank, different suit
        assertNotEquals(card2, card3); //Same suit, different rank
        assertNotEquals(card3, card4); //Different rank, different suit
    }

    @Test
    void hashCode_ShouldReturnSameHashCode_WhenCardsHaveSameSuitAndRank() {
        Card card1 = new Card(Card.Suit.HEART, 1);
        Card card2 = new Card(Card.Suit.HEART, 1);
        assertEquals(card1.hashCode(), card2.hashCode());
    }
}