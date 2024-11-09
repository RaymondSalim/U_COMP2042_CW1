package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.events.GameEventListener;
import com.example.demo.model.base.LevelParent;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameController {
    private final Stage stage;
    private LevelController levelController;
    private Timeline gameTimeline;
    private GameEventListener gameEventListener;

    private boolean isPaused;
    private final StackPane mainLayout = new StackPane();
    private final StackPane pauseOverlay = createPauseOverlay();

    public GameController(Stage stage) {
        this.stage = stage;
        mainLayout.getChildren().add(pauseOverlay);
        pauseOverlay.setVisible(false); // Hide initially
    }

    public void setLevelController(LevelController levelController) {
        this.levelController = levelController;
    }

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }

    public void launchGame() {
        stage.show();
        goToLevel(LevelType.LEVEL_ONE);
    }

    public void goToLevel(LevelType levelType) {
        LevelParent level = levelController.loadLevel(levelType, stage.getWidth(), stage.getHeight());
        Scene levelScene = level.initializeScene();
        setPauseKeyHandler(levelScene);

        // Clear mainLayout, then add both the game's root and the overlay
        mainLayout.getChildren().clear();
        mainLayout.getChildren().addAll(level.getRoot(), pauseOverlay); // Ensure game visuals and overlay are in mainLayout
        levelScene.setRoot(mainLayout); // Set mainLayout as the scene's root

        stage.setScene(levelScene);
        setPauseKeyHandler(levelScene);
        startGame();
    }

    private StackPane createPauseOverlay() {
        VBox overlayContent = new VBox(10);
        overlayContent.setAlignment(Pos.CENTER);

        Rectangle background = new Rectangle(300, 200, Color.rgb(0, 0, 0, 0.7));
        Label pauseLabel = new Label("Game Paused");
        pauseLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white;");

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> togglePause()); // Toggle pause on resume button click

        overlayContent.getChildren().addAll(pauseLabel, resumeButton);
        StackPane overlay = new StackPane(background, overlayContent);
        overlay.setAlignment(Pos.CENTER);

        return overlay;
    }

    private void setPauseKeyHandler(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {
                togglePause();
            }
        });
    }

    public void startGame() {
        if (gameTimeline == null) {
            gameTimeline = levelController.getGameTimeline();
        }
        gameTimeline.play();
    }

    private void togglePause() {
        if (isPaused) {
            resumeGame();
            pauseOverlay.setVisible(false);
        } else {
            pauseGame();
            pauseOverlay.setVisible(true);
        }
        isPaused = !isPaused;
    }

    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
            if (gameEventListener != null) {
                gameEventListener.onGamePaused();
            }
        }
    }

    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.play();
            if (gameEventListener != null) {
                gameEventListener.onGameResumed();
            }
        }
    }

    public void endGame() {
        if (gameTimeline != null) {
            gameTimeline.stop();
        }
        if (gameEventListener != null) {
            gameEventListener.onGameOver();
        }
    }
}
