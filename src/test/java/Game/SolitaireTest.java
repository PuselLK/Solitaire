package Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolitaireTest {
    private Solitaire solitaire;

    @BeforeEach
    void setUp() {
        solitaire = new Solitaire();
    }

    @Test
    void SolitaireConstructor_ShouldSetSolitaireCorrectly() {
        for (Foundation foundation : solitaire.get_foundationsArray()) {
            assertTrue(foundation.isEmpty());
        }

        for (Tableau tableau : solitaire.get_tableausArray()) {
            int tableauSize = 0;
            tableauSize++;
            assertEquals(tableauSize, tableau.getTableauSize());
            assertTrue(tableau.peekTableau().isVisible());
        }

        assertTrue(solitaire.get_deck().isDiscardPileEmpty());
        assertEquals(24, solitaire.get_deck().get_deck().size());
    }


}