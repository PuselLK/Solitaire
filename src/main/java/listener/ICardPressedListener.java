package listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public interface ICardPressedListener {
    void onCardPressed(MouseEvent e, JLabel cardLabel, String source, Container parentContainer);
}
