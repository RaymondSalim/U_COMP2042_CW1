package com.example.demo.observer;

import com.example.demo.enums.GameState;
import com.example.demo.observer.base.BaseObservable;

public abstract class GameStateObservable extends BaseObservable<GameStateObserver> {
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
