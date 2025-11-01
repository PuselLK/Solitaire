package service;

import listener.IScoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the score of the game
 */
public final class ScoreManager {

    private static ScoreManager _INSTANCE;
    private int _score;
    private final List<IScoreListener> listeners = new ArrayList<>();

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
        notifyListeners();
        System.out.println("Score increased by " + i);
    }

    public void decreaseScore(int i) {
        if (_score - i < 0) {
            _score = 0;
        } else {
            _score -= i;
        }
        notifyListeners();
        System.out.println("Score decreased by " + i);
    }

    public void resetScore() {
        _score = 0;
        notifyListeners();
        System.out.println("Score reset");
    }

    public void addListener(IScoreListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IScoreListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (IScoreListener listener : listeners) {
            listener.onScoreChange(_score);
        }
    }

}
