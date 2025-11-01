package controller;

import model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameMoveManagerTest {
    private SolitaireController solitaireController;

    @BeforeEach
    void setUp() {
        solitaireController = new SolitaireController();

    }

    @Test
    void stepBack_ShouldMoveAllCardsToDiscardPile_WhenDiscardPileIsEmpty() {
        List<Card> deckCards = new ArrayList<>();
        for (int i = 0; i < solitaireController.get_deck().get_deck().size(); i++) {
            solitaireController.placeCardOnDiscardPile(solitaireController.drawCardFromDeck());
            deckCards.add(solitaireController.get_discardPile().get_discardPile().peek());
        }
        solitaireController.reDealCards();
        solitaireController.stepBack();
        for (int i = 0; i < deckCards.size(); i++) {
            assertTrue(solitaireController.get_discardPile().get_discardPile().get(i).isVisible());
        }
        assertEquals(0, solitaireController.get_deck().get_deck().size());
        assertEquals(24, solitaireController.get_discardPile().get_discardPile().size());
    }

    @Test
    void stepBack_ShouldPutTopDiscardPileCardBackToDeck_WhenDiscardPileIsNotEmpty() {
        Card card = solitaireController.drawCardFromDeck();
        solitaireController.placeCardOnDiscardPile(card);
        solitaireController.stepBack();
        assertEquals(card, solitaireController.get_deck().get_deck().peek());
        assertTrue(solitaireController.get_discardPile().isEmpty());
    }
}