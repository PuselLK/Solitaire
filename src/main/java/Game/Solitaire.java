package Game;

import java.util.Stack;

/**
 * Represents the game of Solitaire.
 * Contains the Deck, the Foundations and the Tableaus.
 */
public class Solitaire {
    private Deck _deck;
    private Foundation[] _foundationsArray;
    private Tableau[] _tableausArray;
    private final GameMoveManager _gameMoveManager;

    /**
     * Constructor for the Solitaire class.
     */
    public Solitaire() {
        _gameMoveManager = new GameMoveManager();
        initializeGame();
    }

    /**
     * Restarts the Solitaire game.
     */
    public void restart() {
        _gameMoveManager.clearGameMoves();
        initializeGame();
    }

    /**
     * Initializes the Solitaire game.
     * Creates a new Deck, 4 Foundations and 7 Tableaus.
     * The Deck is initialized with 52 Cards.
     * The Foundations are initialized with 4 empty Stacks.
     * The Tableaus are initialized with 7 Stacks of Cards.
     * The first Tableau has 1 Card, the second 2 Cards, the third 3 Cards and so on.
     * The top Card of each Tableau is set visible.
     */
    private void initializeGame() {
        _deck = new Deck();

        _foundationsArray = new Foundation[4];
        for (int i = 0; i < _foundationsArray.length; i++) {
            _foundationsArray[i] = new Foundation();
        }

        _tableausArray = new Tableau[7];
        for (int i = 0; i < _tableausArray.length; i++) {
            Stack<Card> tableau = new Stack<>();
            for (int j = 0; j <= i; j++) {
                tableau.push(_deck.drawCardFromDeck());
            }
            _tableausArray[i] = new Tableau(tableau);
            _tableausArray[i].peekTableau().set_isVisible(true);
        }
    }

    /**
     * Places a card on a foundation or tableau by clicking on it.
     * If the card is the top card of the origin, it is removed from the origin.
     * If the card is not the top card of the origin, all cards on top of the card are removed from the origin.
     * The card is then placed on the foundation or tableau.
     *
     * @param card The card to be placed
     * @return True if the card was placed successfully, false otherwise
     */
    public boolean placeCardOnClick(Card card) {
        CardHolder origin = findCardOrigin(card);
        boolean isTopCard = isCardTopCard(card, origin);
        GameMove gameMove = new GameMove(origin, null);

        if (isTopCard && placeCardAlgorithm(card, true)) {
            removeCardIfTop(origin, gameMove);

            gameMove.set_destination(findCardOrigin(card));
            gameMove.addCard(card);
            _gameMoveManager.addGameMove(gameMove);
            return true;
        } else if (!isTopCard && placeCardAlgorithm(card, false)) {
            removeCardIfNotTop(origin, card, gameMove);

            addAttachedCardsToGameMove(card, gameMove);
            _gameMoveManager.addGameMove(gameMove);
            return true;
        }
        return false;
    }

    /**
     * Places a card on a foundation or tableau by dragging it.
     * If the card is the top card of the origin, it is removed from the origin.
     * If the card is not the top card of the origin, all cards on top of the card are removed from the origin.
     * The card is then placed on the foundation or tableau.
     *
     * @param card        The card to be placed
     * @param targetIndex The index of the foundation or tableau
     * @param targetType  The type of the target (Foundation or Tableau)
     * @return True if the card was placed successfully, false otherwise
     */
    public boolean placeCardOnDrag(Card card, int targetIndex, String targetType) {
        CardHolder origin = findCardOrigin(card);
        boolean isTopCard = isCardTopCard(card, origin);
        GameMove gameMove = new GameMove(origin, null);

        if ((isTopCard && targetType.equals("Foundation") && placeCardOnFoundation(card, targetIndex, true)) ||
                (isTopCard && targetType.equals("Tableau") && placeCardOnTableau(card, targetIndex))) {
            removeCardIfTop(origin, gameMove);

            gameMove.set_destination(findCardOrigin(card));
            gameMove.addCard(card);
            _gameMoveManager.addGameMove(gameMove);
            return true;
        } else if ((!isTopCard && targetType.equals("Foundation") && placeCardOnFoundation(card, targetIndex, false)) ||
                (!isTopCard && targetType.equals("Tableau") && placeCardOnTableau(card, targetIndex))) {
            removeCardIfNotTop(origin, card, gameMove);

            addAttachedCardsToGameMove(card, gameMove);
            _gameMoveManager.addGameMove(gameMove);
            return true;
        }
        return false;
    }

    /**
     * Removes the top most card from the given origin.
     * If the origin is a tableau the next card is set visible.
     *
     * @param origin The card to be removed
     */
    private void removeCardIfTop(CardHolder origin, GameMove gameMove) {
        if (origin instanceof Deck) {
            _deck.removeCardFromDiscardPile();
        } else if (origin instanceof Foundation foundation) {
            foundation.pickUpCard();
        } else if (origin instanceof Tableau tableau) {
            tableau.pickUpCard();
            Card topCard = tableau.peekTableau();
            if (topCard != null) {
                if (topCard.isVisible()) {
                    gameMove.setTableauCardWasVisible(true);
                }
                topCard.set_isVisible(true);
            }
        }
    }

    /**
     * Removes the given card and every card on top of it from the given origin.
     *
     * @param origin The card to be removed
     * @param card   The card to be removed
     * @throws RuntimeException if the origin is a Deck or Foundation
     */
    private void removeCardIfNotTop(CardHolder origin, Card card, GameMove gameMove) {
        if (!(origin instanceof Tableau tableau)) {
            throw new RuntimeException("Clicked a Card on the Discard Pile or Foundation that is not the top Card");
        }
        Stack<Card> cardsOnTop = new Stack<>();

        for (int i = tableau.getTableauSize(); i > 0; i--) {
            // remove all cards on top of the clicked card including the clicked card
            cardsOnTop.push(tableau.pickUpCard());
            if (cardsOnTop.peek().equals(card)) {
                break;
            }
        }
        // set the next card visible
        Card topCard = tableau.peekTableau();
        if (topCard != null) {
            if (topCard.isVisible()) {
                gameMove.setTableauCardWasVisible(true);
            }
            topCard.set_isVisible(true);
        }

        // remove the clicked card since it already has been placed
        cardsOnTop.pop();

        // place all cards on top of the clicked card to the tableau excluding the clicked card
        CardHolder newOrigin = findCardOrigin(card);
        if (!(newOrigin instanceof Tableau newTableau)) {
            throw new RuntimeException("Card has been moved to a Foundation or Deck/discardPile even though " +
                    "it was not the top card or the Card was not found.");
        }
        while (!cardsOnTop.isEmpty()) {
            newTableau.placeCard(cardsOnTop.pop());
        }
    }

    /**
     * Places a card on a foundation.
     *
     * @param card The card to be placed
     * @param i    The index of the foundation
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardOnFoundation(Card card, int i, boolean isTopCard) {
        if (_foundationsArray[i].isValidMove(card, isTopCard)) {
            _foundationsArray[i].placeCard(card);
            return true;
        }
        return false;
    }

    /**
     * Places a card on a tableau.
     *
     * @param card The card to be placed
     * @param i    The index of the tableau
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardOnTableau(Card card, int i) {
        if (_tableausArray[i].isValidMove(card)) {
            _tableausArray[i].placeCard(card);
            return true;
        }
        return false;
    }

    /**
     * Checks if the card is the top card of the origin.
     *
     * @param card   The card to be checked
     * @param origin The origin of the card
     * @return True if the card is the top card of the origin, false otherwise
     */
    private boolean isCardTopCard(Card card, CardHolder origin) {
        if (origin instanceof Deck) {
            return _deck.peekDiscardPile().equals(card);
        } else if (origin instanceof Tableau) {
            return ((Tableau) origin).peekTableau().equals(card);
        } else if (origin instanceof Foundation) {
            return ((Foundation) origin).peekFoundation().equals(card);
        }
        return false;

    }

    /**
     * Tries to place a card on a foundation and if not successful, tries to place it on a tableau
     * Does not remove the card from the origin
     *
     * @param card      The card to be placed
     * @param isTopCard True if the card is the top card of the origin, false otherwise
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardAlgorithm(Card card, boolean isTopCard) {
        for (Foundation foundation : _foundationsArray) {
            if (foundation.isValidMove(card, isTopCard)) {
                foundation.placeCard(card);
                return true;
            }
        }

        for (Tableau tableau : _tableausArray) {
            if (tableau.isValidMove(card)) {
                tableau.placeCard(card);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the origin of the card by checking the Deck, Tableaus and Foundations.
     *
     * @param card The card to find the origin of
     * @return The origin of the card
     */
    private CardHolder findCardOrigin(Card card) {
        if (_deck.peekDiscardPile() != null && _deck.peekDiscardPile().equals(card)) {
            return _deck;
        }

        // Tableau
        for (Tableau tableau : _tableausArray) {
            for (Card tableauCard : tableau.get_tableau()) {
                if (tableauCard.equals(card)) {
                    return tableau;
                }
            }
        }

        // Foundation
        for (Foundation foundation : _foundationsArray) {
            if (foundation.peekFoundation() != null && foundation.peekFoundation().equals(card)) {
                return foundation;
            }
        }
        return null;
    }

    /**
     * The card drawn from the deck if the deck is not empty, null otherwise
     *
     * @return The card drawn from the deck
     */
    public Card drawCardFromDeck() {
        _gameMoveManager.addGameMove(new GameMove(_deck, _deck));

        return _deck.drawCardFromDeck();
    }

    /**
     * Places the given card on the discardPile and makes it visible
     */
    public void placeCardOnDiscardPile(Card card) {
        _deck.placeCardOnDiscardPile(card);
    }

    /**
     * Puts all the cards from the discardPile back into the deck and sets their visibility to false
     */
    public void reDealCards() {
        _gameMoveManager.addGameMove(new GameMove(_deck, _deck));
        _deck.reDealCards();
    }

    /**
     * Checks if the game is finished.
     * The game is finished if all foundations are filled with a King.
     *
     * @return True if the game is finished, false otherwise
     */
    public boolean isGameFinished() {
        for (Foundation foundation : _foundationsArray) {
            if (foundation.isEmpty() || foundation.peekFoundation().getValue() != 13) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds all Cards above the clicked/dragged to the GameMove moved Cards list
     *
     * @param card     The card to be moved
     * @param gameMove The game move object containing move details
     */
    private void addAttachedCardsToGameMove(Card card, GameMove gameMove) {
        Tableau destination = (Tableau) findCardOrigin(card);
        gameMove.set_destination(destination);
        assert destination != null;
        for (int i = destination.get_tableau().size() - 1; i >= 0; i--) {
            gameMove.addCard(destination.get_tableau().get(i));
            if (destination.get_tableau().get(i).equals(card)) {
                break;
            }
        }
    }

    public void stepBack() {
        _gameMoveManager.stepBack();
    }

    //Getter methods----------------------------------------------

    /**
     * Getter method for the deck
     *
     * @return The deck
     */
    public Deck get_deck() {
        return _deck;
    }

    /**
     * Getter method for the foundationsArray
     *
     * @return The foundationsArray
     */
    public Foundation[] get_foundationsArray() {
        return _foundationsArray;
    }

    /**
     * Getter method for the tableausArray
     *
     * @return The tableausArray
     */
    public Tableau[] get_tableausArray() {
        return _tableausArray;
    }
}
