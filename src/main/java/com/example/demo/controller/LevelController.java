package com.example.demo.controller;

import com.example.demo.context.AppContext;
import com.example.demo.enums.LevelType;
import com.example.demo.factory.LevelFactory;
import com.example.demo.model.base.LevelParent;
import com.example.demo.observer.GameStateObserver;
import com.example.demo.view.screens.PauseMenu;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelController implements GameStateObserver {
    private final Stage stage;
    private final UIController uiController;
    private PauseMenu pauseMenu;

    private LevelParent currentLevel;
    private Timeline gameTimeline;

    public LevelController(Stage stage, UIController uiController) {
        this.stage = stage;
        this.uiController = uiController;
        this.uiController.addGameStateObserver(this);

        this.initializePauseMenu();
    }

    @Override
    public void onResumeGame() {
        resumeGame();
    }

    @Override
    public void onGameOver() {
        this.gameTimeline.pause();
        this.uiController.showGameOverOverlay();
    }

    @Override
    public void onLevelRestart() {
        this.currentLevel.resetLevel();
        this.gameTimeline.play();
    }

    @Override
    public void onLevelComplete() {
        this.gameTimeline.pause();
        uiController.showLevelCompleteOverlay();
    }

    @Override
    public void onLevelAdvance() {
        LevelType levelType = currentLevel.getNextLevel();
        if (levelType != null) {
            goToLevel(levelType);
            startGame();
        }
    }

    @Override
    public void onGameWin() {
        this.gameTimeline.stop();
        // TODO! Stop game, show credits
    }

    private void initializePauseMenu() {
        pauseMenu = new PauseMenu(
                this::resumeGame, // Resume button action
                this::onLevelRestart,
                () -> {
                    // Settings button action
                    gameTimeline.pause();
                    pauseMenu.hide();
                    uiController.showLevelSelectScreen();
                }
        );
        uiController.setPauseMenu(pauseMenu);
    }

    public void goToLevel(LevelType levelType) {
        currentLevel = LevelFactory.createLevel(levelType);
        currentLevel.addGameStateObserver(this);

        AppContext context = AppContext.getInstance();
        context.addGameStateObserver(currentLevel);

        StackPane mainLayout = new StackPane(currentLevel.getRoot());

        uiController.addOverlayToLayout(mainLayout);

        Scene levelScene = new Scene(mainLayout);
        setPauseKeyHandler(levelScene);

        stage.setScene(levelScene);
        startGame();
    }

    public void startGame() {
        if (gameTimeline == null) {
            // Initialize game loop with a key frame for each "tick" of the game
            gameTimeline = new Timeline(new KeyFrame(Duration.millis(50), event -> updateGame()));
            gameTimeline.setCycleCount(Timeline.INDEFINITE);
        }
        gameTimeline.play();
        pauseMenu.hide();
        uiController.hideLevelCompleteOverlay();
    }

    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
            pauseMenu.show();
        }
    }

    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.play();
            pauseMenu.hide();
        }
    }

    // Toggle pause/resume based on current game state
    private void togglePause() {
        if (gameTimeline.getStatus() == Timeline.Status.RUNNING) {
            pauseGame();
        } else {
            resumeGame();
        }
    }

    // Set key handler to listen for "P" key to pause/resume the game
    private void setPauseKeyHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P || event.getCode() == KeyCode.ESCAPE) {
                togglePause();
            }
        });
    }

    private void updateGame() {
        currentLevel.updateScene();
    }
}
