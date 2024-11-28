package com.example.demo.controller;

import com.example.demo.enums.GameState;
import com.example.demo.observer.GameStateObservable;
import com.example.demo.view.base.NavigationHandler;
import com.example.demo.view.screens.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class UIController extends GameStateObservable {
    private final Stage stage;
    private Scene menuScene;
    private Scene levelSelectScene;
    private Scene settingsScene;

    private LevelComplete levelCompleteOverlay;
    private GameOver gameOverOverlay;
    private PauseMenu pauseOverlay;

    public UIController(Stage stage, NavigationHandler navigationHandler) {
        this.stage = stage;

        // Initialize UI screens
        initializeScreens(navigationHandler);
    }

    private void initializeScreens(NavigationHandler navigationHandler) {
        MenuScreen menuScreen = new MenuScreen(navigationHandler);
        menuScene = menuScreen.createScene();

        LevelSelect levelSelect = new LevelSelect(navigationHandler);
        levelSelectScene = levelSelect.createScene();

        Settings settingsScreen = new Settings(navigationHandler);
        settingsScene = settingsScreen.createScene();
    }

    public void setPauseMenu(PauseMenu pauseMenu) {
        this.pauseOverlay = pauseMenu;
    }

    public void setGameOver(GameOver gameOver) {
        this.gameOverOverlay = gameOver;
    }

    public void setLevelCompleteOverlay(LevelComplete levelCompleteOverlay) {
        this.levelCompleteOverlay = levelCompleteOverlay;
    }

    public void showMenuScreen() {
        stage.setScene(menuScene);
        stage.show();
    }

    public void showLevelSelectScreen() {
        stage.setScene(levelSelectScene);
        stage.show();
    }

    public void showSettingsScreen() {
        stage.setScene(settingsScene);
        stage.show();
    }

    public void addOverlayToLayout(StackPane layout) {
        layout.getChildren().addAll(pauseOverlay.getPane(), gameOverOverlay.getPane(), levelCompleteOverlay.getPane());
    }

    public void showGameOverOverlay() {
        gameOverOverlay.show();
    }

    public void hideGameOverOverlay() {
        gameOverOverlay.hide();
    }

    public void showLevelCompleteOverlay(int score, int starCount) {
        levelCompleteOverlay.setStarCount(starCount);
        levelCompleteOverlay.setScore(score);
        levelCompleteOverlay.show();
    }

    public void hideLevelCompleteOverlay() {
        levelCompleteOverlay.hide();
    }

    public void showGameWinScreen() {
        // TODO!
    }

    public void hideOverlays() {
        hideLevelCompleteOverlay();
        hideGameOverOverlay();
    }

    private StackPane createLevelCompleteOverlay() {
        VBox overlayContent = new VBox(10);
        overlayContent.setAlignment(Pos.CENTER);

        Rectangle background = new Rectangle(300, 200, Color.rgb(0, 0, 0, 0.7));
        Button nextLevelButton = new Button("Next Level");
        Button restartButton = new Button("Restart");

        nextLevelButton.setOnAction(e -> {
            notifyEvent(GameState.LEVEL_ADVANCED);
            hideLevelCompleteOverlay();
            hideGameOverOverlay();
        });

        restartButton.setOnAction(e -> {
            notifyEvent(GameState.LEVEL_RESTARTED);
            hideLevelCompleteOverlay();
            hideGameOverOverlay();
        });

        overlayContent.getChildren().addAll(nextLevelButton, restartButton);
        StackPane overlay = new StackPane(background, overlayContent);
        overlay.setAlignment(Pos.CENTER);
        overlay.setVisible(false);
        return overlay;
    }
}
