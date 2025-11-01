package controller;

import model.Deck;
import model.DiscardPile;
import model.Foundation;
import model.Tableau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolitaireTest {
    private Solitaire solitaire;
    private Deck deck;
    private DiscardPile discardPile;

    @BeforeEach
    void setUp() {
        solitaire = new Solitaire();
        deck = solitaire.get_deck();
        discardPile = solitaire.get_discardPile();
    }

    @Test
    void SolitaireConstructor_ShouldSetSolitaireCorrectly() {
        for (Foundation foundation : solitaire.get_foundationsArray()) {
            assertTrue(foundation.isEmpty());
        }

        int tableauSize = 0;
        for (Tableau tableau : solitaire.get_tableausArray()) {
            tableauSize++;
            assertEquals(tableauSize, tableau.getTableauSize());
            assertTrue(tableau.peek().isVisible());
        }

        assertTrue(solitaire.get_discardPile().isEmpty());
        assertEquals(24, solitaire.get_deck().get_deck().size());
    }

    @Test
    void reDealCards_shouldMoveAllCardsFromDiscardPileToDeck() {
        //Remove all cards from the deck and add them to the discardPile
        for (int i = 0; i < 24; i++) {
            discardPile.placeCard(deck.pickUpCard());
        }
        assertEquals(0, deck.get_deck().size());
        assertEquals(24, discardPile.get_discardPile().size());
        solitaire.reDealCards();
        assertEquals(24, deck.get_deck().size());
        assertEquals(0, discardPile.get_discardPile().size());
    }


}