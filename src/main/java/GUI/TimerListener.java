package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TimerListener class that updates the time label every second
 */
public class TimerListener implements ActionListener {
    private long _startTime;
    private final JLabel _timeLabel;

    public TimerListener(long startTime, JLabel timeLabel) {
        _startTime = startTime;
        _timeLabel = timeLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - _startTime;
        updateDisplay(elapsedTime);
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

    public void resetTimer() {
        _startTime = System.currentTimeMillis();
    }
}
