package listener;

import java.awt.event.MouseEvent;

public interface ICardDraggedListener {
    void onCardDragged(MouseEvent e, String source);
}
