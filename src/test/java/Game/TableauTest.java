package Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class TableauTest {
    Tableau tableau;

    @BeforeEach
    void setUp() {
        Stack<Card> initCards = new Stack<>();
        tableau = new Tableau(initCards);
    }

    @Test
    void PlaceCard_ShouldReturnTrue_WhenKingIsPlacedOnEmptyTableau() {
        Card card = new Card(Card.Suit.heart, 13);
        assertTrue(tableau.placeCard(card));
    }

    @Test
    void PlaceCard_ShouldReturnFalse_WhenCardIsNotKingAndTableauIsEmpty() {
        Card card = new Card(Card.Suit.heart, 12);
        assertFalse(tableau.placeCard(card));
    }

    @Test
    void PlaceCard_ShouldReturnTrue_WhenCardIsPlacedOnTableauWithOppositeColorAndValueOneLower() {
        Card card1 = new Card(Card.Suit.heart, 13);
        Card card2 = new Card(Card.Suit.spade, 12);
        tableau.placeCard(card1);
        assertTrue(tableau.placeCard(card2));
    }

    @Test
    void PlaceCard_ShouldReturnFalse_WhenCardIsPlacedOnTableauWithSameColor() {
        Card card1 = new Card(Card.Suit.heart, 13);
        Card card2 = new Card(Card.Suit.heart, 12);
        tableau.placeCard(card1);
        assertFalse(tableau.placeCard(card2));
    }

    @Test
    void PlaceCard_ShouldReturnFalse_WhenCardIsPlacedOnTableauWithSameColorAndValueNotOneLower() {
        Card card1 = new Card(Card.Suit.heart, 13);
        Card card2 = new Card(Card.Suit.heart, 11);
        tableau.placeCard(card1);
        assertFalse(tableau.placeCard(card2));
    }

    @Test
    void PickUpCard_ShouldThrowRuntimeException_WhenTableauIsEmpty() {
        assertThrows(RuntimeException.class, () -> tableau.pickUpCard());
    }

    @Test
    void PickUpCard_ShouldRemoveTopCardFromTableau() {
        Card card = new Card(Card.Suit.heart, 13);
        tableau.placeCard(card);
        tableau.pickUpCard();
        assertTrue(tableau.isEmpty());
    }

    @Test
    void PickUpCard_ShouldSetNextCardVisible() {
        Card card1 = new Card(Card.Suit.heart, 13);
        Card card2 = new Card(Card.Suit.spade, 12);
        tableau.placeCard(card1);
        tableau.placeCard(card2);
        tableau.pickUpCard();
        assertTrue(tableau.peekTableau().isVisible());
    }

    @Test
    void peekTableau_ShouldReturnNull_WhenTableauIsEmpty() {
        assertNull(tableau.peekTableau());
    }

    @Test
    void resetTableau_ShouldRemoveAllCardsFromTableau() {
        Card card = new Card(Card.Suit.heart, 13);
        tableau.placeCard(card);
        tableau.resetTableau();
        assertTrue(tableau.isEmpty());
    }

    @Test
    void getTableau_ShouldReturnTableau() {
        Stack<Card> tableauStack = tableau.get_tableau();
        assertNotNull(tableauStack);
    }

    @Test
    void getTableauSize_ShouldReturnTableauSize() {
        assertEquals(0, tableau.getTableauSize());
    }

    @Test
    void getTableauSize_ShouldReturnCorrectTableauSize() {
        Card card = new Card(Card.Suit.heart, 13);
        tableau.placeCard(card);
        assertEquals(1, tableau.getTableauSize());
    }

    @Test
    void isEmpty_ShouldReturnFalse_WhenTableauIsNotEmpty() {
        Card card = new Card(Card.Suit.heart, 13);
        tableau.placeCard(card);
        assertFalse(tableau.isEmpty());
    }

    @Test
    void isEmpty_ShouldReturnTrue_WhenTableauIsEmpty() {
        assertTrue(tableau.isEmpty());
    }
}