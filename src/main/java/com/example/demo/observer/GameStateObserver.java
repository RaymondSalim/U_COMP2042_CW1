package com.example.demo.observer;

import com.example.demo.enums.GameState;

import java.util.ArrayList;
import java.util.List;

public abstract class GameStateObserver {
    private final List<GameStateObservable> observers = new ArrayList<>();

    public void addGameStateObserver(GameStateObservable observer) {
        observers.add(observer);
    }

    public void removeGameStateObserver(GameStateObservable observer) {
        observers.remove(observer);
    }

    public void notifyEvent(GameState gameState) {
        for (GameStateObservable observer : observers) {
            switch (gameState) {
                case WIN -> observer.onGameWin();
                case PAUSED -> observer.onPauseGame();
                case RESUMED -> observer.onResumeGame();
                case GAME_OVER -> observer.onGameOver();
                case LEVEL_COMPLETED -> observer.onLevelComplete();
                case LEVEL_RESTARTED -> observer.onLevelRestart();
            }
        }
    }
}
