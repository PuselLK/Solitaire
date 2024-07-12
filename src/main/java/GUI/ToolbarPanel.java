package GUI;

import Game.Solitaire;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static GUI.GamePanel.renderGameState;

public class ToolbarPanel {
    private final Solitaire _solitaire;
    private final JLabel _timeLabel;
    private final JButton _stepBackButton;
    private final JToolBar _toolBar;

    /**
     * Creates a new toolbar panel with a time label
     */
    public ToolbarPanel(Solitaire solitaire) {
        _solitaire = solitaire;
        _toolBar = new JToolBar();
        _toolBar.setFloatable(false);

        _timeLabel = new JLabel("Elapsed time: 00:00:00", SwingConstants.CENTER);
        _timeLabel.setFont(_timeLabel.getFont().deriveFont(24.0f));
        _timeLabel.setBounds(50, 20, 200, 50);

        _stepBackButton = new JButton("Step Back");
        addMouseListener();
        _toolBar.add(_timeLabel);
        _toolBar.add(_stepBackButton);
    }

    private void addMouseListener() {
        _stepBackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _solitaire.stepBack();
                renderGameState();
            }
        });
    }

    public JToolBar getToolBar() {
        return _toolBar;
    }

    public JLabel getTimeLabel() {
        return _timeLabel;
    }

    public JButton getStepBackButton() {
        return _stepBackButton;
    }
}
