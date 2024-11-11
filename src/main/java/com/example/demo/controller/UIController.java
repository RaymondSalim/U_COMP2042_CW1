package com.example.demo.controller;

import com.example.demo.enums.GameState;
import com.example.demo.observer.GameStateObservable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class UIController extends GameStateObservable {
    private final Stage stage;
    private final StackPane pauseOverlay;  // Encapsulate overlay in UIController
    private final StackPane gameOverOverlay;  // Encapsulate overlay in UIController

    public UIController(Stage stage) {
        this.stage = stage;
        this.pauseOverlay = createPauseOverlay();
        this.gameOverOverlay = createGameOverOverlay();
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

    private StackPane createPauseOverlay() {
        VBox overlayContent = new VBox(10);
        overlayContent.setAlignment(Pos.CENTER);

        Rectangle background = new Rectangle(300, 200, Color.rgb(0, 0, 0, 0.7));
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> {
            notifyEvent(GameState.RESUMED);
            hidePauseOverlay();
        });  // Hide overlay when resume is clicked

        overlayContent.getChildren().addAll(resumeButton);
        StackPane overlay = new StackPane(background, overlayContent);
        overlay.setAlignment(Pos.CENTER);
        overlay.setVisible(false);  // Start with overlay hidden
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

    public void addOverlayToLayout(StackPane layout) {
        layout.getChildren().add(pauseOverlay);
        layout.getChildren().add(gameOverOverlay);
    }
}
