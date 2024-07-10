package Game;

import java.util.Stack;

/**
 * Represents the game of Game.Solitaire.
 * Contains the Game.Deck, the Foundations and the Tableaus.
 */
public class Solitaire {
    private final Deck _deck;
    private final Foundation[] _foundationsArray;
    private final Tableau[] _tableausArray;

    /**
     * Constructor for the Game.Solitaire class.
     * Initializes the Game.Deck, the Foundations and the Tableaus.
     * The Game.Deck is initialized with 52 Cards.
     * The Foundations are initialized with 4 empty Stacks.
     * The Tableaus are initialized with 7 Stacks of Cards.
     * The first Game.Tableau has 1 Game.Card, the second 2 Cards, the third 3 Cards and so on.
     * The top Game.Card of each Game.Tableau is set visible.
     */
    public Solitaire() {
        _deck = new Deck();

        _foundationsArray = new Foundation[4];
        for (int i = 0; i < _foundationsArray.length; i++) {
            _foundationsArray[i] = new Foundation();
        }

        _tableausArray = new Tableau[7];
        for (int i = 0; i < _tableausArray.length; i++) {
            Stack<Card> tableau = new Stack<>();
            for (int j = 0; j <= i; j++) {
                tableau.push(_deck.drawCardForInit());
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
        Object origin = findCardOrigin(card);
        boolean isTopCard = isCardTopCard(card, origin);

        if (isTopCard && placeCardAlgorithm(card)) {
            removeCardIfTop(origin);
            return true;
        } else if (!isTopCard && placeCardAlgorithm(card)) {
            removeCardIfNotTop(origin, card);
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
     * @param targetType  The type of the target (Game.Foundation or Game.Tableau)
     * @return True if the card was placed successfully, false otherwise
     */
    public boolean placeCardOnDrag(Card card, int targetIndex, String targetType) {
        Object origin = findCardOrigin(card);
        boolean isTopCard = isCardTopCard(card, origin);

        if ((isTopCard && targetType.equals("Game.Foundation") && placeCardOnFoundation(card, targetIndex)) ||
                (isTopCard && targetType.equals("Game.Tableau") && placeCardOnTableau(card, targetIndex))) {
            removeCardIfTop(origin);
            return true;
        } else if ((!isTopCard && targetType.equals("Game.Foundation") && placeCardOnFoundation(card, targetIndex)) ||
                (!isTopCard && targetType.equals("Game.Tableau") && placeCardOnTableau(card, targetIndex))) {
            removeCardIfNotTop(origin, card);
            return true;
        }
        return false;
    }

    /**
     * Removes the top most card from the given origin.
     *
     * @param origin The card to be removed
     */
    private void removeCardIfTop(Object origin) {
        if (origin instanceof Deck) {
            _deck.removeCardFromDiscardPile();
        } else if (origin instanceof Tableau) {
            ((Tableau) origin).pickUpCard();
        } else if (origin instanceof Foundation) {
            ((Foundation) origin).pickUpCard();
        }
    }

    /**
     * Removes the given card and every card on top of it from the given origin.
     *
     * @param origin The card to be removed
     * @param card   The card to be removed
     * @throws RuntimeException if the origin is a Game.Deck or Game.Foundation
     */
    private void removeCardIfNotTop(Object origin, Card card) {
        if (origin instanceof Deck) {
            throw new RuntimeException("Clicked a Game.Card on the Discard Pile that is not the top Game.Card");
        } else if (origin instanceof Foundation) {
            throw new RuntimeException("Clicked a Game.Card on the Game.Foundation that is not the top Game.Card");
        } else if (origin instanceof Tableau) {
            Stack<Card> cardsOnTop = new Stack<>();

            for (int i = ((Tableau) origin).getTableauSize(); i > 0; i--) {
                // remove all cards on top of the clicked card including the clicked card
                cardsOnTop.push(((Tableau) origin).pickUpCard());
                if (cardsOnTop.peek().equals(card)) {
                    break;
                }
            }
            // remove the clicked card since it already has been placed in the top most else if
            cardsOnTop.pop();
            // place all cards on top of the clicked card to the tableau excluding the clicked card
            Object newOrigin = findCardOrigin(card);
            if (newOrigin instanceof Deck || newOrigin instanceof Foundation || newOrigin == null) {
                throw new RuntimeException("Game.Card has been moved to a Game.Foundation or Game.Deck/discardPile even though " +
                        "it was not the top card or the Game.Card was not found.");
            }
            while (!cardsOnTop.isEmpty()) {
                ((Tableau) newOrigin).placeCard(cardsOnTop.pop());
            }
        }
    }

    /**
     * Places a card on a foundation.
     *
     * @param card The card to be placed
     * @param i    The index of the foundation
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardOnFoundation(Card card, int i) {
        return _foundationsArray[i].placeCard(card);
    }

    /**
     * Places a card on a tableau.
     *
     * @param card The card to be placed
     * @param i    The index of the tableau
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardOnTableau(Card card, int i) {
        return _tableausArray[i].placeCard(card);

    }

    /**
     * Checks if the card is the top card of the origin.
     *
     * @param card   The card to be checked
     * @param origin The origin of the card
     * @return True if the card is the top card of the origin, false otherwise
     */
    private boolean isCardTopCard(Card card, Object origin) {
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
     * @param card The card to be placed
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardAlgorithm(Card card) {
        for (Foundation foundation : _foundationsArray) {
            if (foundation.placeCard(card))
                return true;
        }

        for (Tableau tableau : _tableausArray) {
            if (tableau.placeCard(card))
                return true;
        }
        return false;
    }

    /**
     * Finds the origin of the card by checking the Game.Deck, Tableaus and Foundations.
     *
     * @param card The card to find the origin of
     * @return The origin of the card
     */
    private Object findCardOrigin(Card card) {
        if (_deck.peekDiscardPile() != null && _deck.peekDiscardPile().equals(card)) {
            return _deck;
        }

        // Game.Tableau
        for (Tableau tableau : _tableausArray) {
            for (Card tableauCard : tableau.get_tableau()) {
                if (tableauCard.equals(card)) {
                    return tableau;
                }
            }
        }

        // Game.Foundation
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
