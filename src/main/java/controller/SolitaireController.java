package controller;

import listener.ICardDraggedListener;
import listener.ICardPressedListener;
import listener.ICardReleasedListener;
import model.Card;
import model.Deck;
import model.DiscardPile;
import model.Foundation;
import model.GameMove;
import model.ICardHolder;
import model.Tableau;
import view.SolitaireView;
import view.ToolbarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Stack;

import static view.DeckPanel.DECK;
import static view.FoundationPanel.FOUNDATION;
import static view.TableauPanel.TABLEAU;

/**
 * Represents the game of Solitaire.
 * Contains the Deck, the Foundations and the Tableaus.
 */
public final class SolitaireController implements ICardPressedListener, ICardReleasedListener, ICardDraggedListener {
    private static SolitaireController INSTANCE;
    private SolitaireView _view;
    private Deck _deck;
    private DiscardPile _discardPile;
    private Foundation[] _foundationsArray;
    private Tableau[] _tableausArray;

    private JLayeredPane _mainPane;
    private boolean _hasBeenDragged = false;
    private JLabel _draggedLabel = null;
    private Point _initialClickPosition;

    /**
     * Constructor for the Solitaire class.
     */
    private SolitaireController() {
    }

    public static SolitaireController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SolitaireController();
        }
        return INSTANCE;
    }

    /**
     * Restarts the Solitaire game.
     */
    public void restart() {
        GameMoveManager.getInstance().clearGameMoves();
        ScoreManager.getInstance().resetScore();
        initializeGame();
    }

    /**
     * Initializes the controller, model and view.
     */
    public void initialize() {
        initializeGame();
        _view = new SolitaireView();
        _mainPane = _view.getGamePanel().getMainPane();
        wireUpView();
        updateView();
    }

    /**
     * Wires up the view with the controller.
     */
    private void wireUpView() {
        ScoreManager.getInstance().addListener(_view.getToolbarPanel());
        _view.getToolbarPanel().addStepBackListener(e -> {
            stepBack();
            updateView();
        });

        _view.getToolbarPanel().addRestartListener(e -> {
            restart();
            ToolbarPanel.resetTimer();
            updateView();
        });

        _view.addGameOverListener(e -> {
            restart();
            ToolbarPanel.resetTimer();
            updateView();
        });

        _view.getGamePanel().getDeckPanel().addRedrawListener(e -> {
            reDealCards();
            updateView();
        });

        _view.getGamePanel().registerCardListeners(this, this, this);
    }

    /**
     * Updates the view with the current state of the model.
     */
    private void updateView() {
        _view.updateGameState(_deck, _discardPile, _foundationsArray, _tableausArray);

        if (isGameFinished()) {
            _view.showGameOverDialog();
        }
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
        _discardPile = new DiscardPile();

        _foundationsArray = new Foundation[4];
        for (int i = 0; i < _foundationsArray.length; i++) {
            _foundationsArray[i] = new Foundation();
        }

        _tableausArray = new Tableau[7];
        for (int i = 0; i < _tableausArray.length; i++) {
            Stack<Card> tableau = new Stack<>();
            for (int j = 0; j <= i; j++) {
                tableau.push(_deck.pickUpCard());
            }
            _tableausArray[i] = new Tableau(tableau);
            _tableausArray[i].peek().setVisibility(true);
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
        ICardHolder origin = findCardOrigin(card);
        boolean isTopCard = isCardTopCard(card, origin);
        GameMove gameMove = new GameMove(origin, null);

        if (isTopCard && placeCardAlgorithm(card, true)) {
            removeCardIfTop(origin, gameMove);

            gameMove.set_destination(findCardOrigin(card));
            gameMove.addCard(card);
            GameMoveManager.getInstance().addGameMove(gameMove);
            return true;
        } else if (!isTopCard && placeCardAlgorithm(card, false)) {
            removeCardIfNotTop(origin, card, gameMove);

            addAttachedCardsToGameMove(card, gameMove);
            GameMoveManager.getInstance().addGameMove(gameMove);
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
        ICardHolder origin = findCardOrigin(card);
        boolean isTopCard = isCardTopCard(card, origin);
        GameMove gameMove = new GameMove(origin, null);

        if ((isTopCard && targetType.equals(FOUNDATION) && placeCardOnFoundation(card, targetIndex, true)) ||
                (isTopCard && targetType.equals(TABLEAU) && placeCardOnTableau(card, targetIndex))) {
            removeCardIfTop(origin, gameMove);

            gameMove.set_destination(findCardOrigin(card));
            gameMove.addCard(card);
            GameMoveManager.getInstance().addGameMove(gameMove);
            return true;
        } else if ((!isTopCard && targetType.equals(FOUNDATION) && placeCardOnFoundation(card, targetIndex, false)) ||
                (!isTopCard && targetType.equals(TABLEAU) && placeCardOnTableau(card, targetIndex))) {
            removeCardIfNotTop(origin, card, gameMove);

            addAttachedCardsToGameMove(card, gameMove);
            GameMoveManager.getInstance().addGameMove(gameMove);
            return true;
        }
        return false;
    }

    /**
     * Removes the top most card from the given origin.
     * If the origin is a tableau the next card is set visible.
     * Increases Score by 5, if the card was not visible before.
     * Increases Score by 5, if the origin is a discardPile
     *
     * @param origin The card to be removed
     */
    private void removeCardIfTop(ICardHolder origin, GameMove gameMove) {
        origin.pickUpCard();
        if (origin instanceof Tableau) {
            Card topCard = origin.peek();
            if (topCard != null) {
                if (topCard.isVisible()) {
                    gameMove.setTableauCardWasVisible(true);
                } else {
                    ScoreManager.getInstance().increaseScore(5);
                }
                topCard.setVisibility(true);
            }
        }
        if (origin instanceof DiscardPile) {
            ScoreManager.getInstance().increaseScore(5);
        }
        if (origin instanceof Foundation) {
            ScoreManager.getInstance().decreaseScore(5);
        }
    }

    /**
     * Removes the given card and every card on top of it from the given origin.
     * Increases Score by 5, if the card was not visible before.
     *
     * @param origin The card to be removed
     * @param card   The card to be removed
     * @throws RuntimeException if the origin is a Deck or Foundation
     */
    private void removeCardIfNotTop(ICardHolder origin, Card card, GameMove gameMove) {
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
        Card topCard = tableau.peek();
        if (topCard != null) {
            if (topCard.isVisible()) {
                gameMove.setTableauCardWasVisible(true);
            } else {
                ScoreManager.getInstance().increaseScore(5);
            }
            topCard.setVisibility(true);
        }

        // remove the clicked card since it already has been placed
        cardsOnTop.pop();

        // place all cards on top of the clicked card to the tableau excluding the clicked card
        ICardHolder newOrigin = findCardOrigin(card);
        if (!(newOrigin instanceof Tableau)) {
            throw new RuntimeException("Card has been moved to a Foundation or Deck/discardPile even though " +
                    "it was not the top card or the Card was not found.");
        }
        while (!cardsOnTop.isEmpty()) {
            newOrigin.placeCard(cardsOnTop.pop());
        }
    }

    /**
     * Places a card on a foundation.
     * Increases Score by 10, if card is placed on a Foundation
     *
     * @param card The card to be placed
     * @param i    The index of the foundation
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardOnFoundation(Card card, int i, boolean isTopCard) {
        if (_foundationsArray[i].isValidMove(card, isTopCard)) {
            _foundationsArray[i].placeCard(card);
            ScoreManager.getInstance().increaseScore(10);
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
    private boolean isCardTopCard(Card card, ICardHolder origin) {
        return origin.peek().equals(card);
    }

    /**
     * Tries to place a card on a foundation and if not successful, tries to place it on a tableau
     * Does not remove the card from the origin
     * Increases Score by 10, if card is placed on a Foundation
     *
     * @param card      The card to be placed
     * @param isTopCard True if the card is the top card of the origin, false otherwise
     * @return True if the card was placed successfully, false otherwise
     */
    private boolean placeCardAlgorithm(Card card, boolean isTopCard) {
        for (Foundation foundation : _foundationsArray) {
            if (foundation.isValidMove(card, isTopCard)) {
                foundation.placeCard(card);
                ScoreManager.getInstance().increaseScore(10);
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
    private ICardHolder findCardOrigin(Card card) {
        if (_discardPile.peek() != null && _discardPile.peek().equals(card)) {
            return _discardPile;
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
            if (foundation.peek() != null && foundation.peek().equals(card)) {
                return foundation;
            }
        }
        throw new RuntimeException("Card not found in any CardHolder/ does not have an origin.");
    }

    /**
     * The card drawn from the deck if the deck is not empty, null otherwise
     * Also creates a new GameMove object and adds it to the GameMoveManager
     *
     * @return The card drawn from the deck
     */
    public Card drawCardFromDeck() {
        GameMoveManager.getInstance().addGameMove(new GameMove(_deck, _discardPile));

        return _deck.pickUpCard();
    }

    /**
     * Calls the placeCardOnDiscardPile method in the Deck class
     *
     * @see DiscardPile#placeCard(Card)
     */
    public void placeCardOnDiscardPile(Card card) {
        _discardPile.placeCard(card);
    }

    /**
     * Puts all the cards from the discardPile back into the deck and sets their visibility to false
     * Decreases Score by 100
     */
    public void reDealCards() {
        GameMoveManager.getInstance().addGameMove(new GameMove(_discardPile, _deck));
        while (!_discardPile.isEmpty()) {
            _discardPile.peek().setVisibility(false);
            _deck.placeCard(_discardPile.pickUpCard());
        }
        ScoreManager.getInstance().decreaseScore(100);
    }

    /**
     * Checks if the game is finished.
     * The game is finished if all foundations are filled with a King.
     * Increases Score by 100 if the Game is Won
     *
     * @return True if the game is finished, false otherwise
     */
    public boolean isGameFinished() {
        for (Foundation foundation : _foundationsArray) {
            if (foundation.isEmpty() || foundation.peek().getRank() != 13) {
                return false;
            }
        }
        ScoreManager.getInstance().increaseScore(100);
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

    /**
     * Calls the StepBack method in the GameMoveManager
     * Decreases Score by 10
     *
     * @see GameMoveManager#stepBack()
     */
    public void stepBack() {
        GameMoveManager.getInstance().stepBack();
        ScoreManager.getInstance().decreaseScore(10);
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
     * Getter method for the discardPile
     *
     * @return The discardPile
     */
    public DiscardPile get_discardPile() {
        return _discardPile;
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

    @Override
    public void onCardPressed(MouseEvent e, JLabel cardLabel, String source, Container parentContainer) {
        handleMousePressed(e, cardLabel, source, parentContainer);
    }

    /**
     * Handles the mouse pressed event for a card label
     * The Card Label is removed from its parent container and added to the drag layer of the main pane
     *
     * @param e               The mouse event
     * @param cardLabel       The label that was pressed
     * @param source          The source of the card
     * @param parentContainer The container in which the card is placed
     */
    private void handleMousePressed(MouseEvent e, JLabel cardLabel, String source, Container parentContainer) {
        _draggedLabel = cardLabel;
        _initialClickPosition = e.getPoint();

        Point cardLocation = SwingUtilities.convertPoint(parentContainer, cardLabel.getLocation(), _mainPane);
        cardLabel.setBounds(cardLocation.x, cardLocation.y, cardLabel.getWidth(), cardLabel.getHeight());

        if (source.equals(TABLEAU)) {
            handleTableau(parentContainer, cardLabel);
        }

        parentContainer.remove(cardLabel);
        _mainPane.add(_draggedLabel, JLayeredPane.DRAG_LAYER);
    }

    /**
     * Moves all Card Labels above the dragged card to the drag layer of the main pane
     *
     * @param parentContainer The container in which the card is placed
     * @param cardLabel       The label that is being dragged
     */
    private void handleTableau(Container parentContainer, JLabel cardLabel) {
        int layer = getTopmostLayer((JLayeredPane) parentContainer);
        Component[] compArray = ((JLayeredPane) parentContainer).getComponentsInLayer(layer);
        Component comp = compArray[0];

        while (comp != cardLabel) {
            if (comp instanceof JLabel label) {
                Point otherLocation = SwingUtilities.convertPoint(parentContainer, label.getLocation(), _mainPane);
                label.setBounds(otherLocation.x, otherLocation.y, label.getWidth(), label.getHeight());
                parentContainer.remove(label);
                _mainPane.add(label, JLayeredPane.DRAG_LAYER);
            }
            layer--;
            compArray = ((JLayeredPane) parentContainer).getComponentsInLayer(layer);
            comp = compArray[0];
        }
    }

    @Override
    public void onCardReleased(Card card, String source, Container parentContainer) {
        handleMouseReleased(card, source, parentContainer);
    }

    /**
     * Handles the mouse released event for a card label
     * If the card has not been dragged, we handle the click event
     * If the card has been dragged, we find the drop target and place the card
     * After the card has been placed, we reset the drag layer components
     * and rerender the game state
     *
     * @param card            The card that was released
     * @param source          The source of the card
     * @param parentContainer The container in which the card is placed
     */
    private void handleMouseReleased(Card card, String source, Container parentContainer) {
        _mainPane.remove(_draggedLabel);
        boolean cardPlaced = false;

        if (!_hasBeenDragged) {
            handleClick(card, source);
        } else {
            _hasBeenDragged = false;
            Container dropTarget = findDropTarget(_mainPane, _draggedLabel.getLocation());

            if (dropTarget != null) {
                System.out.println(_draggedLabel.getIcon() + " dropped on " + dropTarget);
                cardPlaced = handleDrop(card, dropTarget);
            }

            if (!cardPlaced) {
                System.out.println("Card could not be placed");
            }
        }

        resetDragLayerComponents(parentContainer);
        updateView();
    }

    /**
     * When a Card has been clicked,we either draw a card from the deck or place the card on the discard pile
     * or place the card on the foundation or tableau
     *
     * @param card   The card that was clicked
     * @param source The source of the card
     */
    private void handleClick(Card card, String source) {
        System.out.println("Card clicked: " + card.getSuit() + " " + card.getRank());

        if (source.equals(DECK)) {
            placeCardOnDiscardPile(drawCardFromDeck());
        } else {
            if (!placeCardOnClick(card)) {
                System.out.println("Card could not be placed");
            }
        }
    }

    /**
     * When a Card has been dragged, this method handles the dropping of the card based on the drop target
     * <p>
     * If the drop target is a JPanel, we iterate through the foundation panels to find the correct target
     * and place the card on the foundation
     * <p>
     * If the drop target is a JLayeredPane, we iterate through the tableau panels to find the correct target
     * and place the card on the tableau
     *
     * @param card       The card that is being dropped
     * @param dropTarget The container that the card is being dropped on
     * @return True if the card was placed, false otherwise
     */
    private boolean handleDrop(Card card, Container dropTarget) {
        boolean cardPlaced = false;
        if (dropTarget instanceof JPanel) {
            for (int targetIndex = 0; targetIndex < _view.getGamePanel().getFoundationPanel().getFoundationPanel().getComponentCount(); targetIndex++) {
                if (_view.getGamePanel().getFoundationPanel().getFoundationPanel().getComponent(targetIndex) == dropTarget) {
                    cardPlaced = placeCardOnDrag(card, targetIndex, FOUNDATION);
                    break;
                }
            }
        } else if (dropTarget instanceof JLayeredPane) {
            for (int targetIndex = 0; targetIndex < _view.getGamePanel().getTableauPanel().getTableauPanel().getComponentCount(); targetIndex++) {
                if (_view.getGamePanel().getTableauPanel().getTableauPanel().getComponent(targetIndex) == dropTarget) {
                    cardPlaced = placeCardOnDrag(card, targetIndex, TABLEAU);
                    break;
                }
            }
        }
        return cardPlaced;
    }

    /**
     * Resets _mainPane by removing all components in its DRAG_LAYER
     * and adding them back to their former parent container
     *
     * @param parentContainer The container to add the components back to
     */
    private void resetDragLayerComponents(Container parentContainer) {
        for (Component comp : _mainPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)) {
            if (comp instanceof JLabel) {
                _mainPane.remove(comp);
                parentContainer.add(comp);
            }
        }
    }

    @Override
    public void onCardDragged(MouseEvent e, String source) {
        handleMouseDragged(e, source);
    }

    /**
     * Handles the dragging of a card label by updating its position based on the mouse movement
     * If the card is from the tableau, all other cards above the dragged card are also moved
     *
     * @param e      The mouse event
     * @param source The source of the card
     */
    private void handleMouseDragged(MouseEvent e, String source) {
        if (!Objects.equals(source, DECK)) {
            _hasBeenDragged = true;

            int xMoved = e.getX() - _initialClickPosition.x;
            int yMoved = e.getY() - _initialClickPosition.y;

            int nextX = _draggedLabel.getLocation().x + xMoved;
            int nextY = _draggedLabel.getLocation().y + yMoved;

            _draggedLabel.setLocation(nextX, nextY);

            if (source.equals(TABLEAU)) {
                for (Component comp : _mainPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)) {
                    if (comp instanceof JLabel && comp != _draggedLabel) {
                        int compNextX = comp.getLocation().x + xMoved;
                        int compNextY = comp.getLocation().y + yMoved;
                        comp.setLocation(compNextX, compNextY);
                    }
                }
            }
        }
    }

    /**
     * Recursively finds the drop target for a card label based on its position
     *
     * @param parent The parent container to search in
     * @param point  The point to search for
     * @return The drop target container
     */
    private Container findDropTarget(Container parent, Point point) {
        for (Component component : parent.getComponents()) {
            if (component instanceof Container && component.getBounds().contains(point) && !(component instanceof JLabel)) {
                Point convertedPoint = SwingUtilities.convertPoint(parent, point, component);
                Container innerContainer = findDropTarget((Container) component, convertedPoint);
                return Objects.requireNonNullElseGet(innerContainer, () -> (Container) component);
            }
        }
        return null;
    }

    /**
     * Returns the highest layer of a JLayeredPane
     * Used to determine the layer of the topmost card in a tableau to later iterate downwards to the dragged card
     *
     * @param layeredPane The JLayeredPane to get the highest layer from
     * @return The highest layer
     */
    private int getTopmostLayer(JLayeredPane layeredPane) {
        int highestLayer = 0;
        for (Component comp : layeredPane.getComponents()) {
            int layer = layeredPane.getLayer(comp);
            if (layer > highestLayer) {
                highestLayer = layer;
            }
        }
        return highestLayer;
    }
}
