import java.util.Stack;

public class Solitaire {
    private final Deck _deck;
    private final Foundation[] _foundationsArray;
    private final Tableau[] _tableausArray;

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

    public boolean placeCardOnDrag(Card card, int targetIndex, String targetType) {
        Object origin = findCardOrigin(card);
        boolean isTopCard = isCardTopCard(card, origin);

        if ((isTopCard && targetType.equals("Foundation") && placeCardOnFoundation(card, targetIndex)) ||
                (isTopCard && targetType.equals("Tableau") && placeCardOnTableau(card, targetIndex))) {
            removeCardIfTop(origin);
            return true;
        } else if ((!isTopCard && targetType.equals("Foundation") && placeCardOnFoundation(card, targetIndex)) ||
                (!isTopCard && targetType.equals("Tableau") && placeCardOnTableau(card, targetIndex))) {
            removeCardIfNotTop(origin, card);
            return true;
        }
        return false;
    }

    private void removeCardIfTop(Object origin) {
        if (origin instanceof Deck) {
            _deck.removeCardFromDiscardPile();
        } else if (origin instanceof Tableau) {
            ((Tableau) origin).pickUpCard();
        } else if (origin instanceof Foundation) {
            ((Foundation) origin).pickUpCard();
        }
    }

    private void removeCardIfNotTop(Object origin, Card card) {
        if (origin instanceof Deck) {
            throw new RuntimeException("Clicked a Card on the Discard Pile that is not the top Card");
        } else if (origin instanceof Foundation) {
            throw new RuntimeException("Clicked a Card on the Foundation that is not the top Card");
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
                throw new RuntimeException("Card has been moved to a Foundation or Deck/discardPile even though " +
                        "it was not the top card or the Card was not found.");
            }
            while (!cardsOnTop.isEmpty()) {
                ((Tableau) newOrigin).placeCard(cardsOnTop.pop());
            }
        }
    }

    private boolean placeCardOnFoundation(Card card, int i) {
        return _foundationsArray[i].placeCard(card);
    }

    private boolean placeCardOnTableau(Card card, int i) {
        return _tableausArray[i].placeCard(card);

    }

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
     * DOES NOT REMOVE THE CARD FROM THE ORIGIN
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

    private Object findCardOrigin(Card card) {
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
     * Draws a card from the deck and puts it on the discardPile.
     * If the deck is empty, all Cards from the discardPile are put back into the Deck
     * and the first card on the deck is drawn.
     */
    public Card drawCardFromDeck() {
        return _deck.drawCardFromDeck();
    }

    public void placeCardOnDiscardPile(Card card) {
        _deck.placeCardOnDiscardPile(card);
    }

    public void reDealCards() {
        _deck.reDealCards();
    }

    public boolean isGameFinished() {
        for (Foundation foundation : _foundationsArray) {
            if (foundation.isEmpty() || foundation.peekFoundation().getValue() != 13) {
                return false;
            }
        }
        return true;
    }

    //Getter methods----------------------------------------------
    public Deck get_deck() {
        return _deck;
    }

    public Foundation[] get_foundationsArray() {
        return _foundationsArray;
    }

    public Tableau[] get_tableausArray() {
        return _tableausArray;
    }
}
