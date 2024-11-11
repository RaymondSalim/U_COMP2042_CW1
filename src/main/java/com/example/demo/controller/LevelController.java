package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.factory.LevelFactory;
import com.example.demo.model.base.LevelParent;
import com.example.demo.observer.GameStateObserver;
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

    private LevelParent currentLevel;
    private Timeline gameTimeline;

    public LevelController(Stage stage, UIController uiController) {
        this.stage = stage;
        this.uiController = uiController;
        this.uiController.addGameStateObserver(this);
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

    public void goToLevel(LevelType levelType) {
        currentLevel = LevelFactory.createLevel(levelType);
        currentLevel.addGameStateObserver(this);

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
        uiController.hidePauseOverlay();
    }

    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
            uiController.showPauseOverlay();
        }
    }

    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.play();
            uiController.hidePauseOverlay();
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
            if (event.getCode() == KeyCode.P) {
                togglePause();
            }
        });
    }

    private void updateGame() {
        currentLevel.updateScene();
    }
}
