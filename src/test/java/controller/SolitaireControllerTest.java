package controller;

import model.Deck;
import model.DiscardPile;
import model.Foundation;
import model.Tableau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolitaireControllerTest {
    private SolitaireController solitaireController;
    private Deck deck;
    private DiscardPile discardPile;

    @BeforeEach
    void setUp() {
        solitaireController = new SolitaireController();
        deck = solitaireController.get_deck();
        discardPile = solitaireController.get_discardPile();
    }

    @Test
    void SolitaireConstructor_ShouldSetSolitaireCorrectly() {
        for (Foundation foundation : solitaireController.get_foundationsArray()) {
            assertTrue(foundation.isEmpty());
        }

        int tableauSize = 0;
        for (Tableau tableau : solitaireController.get_tableausArray()) {
            tableauSize++;
            assertEquals(tableauSize, tableau.getTableauSize());
            assertTrue(tableau.peek().isVisible());
        }

        assertTrue(solitaireController.get_discardPile().isEmpty());
        assertEquals(24, solitaireController.get_deck().get_deck().size());
    }

    @Test
    void reDealCards_shouldMoveAllCardsFromDiscardPileToDeck() {
        //Remove all cards from the deck and add them to the discardPile
        for (int i = 0; i < 24; i++) {
            discardPile.placeCard(deck.pickUpCard());
        }
        assertEquals(0, deck.get_deck().size());
        assertEquals(24, discardPile.get_discardPile().size());
        solitaireController.reDealCards();
        assertEquals(24, deck.get_deck().size());
        assertEquals(0, discardPile.get_discardPile().size());
    }


}