package com.example.demo.controller;

import com.example.demo.enums.GameState;
import com.example.demo.observer.GameStateObservable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UIController extends GameStateObservable {
    private final StackPane pauseOverlay;
    private final StackPane gameOverOverlay;
    private final StackPane levelCompleteOverlay;

    public UIController() {
        this.pauseOverlay = createPauseOverlay();
        this.gameOverOverlay = createGameOverOverlay();
        this.levelCompleteOverlay = createLevelCompleteOverlay();
    }

    public void addOverlayToLayout(StackPane layout) {
        layout.getChildren().addAll(pauseOverlay, gameOverOverlay, levelCompleteOverlay);
    }

    public void showPauseOverlay() {
        pauseOverlay.setVisible(true);
    }

    public void hidePauseOverlay() {
        pauseOverlay.setVisible(false);
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

    private StackPane createPauseOverlay() {
        VBox overlayContent = new VBox(10);
        overlayContent.setAlignment(Pos.CENTER);

        Rectangle background = new Rectangle(300, 200, Color.rgb(0, 0, 0, 0.7));
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> {
            notifyEvent(GameState.RESUMED);
            hidePauseOverlay();
        });

        overlayContent.getChildren().addAll(resumeButton);
        StackPane overlay = new StackPane(background, overlayContent);
        overlay.setAlignment(Pos.CENTER);
        overlay.setVisible(false);
        return overlay;
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
