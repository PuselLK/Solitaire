package Game;

import java.util.List;
import java.util.Stack;

/**
 * The GameMoveManager class is responsible for managing game moves.
 * It keeps track of all moves made during the game and allows the user to step back one move at a time.
 */
public class GameMoveManager {
    private final Stack<GameMove> _gameMoves;

    /**
     * inits the GameMoveManager with an empty stack of game moves.
     */
    public GameMoveManager() {
        _gameMoves = new Stack<>();
    }

    /**
     * Clears all game moves.
     */
    public void clearGameMoves() {
        _gameMoves.clear();
    }

    /**
     * Adds a game move to the stack of game moves.
     *
     * @param gameMove The game move to be added.
     */
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

        if (origin instanceof Deck) {
            handleDeckStepBack(origin, destination);
        } else if (origin instanceof DiscardPile) {
            handleDiscardPileStepBack(movedCards, destination, origin);
        } else if (origin instanceof Tableau) {
            handleTableauStepBack(gameMove, movedCards, origin, destination);
        } else if (origin instanceof Foundation) {
            handleFoundationStepBack(movedCards, origin, destination);
        }
    }

    /**
     * Handles the step back logic when the origin is a Deck.
     *
     * @param movedCards        The list of cards that were moved.
     * @param destination       The destination from where the cards were moved.
     * @param discardPileOrigin The originating deck from which cards were moved.
     */
    private void handleDiscardPileStepBack(List<Card> movedCards, CardHolder destination, CardHolder discardPileOrigin) {
        if (!(destination instanceof Deck)) {
            destination.pickUpCard();
            discardPileOrigin.placeCard(movedCards.get(0));
        } else {
            while (!destination.isEmpty()) {
                discardPileOrigin.placeCard(destination.pickUpCard());
            }
        }
    }

    /**
     * Used to go one Step back for the Deck and DiscardPile
     * If the discardPile is empty, all cards from the deck are moved to the discardPile
     * If the discardPile is not empty, the top card is moved back to the deck
     */
    public void handleDeckStepBack(CardHolder origin, CardHolder destination) {
        Card card = destination.pickUpCard();
        card.set_isVisible(false);
        origin.placeCard(card);
    }

    /**
     * Handles the step back logic when the origin is a Tableau.
     *
     * @param gameMove      The game move object containing move details.
     * @param movedCards    The list of cards that were moved.
     * @param tableauOrigin The originating tableau from which cards were moved.
     * @param destination   The destination where the cards were moved.
     */
    private void handleTableauStepBack(GameMove gameMove, List<Card> movedCards, CardHolder tableauOrigin, CardHolder destination) {
        if (destination instanceof Tableau) {
            moveCardsBetweenTableaus(gameMove, movedCards, tableauOrigin, destination);
        } else if (destination instanceof Foundation) {
            destination.pickUpCard();
            handleTableauVisibility(gameMove, tableauOrigin);
            tableauOrigin.placeCard(movedCards.get(0));
        }
    }

    /**
     * Handles the step back logic when the origin is a Foundation.
     *
     * @param movedCards       The list of cards that were moved.
     * @param foundationOrigin The originating foundation from which cards were moved.
     * @param destination      The destination where the cards were moved.
     */
    private void handleFoundationStepBack(List<Card> movedCards, CardHolder foundationOrigin, CardHolder destination) {
        destination.pickUpCard();
        foundationOrigin.placeCard(movedCards.get(0));
    }

    /**
     * Ensures the visibility of the cards in the tableau is correctly managed.
     *
     * @param gameMove The game move object containing move details.
     * @param tableau  The tableau whose visibility is being managed.
     */
    private void handleTableauVisibility(GameMove gameMove, CardHolder tableau) {
        if (!tableau.isEmpty() && !gameMove.getTableauCardWasVisible()) {
            tableau.peek().set_isVisible(false);
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
    private void moveCardsBetweenTableaus(GameMove gameMove, List<Card> movedCards, CardHolder origin, CardHolder destination) {
        for (Card ignored : movedCards) {
            destination.pickUpCard();
        }

        handleTableauVisibility(gameMove, origin);

        for (int i = movedCards.size() - 1; i >= 0; i--) {
            origin.placeCard(movedCards.get(i));
        }
    }
}
