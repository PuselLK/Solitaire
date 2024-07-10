package Game;

class SolitaireTest {
    private Solitaire solitaire;
    private Solitaire solitaireMock;
    private Card mockCard;
    private Deck mockDeck;

//    @BeforeEach
//    void setUp() {
//        solitaire = new Solitaire();
//        solitaireMock = mock(Solitaire.class);
//        mockCard = mock(Card.class);
//        mockDeck = mock(Deck.class);
//    }

//    @Test
//    void constructor_shouldPlaceCorrectNumberOfCardsOnTableau() {
//        int totalCards = 0;
//        int tableauSize = 1;
//        for (Tableau tableau : solitaire.get_tableausArray()) {
//            assertEquals(tableauSize, tableau.getTableauSize());
//            tableauSize++;
//            totalCards += tableau.getTableauSize();
//        }
//        assertEquals(28, totalCards);
//    }
//
//    @Test
//    void placeCardOnClick_shouldReturnTrue_WhenCardIsPlacedOnFoundation_And_CardIsAce() {
//        Card card = new Card(Card.Suit.club, 1);
//        assertTrue(solitaire.placeCardOnClick(card));
//        assertTrue(solitaire.get_foundationsArray()[0].peekFoundation().equals(card));
//    }


//    @Test
//    void isGameFinished_ShouldReturnTrue_WhenAllFoundationsHaveKings() {
//        // Mock foundations to simulate a finished game
//        Card.Suit[] suits = {Card.Suit.club, Card.Suit.spade, Card.Suit.diamond, Card.Suit.heart};
//
//        for (int i = 0; i < suits.length; i++) {
//            Foundation foundation = solitaire.get_foundationsArray()[i];
//            for (int rank = 1; rank <= 13; rank++) {
//                foundation.placeCard(new Card(suits[i], rank));
//            }
//        }
//        assertTrue(solitaire.isGameFinished());
//    }
}