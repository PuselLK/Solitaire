package Game;

import GUI.ScoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the score of the game
 */
public final class ScoreManager {

    private static ScoreManager _INSTANCE;
    private int _score;
    private final List<ScoreListener> listeners = new ArrayList<>();

    private ScoreManager() {
        _score = 0;
    }

    public static ScoreManager getInstance() {
        if(_INSTANCE == null) {
            _INSTANCE = new ScoreManager();
        }
        return _INSTANCE;
    }

    public int getScore() {
        return _score;
    }

    public void increaseScore(int i) {
        _score += i;
        notifyListners();
        System.out.println("Score increased by " + i);
    }

    public void decreaseScore(int i) {
        if (_score - i < 0) {
            _score = 0;
        } else {
            _score -= i;
        }
        notifyListners();
        System.out.println("Score decreased by " + i);
    }

    public void resetScore() {
        _score = 0;
        notifyListners();
        System.out.println("Score reset");
    }

    public void addListener(ScoreListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ScoreListener listener) {
        listeners.remove(listener);
    }

    public void notifyListners() {
        for (ScoreListener listener : listeners) {
            listener.onScoreChange(_score);
        }
    }

}
