package com.example.demo.view.base;

import com.example.demo.enums.LevelType;

public interface NavigationHandler {
    void showLevelSelectScreen();

    void showSettingsScreen();

    void showMenuScreen();

    void showCreditsScreen();

    void exitGame();

    void startLevel(LevelType levelType);
}