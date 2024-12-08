package com.example.demo.observer;

import com.example.demo.enums.GameState;
import com.example.demo.observer.base.BaseObservable;

/**
 * An observable class that notifies {@link GameStateObserver}s about game state events.
 */
public abstract class GameStateObservable extends BaseObservable<GameStateObserver> {

    /**
     * Notifies all registered observers of a specific game state event.
     *
     * @param gameState the {@link GameState} event to notify observers about.
     */
    public void notifyEvent(GameState gameState) {
        for (GameStateObserver observer : observers) {
            switch (gameState) {
                case WIN -> observer.onGameWin();
                case PAUSED -> observer.onPauseGame();
                case RESUMED -> observer.onResumeGame();
                case GAME_OVER -> observer.onGameOver();
                case LEVEL_COMPLETED -> observer.onLevelComplete();
                case LEVEL_ADVANCED -> observer.onLevelAdvance();
                case LEVEL_RESTARTED -> observer.onLevelRestart();
            }
        }
    }
}
