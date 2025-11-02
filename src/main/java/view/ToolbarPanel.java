package view;

import listener.IScoreListener;
import listener.TimerListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ToolbarPanel class that contains the time label, step back button, restart button, and score label.
 */
public class ToolbarPanel implements IScoreListener {
    private static JLabel _timeLabel = null;
    private final JButton _stepBackButton;
    private final JButton _restartButton;
    private final JToolBar _toolBar;
    private static Timer _timer = null;
    private static TimerListener _timerListener;
    private final JLabel _score;

    // Listeners for Controller to register
    private ActionListener _stepBackListener;
    private ActionListener _restartListener;

    /**
     * Creates a new toolbar panel with a time label
     */
    public ToolbarPanel() {
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
        _score = new JLabel("Score: 0", SwingConstants.CENTER);

        _toolBar.add(_timeLabel);
        _toolBar.add(_stepBackButton);
        _toolBar.add(_restartButton);
        _toolBar.add(_score);
    }

    private void addMouseListener() {
        _stepBackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (_stepBackListener != null) {
                    _stepBackListener.actionPerformed(null);
                }
            }
        });
        _restartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (_restartListener != null) {
                    _restartListener.actionPerformed(null);
                }
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

    @Override
    public void onScoreChange(int score) {
        _score.setText("Score: " + score);
    }

    /**
     * Registers listener for step back button clicks.
     */
    public void addStepBackListener(ActionListener listener) {
        _stepBackListener = listener;
    }

    /**
     * Registers listener for restart button clicks.
     */
    public void addRestartListener(ActionListener listener) {
        _restartListener = listener;
    }
}
