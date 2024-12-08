package com.example.demo.utils;

import com.example.demo.enums.LevelType;

/**
 * An interface for handling navigation between different screens in the application.
 * <p>
 * Implementing classes provide functionality to navigate to various screens,
 * start levels, and manage game exit.
 * </p>
 */
public interface NavigationHandler {

    /**
     * Navigates to the level selection screen.
     */
    void showLevelSelectScreen();

    /**
     * Navigates to the settings screen.
     */
    void showSettingsScreen();

    /**
     * Navigates to the main menu screen.
     */
    void showMenuScreen();

    /**
     * Navigates to the credits screen.
     */
    void showCreditsScreen();

    /**
     * Exits the game.
     */
    void exitGame();

    /**
     * Starts a specific level.
     *
     * @param levelType the {@link LevelType} of the level to start.
     */
    void startLevel(LevelType levelType);
}