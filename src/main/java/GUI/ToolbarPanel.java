package GUI;

import javax.swing.*;

public class ToolbarPanel {
    private final JLabel _timeLabel;
    private final JToolBar _toolBar;

    /**
     * Creates a new toolbar panel with a time label
     */
    public ToolbarPanel() {
        _toolBar = new JToolBar();
        _toolBar.setFloatable(false);
        _timeLabel = new JLabel("Elapsed time: 00:00:00", SwingConstants.CENTER);
        _timeLabel.setFont(_timeLabel.getFont().deriveFont(24.0f));
        _timeLabel.setBounds(50, 20, 200, 50);
        _toolBar.add(_timeLabel);
    }

    public JToolBar getToolBar() {
        return _toolBar;
    }

    public JLabel getTimeLabel() {
        return _timeLabel;
    }
}
