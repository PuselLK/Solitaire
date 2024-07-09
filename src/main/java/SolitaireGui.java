import javax.swing.*;
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
        _mainPane.add(topPanel, JLayeredPane.DEFAULT_LAYER);

        // Panels for foundation piles
        _foundationPanels = new JPanel(new GridLayout(1, 4));
        _foundationPanels.setPreferredSize(new Dimension((int) (_screenWidth * 2.0 / 4), (int) (_screenHeight * 0.25)));
        topPanel.add(_foundationPanels);
        for (int i = 0; i < 4; i++) {
            JPanel foundationPanel = new JPanel();
            foundationPanel.setBorder(BorderFactory.createTitledBorder(FOUNDATION + " " + (i + 1)));
            _foundationPanels.add(foundationPanel, BorderLayout.CENTER);
        }

        // Panel for the discard pile
        _discardPilePanel = new JPanel();
        _discardPilePanel.setPreferredSize(new Dimension((int) (_screenWidth / 8), (int) (_screenHeight * 0.25)));
        _discardPilePanel.setBorder(BorderFactory.createTitledBorder(DISCARD_PILE));
        topPanel.add(_discardPilePanel, BorderLayout.EAST);

        // Panel for the deck
        _deckPanel = new JPanel();
        _deckPanel.setPreferredSize(new Dimension((int) (_screenWidth / 8), (int) (_screenHeight * 0.25)));
        _deckPanel.setBorder(BorderFactory.createTitledBorder(DECK));
        topPanel.add(_deckPanel, BorderLayout.EAST);

        // Panels for Tableau piles
        _tableauPanels = new JPanel(new GridLayout(1, 7));
        _tableauPanels.setBounds(0, (int) (_screenHeight * 0.25), (int) _screenWidth, (int) (_screenHeight * 0.75));
        _mainPane.add(_tableauPanels, JLayeredPane.DEFAULT_LAYER);

        for (int i = 0; i < 7; i++) {
            JLayeredPane tableauPane = new JLayeredPane();
            tableauPane.setBorder(BorderFactory.createTitledBorder(TABLEAU + " " + (i + 1)));
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
            } else {
                panel.add(new JLabel("Empty"));
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
        if (deck.isDiscardPileEmpty()) {
            _discardPilePanel.add(new JLabel("Empty"));
        } else {
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
        if (card.isVisible()) {
            cardLabel.setIcon(loadCardImage(card));
        } else {
            cardLabel.setIcon(loadOtherImage("src/main/resources/card back/card_back.png"));
        }
        _hasBeenDragged = false;

        cardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                _draggedLabel = cardLabel;
                _initialClick = e.getPoint();
                // Set card absolute position based on its parent's position
                Point cardLocation = SwingUtilities.convertPoint(parentContainer, cardLabel.getLocation(), _mainPane);
                _draggedLabel.setBounds(cardLocation.x, cardLocation.y, cardLabel.getWidth(), cardLabel.getHeight());

                if (source.equals(TABLEAU)) {
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
                parentContainer.remove(cardLabel);
                _mainPane.add(_draggedLabel, JLayeredPane.DRAG_LAYER);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                _mainPane.remove(_draggedLabel);
                boolean cardPlaced = false;

                //Executes if a card has been clicked but not dragged
                if (!_hasBeenDragged) {
                    System.out.println("Card clicked: " + card.getSuit() + " " + card.getValue());

                    if (source.equals(DECK)) {
                        _solitaire.placeCardOnDiscardPile(_solitaire.drawCardFromDeck());
                    } else {
                        if (!_solitaire.placeCardOnClick(card)) {
                            //TODO implement some feedback later
                            System.out.println("Card could not be placed");
                        }
                    }

                    // Executes if the mouse has been dragged
                } else {
                    _hasBeenDragged = false;
                    Container dropTarget = findDropTarget(_mainPane, _draggedLabel.getLocation());

                    if (dropTarget != null) {
                        System.out.println(_draggedLabel.getIcon() + " dropped on " + dropTarget);
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
                    }
                    if (!cardPlaced) {
                        // Drop card back into its original parent container at the calculated location
                        //TODO implement some feedback later
                        System.out.println("Card could not be placed");
                    }
                }
                // Reset the drag layer components
                for (Component comp : _mainPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)) {
                    if (comp instanceof JLabel) {
                        _mainPane.remove(comp);
                        parentContainer.add(comp);
                    }
                }
                renderGameState();
            }
        });

        cardLabel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!Objects.equals(source, DECK)) {
                    _hasBeenDragged = true;
                    // Calculate new position
                    int thisX = _draggedLabel.getLocation().x;
                    int thisY = _draggedLabel.getLocation().y;

                    int xMoved = e.getX() - _initialClick.x;
                    int yMoved = e.getY() - _initialClick.y;

                    int nextX = thisX + xMoved;
                    int nextY = thisY + yMoved;

                    // Move the label within the transparent panel
                    _draggedLabel.setLocation(nextX, nextY);

                    if (source.equals(TABLEAU)) {
                        for (Component comp : _mainPane.getComponentsInLayer(JLayeredPane.DRAG_LAYER)) {
                            if (comp instanceof JLabel && comp != _draggedLabel) {
                                Point compLocation = comp.getLocation();
                                int compNextX = compLocation.x + xMoved;
                                int compNextY = compLocation.y + yMoved;
                                comp.setLocation(compNextX, compNextY);
                            }
                        }
                    }
                }
            }
        });
        return cardLabel;
    }

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

    private JLabel createCardBackLabel() {
        JLabel cardBackLabel = new JLabel();
        cardBackLabel.setIcon(loadOtherImage("src/main/resources/card back/card_back.png"));
        return cardBackLabel;
    }

    private ImageIcon loadOtherImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return new ImageIcon(filePath);
        } else {
            System.err.println("Card image not found: " + filePath);
            return null;
        }
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            long currentTime = System.currentTimeMillis();
            _elapsedTime = currentTime - _startTime;
            updateDisplay(_elapsedTime);
        }
    }

    private void updateDisplay(long elapsedTime) {
        long hours = elapsedTime / 3600000;
        long minutes = (elapsedTime / 60000) % 60;
        long seconds = (elapsedTime / 1000) % 60;
        _timeLabel.setText("Elapsed time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
