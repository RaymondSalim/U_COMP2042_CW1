package com.example.demo.enums;

/**
 * Enum representing the various states of the game.
 */
public enum GameState {
    /**
     * The game is currently paused.
     */
    PAUSED,

    /**
     * The game is actively running and not paused.
     */
    RESUMED,

    /**
     * The game has ended with a game-over condition.
     */
    GAME_OVER,

    /**
     * The current level has been successfully completed.
     */
    LEVEL_COMPLETED,

    /**
     * The game is transitioning to the next level.
     */
    LEVEL_ADVANCED,

    /**
     * The current level has been restarted.
     */
    LEVEL_RESTARTED,

    /**
     * The game has been won.
     */
    WIN
}