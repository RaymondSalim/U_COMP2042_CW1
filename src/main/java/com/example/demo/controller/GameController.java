package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.view.LevelSelect;
import com.example.demo.view.MenuScreen;
import com.example.demo.view.Settings;
import com.example.demo.view.base.NavigationHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameController implements NavigationHandler {
    private final Stage stage;
    private final LevelController levelController;
    private final UIController uiController;
    private Scene menuScene;
    private Scene levelSelectScene;
    private Scene settingsScene;

    public GameController(Stage stage) {
        this.stage = stage;
        this.uiController = new UIController();
        this.levelController = new LevelController(stage, uiController);
        initializeScreens();
    }

    private void initializeScreens() {
        MenuScreen menuScreen = new MenuScreen(this);
        menuScene = menuScreen.createScene();

        LevelSelect levelSelect = new LevelSelect(this, levelController);
        levelSelectScene = levelSelect.createScene();

        Settings settingsScreen = new Settings(this);
        settingsScene = settingsScreen.createScene();
    }

    @Override
    public void showMenuScreen() {
        stage.setScene(menuScene);
        stage.show();
    }

    @Override
    public void exitGame() {
        stage.close();
    }

    @Override
    public void showLevelSelectScreen() {
        stage.setScene(levelSelectScene);
        stage.show();
    }

    @Override
    public void showSettingsScreen() {
        stage.setScene(settingsScene);
        stage.show();
    }

    @Override
    public void startLevel(LevelType levelType) {
        levelController.goToLevel(levelType);
    }
}
