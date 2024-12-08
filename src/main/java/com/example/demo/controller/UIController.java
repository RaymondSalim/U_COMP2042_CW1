package com.example.demo.controller;

import com.example.demo.enums.GameState;
import com.example.demo.observer.GameStateObservable;
import com.example.demo.view.overlays.GameOverOverlay;
import com.example.demo.view.overlays.LevelCompleteOverlay;
import com.example.demo.view.overlays.PauseMenuOverlay;
import com.example.demo.view.screens.CreditsScreen;
import com.example.demo.view.screens.LevelSelectScreen;
import com.example.demo.view.screens.MenuScreen;
import com.example.demo.view.screens.SettingsScreen;
import com.example.demo.view.utils.NavigationHandler;
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
    private Scene creditsScene;

    private LevelCompleteOverlay levelCompleteOverlay;
    private GameOverOverlay gameOverOverlay;
    private PauseMenuOverlay pauseOverlay;
    private CreditsScreen creditsScreen;

    public UIController(Stage stage, NavigationHandler navigationHandler) {
        this.stage = stage;

        // Initialize UI screens
        initializeScreens(navigationHandler);
    }

    private void initializeScreens(NavigationHandler navigationHandler) {
        MenuScreen menuScreen = new MenuScreen(navigationHandler);
        menuScene = menuScreen.createScene();

        LevelSelectScreen levelSelectScreen = new LevelSelectScreen(navigationHandler);
        levelSelectScene = levelSelectScreen.createScene();

        SettingsScreen settingsScreen = new SettingsScreen(navigationHandler);
        settingsScene = settingsScreen.createScene();

        creditsScreen = new CreditsScreen(navigationHandler);
        creditsScene = creditsScreen.createScene();
    }

    public void setPauseMenu(PauseMenuOverlay pauseMenuOverlay) {
        this.pauseOverlay = pauseMenuOverlay;
    }

    public void setGameOver(GameOverOverlay gameOverOverlay) {
        this.gameOverOverlay = gameOverOverlay;
    }

    public void setLevelCompleteOverlay(LevelCompleteOverlay levelCompleteOverlay) {
        this.levelCompleteOverlay = levelCompleteOverlay;
    }

    public void showMenuScreen() {
        stage.setOpacity(0.0);
        stage.setScene(menuScene);
        stage.show();
        stage.setOpacity(1.0);
    }

    public void showLevelSelectScreen() {
        stage.setOpacity(0.0);
        stage.setScene(levelSelectScene);
        stage.show();
        stage.setOpacity(1.0);
    }

    public void showSettingsScreen() {
        stage.setOpacity(0.0);
        stage.setScene(settingsScene);
        stage.show();
        stage.setOpacity(1.0);
    }

    public void showCreditsScreen() {
        stage.setOpacity(0.0);
        stage.setScene(creditsScene);
        stage.show();
        stage.setOpacity(1.0);
        creditsScreen.startCredits();
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
