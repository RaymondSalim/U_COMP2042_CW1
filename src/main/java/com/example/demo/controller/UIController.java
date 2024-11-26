package com.example.demo.controller;

import com.example.demo.enums.GameState;
import com.example.demo.observer.GameStateObservable;
import com.example.demo.view.base.NavigationHandler;
import com.example.demo.view.screens.LevelSelect;
import com.example.demo.view.screens.MenuScreen;
import com.example.demo.view.screens.PauseMenu;
import com.example.demo.view.screens.Settings;
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
    private final StackPane gameOverOverlay;
    private final StackPane levelCompleteOverlay;
    private PauseMenu pauseOverlay;

    public UIController(Stage stage, NavigationHandler navigationHandler) {
        this.stage = stage;

        // Initialize UI screens
        initializeScreens(navigationHandler);

        // Create overlays
        this.gameOverOverlay = createGameOverOverlay();
        this.levelCompleteOverlay = createLevelCompleteOverlay();
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
        layout.getChildren().addAll(pauseOverlay.getPane(), gameOverOverlay, levelCompleteOverlay);
    }

    public void showGameOverOverlay() {
        gameOverOverlay.setVisible(true);
    }

    public void hideGameOverOverlay() {
        gameOverOverlay.setVisible(false);
    }

    public void showLevelCompleteOverlay() {
        levelCompleteOverlay.setVisible(true);
    }

    public void hideLevelCompleteOverlay() {
        levelCompleteOverlay.setVisible(false);
    }

    private StackPane createGameOverOverlay() {
        VBox overlayContent = new VBox(10);
        overlayContent.setAlignment(Pos.CENTER);

        Rectangle background = new Rectangle(300, 200, Color.rgb(0, 0, 0, 0.7));
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            notifyEvent(GameState.LEVEL_RESTARTED);
            hideGameOverOverlay();
        });

        overlayContent.getChildren().addAll(restartButton);
        StackPane overlay = new StackPane(background, overlayContent);
        overlay.setAlignment(Pos.CENTER);
        overlay.setVisible(false);
        return overlay;
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
        });

        restartButton.setOnAction(e -> {
            notifyEvent(GameState.LEVEL_RESTARTED);
            hideLevelCompleteOverlay();
        });

        overlayContent.getChildren().addAll(nextLevelButton, restartButton);
        StackPane overlay = new StackPane(background, overlayContent);
        overlay.setAlignment(Pos.CENTER);
        overlay.setVisible(false);
        return overlay;
    }
}
