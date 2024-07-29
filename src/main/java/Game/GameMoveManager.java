package Game;

import java.util.List;
import java.util.Stack;

public class GameMoveManager {
    private final Stack<GameMove> _gameMoves;

    public GameMoveManager() {
        _gameMoves = new Stack<>();
    }

    public void clearGameMoves() {
        _gameMoves.clear();
    }

    public void addGameMove(GameMove gameMove) {
        _gameMoves.push(gameMove);
    }

    /**
     * Steps back one move in the game, if possible.
     */
    public void stepBack() {
        if (_gameMoves.isEmpty()) {
            return;
        }

        GameMove gameMove = _gameMoves.pop();
        List<Card> movedCards = gameMove.getMovedCards();
        CardHolder origin = gameMove.getOrigin();
        CardHolder destination = gameMove.getDestination();

        if (origin instanceof Deck deck) {
            handleDeckStepBack(movedCards, destination, deck);
        } else if (origin instanceof Tableau tableau) {
            handleTableauStepBack(gameMove, movedCards, tableau, destination);
        } else if (origin instanceof Foundation foundation) {
            handleFoundationStepBack(movedCards, foundation, destination);
        }
    }

    /**
     * Handles the step back logic when the origin is a Deck.
     *
     * @param movedCards  The list of cards that were moved.
     * @param destination The destination from where the cards were moved.
     * @param origin      The originating deck from which cards were moved.
     */
    private void handleDeckStepBack(List<Card> movedCards, CardHolder destination, Deck origin) {
        if (destination instanceof Deck) {
            handleDeckStepBack(origin);
            return;
        } else if (destination instanceof Foundation foundation) {
            foundation.pickUpCard();
        } else if (destination instanceof Tableau tableau) {
            tableau.pickUpCard();
        }
        origin.placeCardOnDiscardPile(movedCards.get(0));
    }

    /**
     * Used to go one Step back for the Deck and DiscardPile
     * If the discardPile is empty, all cards from the deck are moved to the discardPile
     * If the discardPile is not empty, the top card is moved back to the deck
     */
    public void handleDeckStepBack(Deck origin) {
        // Last game move was reDealCards() in the deck
        if (origin.isDiscardPileEmpty()) {
            while (!origin.isDeckEmpty()) {
                origin.placeCardOnDiscardPile(origin.drawCardFromDeck());
            }
        } else {
            Card card = origin.removeCardFromDiscardPile();
            card.set_isVisible(false);
            origin.placeCardOnDeck(card);
        }
    }

    /**
     * Handles the step back logic when the origin is a Tableau.
     *
     * @param gameMove    The game move object containing move details.
     * @param movedCards  The list of cards that were moved.
     * @param origin      The originating tableau from which cards were moved.
     * @param destination The destination where the cards were moved.
     */
    private void handleTableauStepBack(GameMove gameMove, List<Card> movedCards, Tableau origin, CardHolder destination) {
        if (destination instanceof Tableau destinationTableau) {
            moveCardsBetweenTableaus(gameMove, movedCards, origin, destinationTableau);
        } else if (destination instanceof Foundation foundation) {
            foundation.pickUpCard();
            handleTableauVisibility(gameMove, origin);
            origin.placeCard(movedCards.get(0));
        }
    }

    /**
     * Handles the step back logic when the origin is a Foundation.
     *
     * @param movedCards  The list of cards that were moved.
     * @param origin      The originating foundation from which cards were moved.
     * @param destination The destination where the cards were moved.
     */
    private void handleFoundationStepBack(List<Card> movedCards, Foundation origin, CardHolder destination) {
        if (destination instanceof Tableau tableau) {
            tableau.pickUpCard();
            origin.placeCard(movedCards.get(0));
        } else if (destination instanceof Foundation foundationDestination) {
            foundationDestination.pickUpCard();
            origin.placeCard(movedCards.get(0));
        }
    }

    /**
     * Ensures the visibility of the cards in the tableau is correctly managed.
     *
     * @param gameMove The game move object containing move details.
     * @param tableau  The tableau whose visibility is being managed.
     */
    private void handleTableauVisibility(GameMove gameMove, Tableau tableau) {
        if (!tableau.isEmpty() && !gameMove.getTableauCardWasVisible()) {
            tableau.peekTableau().set_isVisible(false);
        }
    }

    /**
     * Manages the movement of cards between Tableaus when stepping back.
     *
     * @param gameMove    The game move object containing move details.
     * @param movedCards  The list of cards that were moved.
     * @param origin      The originating tableau from which cards were moved.
     * @param destination The destination tableau to which cards were moved.
     */
    private void moveCardsBetweenTableaus(GameMove gameMove, List<Card> movedCards, Tableau origin, Tableau destination) {
        for (Card ignored : movedCards) {
            destination.pickUpCard();
        }

        handleTableauVisibility(gameMove, origin);

        for (int i = movedCards.size() - 1; i >= 0; i--) {
            origin.placeCard(movedCards.get(i));
        }
    }
}
