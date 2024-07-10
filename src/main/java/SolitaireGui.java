import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;

public class SolitaireGui {
    private Solitaire _solitaire;
    private final JFrame _frame;
    private final JLabel _timeLabel;
    private final long _startTime;
    private long _elapsedTime = 0;
    private final JLayeredPane _mainPane;
    private final JPanel _tableauPanels;
    private final JPanel _foundationPanels;
    private final JPanel _deckPanel;
    private final JPanel _discardPilePanel;
    private double _screenWidth = 1200;
    private double _screenHeight = 800;
    private Point _initialClick;
    private boolean _hasBeenDragged;
    private JLabel _draggedLabel = null;

    private static final String FOUNDATION = "Foundation";
    private static final String DISCARD_PILE = "Discard Pile";
    private static final String DECK = "Deck";
    private static final String TABLEAU = "Tableau";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SolitaireGui::new);
    }

    public SolitaireGui() {
        _solitaire = new Solitaire();

        _frame = new JFrame("Solitaire");
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setLayout(new BorderLayout());
        _frame.setSize((int) _screenWidth, (int) _screenHeight);

        // Toolbar
        JToolBar _toolBar = new JToolBar();
        _toolBar.setFloatable(false);
        _timeLabel = new JLabel("Elapsed time: " + "00:00:00", SwingConstants.CENTER);
        _timeLabel.setFont(_timeLabel.getFont().deriveFont(24.0f));
        _timeLabel.setBounds(50, 20, 200, 50);
        _toolBar.add(_timeLabel);
        _frame.add(_toolBar, BorderLayout.NORTH);

        // Main pane for the game. Used for card dragging
        _mainPane = new JLayeredPane();
        _mainPane.setLayout(null);
        _frame.add(_mainPane, BorderLayout.CENTER);

        // Panel for top row which holds the foundation piles, deck and discard pile
        //TODO change hgab later to be more dynamic
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 80, 0));
        topPanel.setBounds(0, 0, (int) _screenWidth, (int) (_screenHeight * 0.25)); // Ensure top panel coordinates and size
        topPanel.setBackground(new Color(0, 128, 0));
        _mainPane.add(topPanel, JLayeredPane.DEFAULT_LAYER);

        // Panels for foundation piles
        _foundationPanels = new JPanel(new GridLayout(1, 4));
        _foundationPanels.setPreferredSize(new Dimension((int) (_screenWidth * 2.0 / 4), (int) (_screenHeight * 0.25)));

        topPanel.add(_foundationPanels);
        for (int i = 0; i < 4; i++) {
            TitledBorder titledBorder = BorderFactory.createTitledBorder(FOUNDATION + " " + (i + 1));
            titledBorder.setTitleColor(Color.BLACK);
            Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
            titledBorder.setBorder(lineBorder);

            JPanel foundationPanel = new JPanel();
            foundationPanel.setBorder(titledBorder);
            foundationPanel.setBackground(new Color(0, 128, 0));

            _foundationPanels.add(foundationPanel, BorderLayout.CENTER);
        }

        // Panel for the discard pile
        _discardPilePanel = new JPanel();
        _discardPilePanel.setPreferredSize(new Dimension((int) (_screenWidth / 8), (int) (_screenHeight * 0.25)));
        TitledBorder titledBorder = BorderFactory.createTitledBorder(DISCARD_PILE);
        titledBorder.setTitleColor(Color.BLACK);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
        titledBorder.setBorder(lineBorder);
        _discardPilePanel.setBorder(titledBorder);
        _discardPilePanel.setBackground(new Color(0, 128, 0));
        topPanel.add(_discardPilePanel, BorderLayout.EAST);

        // Panel for the deck
        _deckPanel = new JPanel();
        _deckPanel.setPreferredSize(new Dimension((int) (_screenWidth / 8), (int) (_screenHeight * 0.25)));
        titledBorder = BorderFactory.createTitledBorder(DECK);
        titledBorder.setTitleColor(Color.BLACK);
        titledBorder.setBorder(lineBorder);
        _deckPanel.setBorder(titledBorder);
        _deckPanel.setBackground(new Color(0, 128, 0));
        topPanel.add(_deckPanel, BorderLayout.EAST);

        // Panels for Tableau piles
        _tableauPanels = new JPanel(new GridLayout(1, 7));
        _tableauPanels.setBounds(0, (int) (_screenHeight * 0.25), (int) _screenWidth, (int) (_screenHeight * 0.65));
        _tableauPanels.setBackground(new Color(0, 128, 0));
        _mainPane.add(_tableauPanels, JLayeredPane.DEFAULT_LAYER);

        for (int i = 0; i < 7; i++) {
            titledBorder = BorderFactory.createTitledBorder(TABLEAU + " " + (i + 1));
            titledBorder.setTitleColor(Color.BLACK);
            titledBorder.setBorder(lineBorder);

            JLayeredPane tableauPane = new JLayeredPane();
            tableauPane.setBorder(titledBorder);
            tableauPane.setLayout(null);
            _tableauPanels.add(tableauPane);
        }
        Timer _timer = new Timer(1000, new TimerListener());
        _startTime = System.currentTimeMillis() - _elapsedTime;
        _timer.start();

        _frame.setVisible(true);
        renderGameState();

    }

    /**
     * Clears the entire game state and re-renders it based on the current state of the game
     */
    private void renderGameState() {
        clearPanels();

        int cardOffset = 30; // Offset for overlapping cards
        int tableauX_Coordinate = (_tableauPanels.getComponent(1).getWidth() - 100) / 2;

        // Render foundations
        for (int i = 0; i < _solitaire.get_foundationsArray().length; i++) {
            Foundation foundation = _solitaire.get_foundationsArray()[i];
            JPanel panel = (JPanel) _foundationPanels.getComponent(i);
            if (!foundation.isEmpty()) {
                JLabel foundationLabel = createCardLabelClickable(foundation.peekFoundation(), FOUNDATION, panel);
                panel.add(foundationLabel);
            }
        }

        //render deck
        Deck deck = _solitaire.get_deck();
        if (deck.isDeckEmpty()) {
            JLabel redrawLabel = createRedrawLabel();
            _deckPanel.add(redrawLabel);
        } else {
            JLabel deckCardLabel = createCardLabelClickable(deck.peekDeck(), DECK, _deckPanel);
            _deckPanel.add(deckCardLabel);
        }

        // Render discard pile
        if (!deck.isDiscardPileEmpty()) {
            JLabel discardCardLabel = createCardLabelClickable(deck.peekDiscardPile(), DISCARD_PILE, _discardPilePanel);
            _discardPilePanel.add(discardCardLabel);
        }

        // Render tableaux
        for (int i = 0; i < _solitaire.get_tableausArray().length; i++) {
            Tableau tableau = _solitaire.get_tableausArray()[i];

            int layer = 0;
            int yPos = 20;

            // Render Tableau
            for (Card card : tableau.get_tableau()) {
                JLabel tableauCardLabel;
                if (!card.isVisible()) {
                    tableauCardLabel = createCardBackLabel();
                } else {
                    tableauCardLabel = createCardLabelClickable(card, TABLEAU, (Container) _tableauPanels.getComponent(i));
                }

                tableauCardLabel.setBounds(tableauX_Coordinate, yPos, 100, 144); // Adjust size as needed
                JLayeredPane pane = (JLayeredPane) _tableauPanels.getComponent(i);
                pane.add(tableauCardLabel, Integer.valueOf(layer));
                yPos += cardOffset;
                layer++;
            }
        }

        // Refresh the frame
        _frame.revalidate();
        _frame.repaint();
        if (_solitaire.isGameFinished()) {
            JOptionPane.showMessageDialog(_frame, "Congratulations! You have won the game!");
        }
    }

    /**
     * Removes all Components from the foundation, tableau, deck and discard pile panels
     */
    private void clearPanels() {
        for (int i = 0; i < _tableauPanels.getComponentCount(); i++) {
            JLayeredPane tableauPane = (JLayeredPane) _tableauPanels.getComponent(i);
            tableauPane.removeAll();
        }
        for (int i = 0; i < _foundationPanels.getComponentCount(); i++) {
            JPanel foundationPanel = (JPanel) _foundationPanels.getComponent(i);
            foundationPanel.removeAll();
        }
        _deckPanel.removeAll();
        _discardPilePanel.removeAll();
    }

    /**
     * Creates a JLabel for a card that can be clicked and dragged
     *
     * @param card            The card to be displayed
     * @param source          The source of the card
     * @param parentContainer The container in which the card is placed
     * @return The created JLabel
     */
    private JLabel createCardLabelClickable(Card card, String source, Container parentContainer) {
        JLabel cardLabel = new JLabel();
        setCardIcon(cardLabel, card);
        _hasBeenDragged = false;

        addMouseListeners(cardLabel, card, source, parentContainer);

        return cardLabel;
    }

    /**
     * Sets the icon of a card label based on the visibility of the card
     *
     * @param cardLabel The label to set the icon for
     * @param card      The card to get the icon from
     */
    private void setCardIcon(JLabel cardLabel, Card card) {
        if (card.isVisible()) {
            cardLabel.setIcon(loadCardImage(card));
        } else {
            cardLabel.setIcon(loadOtherImage("src/main/resources/card back/card_back.png"));
        }
    }

    /**
     * Adds mouse listeners to a card label
     *
     * @param cardLabel       The label to add the listeners to
     * @param card            The card that the label represents
     * @param source          The source of the card
     * @param parentContainer The container in which the card is placed
     */
    private void addMouseListeners(JLabel cardLabel, Card card, String source, Container parentContainer) {
        cardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e, cardLabel, source, parentContainer);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(card, source, parentContainer);
            }
        });

        cardLabel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e, source);
            }
        });
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
        _initialClick = e.getPoint();

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
                //TODO implement some feedback later
                System.out.println("Card could not be placed");
            }
        }

        resetDragLayerComponents(parentContainer);
        renderGameState();
    }

    /**
     * When a Card has been clicked,we either draw a card from the deck or place the card on the discard pile
     * or place the card on the foundation or tableau
     *
     * @param card   The card that was clicked
     * @param source The source of the card
     */
    private void handleClick(Card card, String source) {
        System.out.println("Card clicked: " + card.getSuit() + " " + card.getValue());

        if (source.equals(DECK)) {
            _solitaire.placeCardOnDiscardPile(_solitaire.drawCardFromDeck());
        } else {
            if (!_solitaire.placeCardOnClick(card)) {
                //TODO implement some feedback later
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
            for (int targetIndex = 0; targetIndex < _foundationPanels.getComponentCount(); targetIndex++) {
                if (_foundationPanels.getComponent(targetIndex) == dropTarget) {
                    cardPlaced = _solitaire.placeCardOnDrag(card, targetIndex, FOUNDATION);
                    break;
                }
            }
        } else if (dropTarget instanceof JLayeredPane) {
            for (int targetIndex = 0; targetIndex < _tableauPanels.getComponentCount(); targetIndex++) {
                if (_tableauPanels.getComponent(targetIndex) == dropTarget) {
                    cardPlaced = _solitaire.placeCardOnDrag(card, targetIndex, TABLEAU);
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

            int xMoved = e.getX() - _initialClick.x;
            int yMoved = e.getY() - _initialClick.y;

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

    /**
     * Loads the image of a card based the image path of the card
     *
     * @param card The card to load the image for
     * @return The ImageIcon of the card
     */
    private ImageIcon loadCardImage(Card card) {
        String fileName = card.get_imagePath();
        File file = new File(fileName);
        if (file.exists()) {
            return new ImageIcon(fileName);
        } else {
            System.err.println("Card image not found: " + fileName);
            return null;
        }
    }

    /**
     * Creates a JLabel for the redraw button
     *
     * @return The created JLabel
     */
    private JLabel createRedrawLabel() {
        JLabel redrawLabel = new JLabel();
        redrawLabel.setIcon(loadOtherImage("src/main/resources/redraw/refresh.png"));
        //TODO improve looks and alignment
        redrawLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _solitaire.reDealCards();
                renderGameState();
            }
        });
        return redrawLabel;
    }

    /**
     * Creates a JLabel for the card back
     *
     * @return The created JLabel
     */
    private JLabel createCardBackLabel() {
        JLabel cardBackLabel = new JLabel();
        cardBackLabel.setIcon(loadOtherImage("src/main/resources/card back/card_back.png"));
        return cardBackLabel;
    }

    /**
     * Loads an image based on the file path
     *
     * @param filePath The file path of the image
     * @return The ImageIcon of the image
     */
    private ImageIcon loadOtherImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return new ImageIcon(filePath);
        } else {
            System.err.println("Card image not found: " + filePath);
            return null;
        }
    }

    /**
     * TimerListener class that updates the time label every second
     */
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            long currentTime = System.currentTimeMillis();
            _elapsedTime = currentTime - _startTime;
            updateDisplay(_elapsedTime);
        }
    }

    /**
     * Updates the time label with the elapsed time
     *
     * @param elapsedTime The elapsed time in milliseconds
     */
    private void updateDisplay(long elapsedTime) {
        long hours = elapsedTime / 3600000;
        long minutes = (elapsedTime / 60000) % 60;
        long seconds = (elapsedTime / 1000) % 60;
        _timeLabel.setText("Elapsed time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
