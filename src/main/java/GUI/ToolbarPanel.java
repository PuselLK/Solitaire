package GUI;

import Game.Solitaire;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolbarPanel {
    private final Solitaire _solitaire;
    private static JLabel _timeLabel = null;
    private final JButton _stepBackButton;
    private final JButton _restartButton;
    private final JToolBar _toolBar;
    private static Timer _timer = null;
    private static TimerListener _timerListener;

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

        _timerListener = new TimerListener(System.currentTimeMillis(), getTimeLabel());
        _timer = new Timer(1000, _timerListener);
        _timer.start();

        _stepBackButton = new JButton("Step Back");
        _restartButton = new JButton("Restart");
        addMouseListener();
        _toolBar.add(_timeLabel);
        _toolBar.add(_stepBackButton);
        _toolBar.add(_restartButton);
    }

    private void addMouseListener() {
        _stepBackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _solitaire.stepBack();
                GamePanel.renderGameState();
            }
        });
        _restartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _solitaire.restart();
                resetTimer();
                GamePanel.renderGameState();
            }
        });
    }

    public static void resetTimer() {
        _timeLabel.setText("Elapsed time: 00:00:00");
        _timer.restart();
        _timerListener.resetTimer();
    }

    public JToolBar getToolBar() {
        return _toolBar;
    }

    public JLabel getTimeLabel() {
        return _timeLabel;
    }
}
