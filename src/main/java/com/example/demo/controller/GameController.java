package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.view.utils.NavigationHandler;
import javafx.stage.Stage;

public class GameController implements NavigationHandler {
    private final Stage stage;
    private final LevelController levelController;
    private final UIController uiController;

    public GameController(Stage stage) {
        this.stage = stage;
        this.uiController = new UIController(stage, this);
        this.levelController = new LevelController(stage, uiController);
    }

    @Override
    public void showMenuScreen() {
        uiController.showMenuScreen();
    }

    @Override
    public void exitGame() {
        stage.close();
    }

    @Override
    public void showLevelSelectScreen() {
        uiController.showLevelSelectScreen();
    }

    @Override
    public void showSettingsScreen() {
        uiController.showSettingsScreen();
    }

    @Override
    public void showCreditsScreen() {
        uiController.showCreditsScreen();
    }

    @Override
    public void startLevel(LevelType levelType) {
        levelController.goToLevel(levelType);
    }
}
