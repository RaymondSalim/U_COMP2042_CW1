package com.example.demo.observer;

/**
 * An interface defining methods for responding to game state changes.
 * <p>
 * Implementations can override the default methods to handle specific game state events.
 * </p>
 */
public interface GameStateObserver {
    default void onResumeGame() {
    }

    /**
     * Called when the game is paused.
     */
    default void onPauseGame() {
    }

    /**
     * Called when the current level is completed.
     */
    default void onLevelComplete() {
    }

    /**
     * Called when the game transitions to the next level.
     */
    default void onLevelAdvance() {
    }

    /**
     * Called when the current level is restarted.
     */
    default void onLevelRestart() {
    }

    /**
     * Called when the game is won.
     */
    default void onGameWin() {
    }

    /**
     * Called when the game is over.
     */
    default void onGameOver() {
    }
}