package Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameMoveManagerTest {
    private Solitaire solitaire;

    @BeforeEach
    void setUp() {
        solitaire = new Solitaire();

    }

    @Test
    void stepBack_ShouldMoveAllCardsToDiscardPile_WhenDiscardPileIsEmpty() {
        List<Card> deckCards = new ArrayList<>();
        for (int i = 0; i < solitaire.get_deck().get_deck().size(); i++) {
            solitaire.placeCardOnDiscardPile(solitaire.drawCardFromDeck());
            deckCards.add(solitaire.get_discardPile().get_discardPile().peek());
        }
        solitaire.reDealCards();
        solitaire.stepBack();
        for (int i = 0; i < deckCards.size(); i++) {
            assertTrue(solitaire.get_discardPile().get_discardPile().get(i).isVisible());
        }
        assertEquals(0, solitaire.get_deck().get_deck().size());
        assertEquals(24, solitaire.get_discardPile().get_discardPile().size());
    }

    @Test
    void stepBack_ShouldPutTopDiscardPileCardBackToDeck_WhenDiscardPileIsNotEmpty() {
        Card card = solitaire.drawCardFromDeck();
        solitaire.placeCardOnDiscardPile(card);
        solitaire.stepBack();
        assertEquals(card, solitaire.get_deck().get_deck().peek());
        assertTrue(solitaire.get_discardPile().isEmpty());
    }
}