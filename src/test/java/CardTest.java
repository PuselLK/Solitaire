import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card cardClubOne;
    private Card cardHeartTwo;
    private Card cardSpadeThree;
    private Card cardDiamondFour;

    @BeforeEach
    void setUp() {
        cardClubOne = new Card(Card.Suit.club, 1);
        cardHeartTwo = new Card(Card.Suit.heart, 2);
        cardSpadeThree = new Card(Card.Suit.spade, 3);
        cardDiamondFour = new Card(Card.Suit.diamond, 4);
    }

    @Test
    void isOppositeColor_ShouldReturnTrue_WhenColorsAreOpposite() {
        assertTrue(cardClubOne.isOppositeColor(cardHeartTwo));
        assertFalse(cardClubOne.isOppositeColor(cardSpadeThree));

    }

    @Test
    void isRed_ShouldReturnTrue_WhenCardIsHeartOrDiamond() {
        assertTrue(cardHeartTwo.isRed());
        assertTrue(cardDiamondFour.isRed());
        assertFalse(cardClubOne.isRed());
        assertFalse(cardSpadeThree.isRed());
    }

    @Test
    void isBlack_ShouldReturnTrue_WhenCardIsClubOrSpade() {
        assertTrue(cardClubOne.isBlack());
        assertTrue(cardSpadeThree.isBlack());
        assertFalse(cardHeartTwo.isBlack());
        assertFalse(cardDiamondFour.isBlack());
    }

    @Test
    void getValue_ShouldReturnCorrectRank() {
        assertEquals(1, cardClubOne.getValue());
        assertEquals(2, cardHeartTwo.getValue());
    }

    @Test
    void getSuit_ShouldReturnCorrectSuit() {
        assertEquals(Card.Suit.club, cardClubOne.getSuit());
        assertEquals(Card.Suit.spade, cardSpadeThree.getSuit());
        assertEquals(Card.Suit.heart, cardHeartTwo.getSuit());
        assertEquals(Card.Suit.diamond, cardDiamondFour.getSuit());
    }

    @Test
    void get_imagePath_ShouldReturnCorrectPath_WhenRankIsValid() {
        String path = "src/main/resources/club/1_club.png";
        assertEquals(path, cardClubOne.get_imagePath());
    }

    @Test
    void visibilityShouldBeChanged_WhenSetVisibleIsCalled() {
        assertFalse(cardClubOne.isVisible());
        cardClubOne.set_isVisible(true);
        assertTrue(cardClubOne.isVisible());
    }

    @Test
    void testEqualsConsistency() {
        // Symmetry
        Card card = new Card(Card.Suit.club, 1);
        assertTrue(cardClubOne.equals(card));
        assertTrue(card.equals(cardClubOne));
        assertFalse(cardClubOne.equals(cardHeartTwo));

        // Transitivity
        Card cardCopy = new Card(Card.Suit.club, 1);
        assertTrue(cardClubOne.equals(card));
        assertTrue(card.equals(cardCopy));
        assertTrue(cardClubOne.equals(cardCopy));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(cardClubOne.equals(null));
    }


    @Test
    void testCardWithInvalidRank() {
        Card card = new Card(Card.Suit.club, -1);
        assertEquals("src/main/resources/card back/card_back.png", card.get_imagePath());
    }

    @Test
    void testCardWithBackside() {
        Card backsideCard = new Card(Card.Suit.spade, 0);
        assertEquals(0, backsideCard.getValue());
        assertEquals("src/main/resources/card back/card_back.png", backsideCard.get_imagePath());
    }
}