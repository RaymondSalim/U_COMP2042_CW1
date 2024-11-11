package com.example.demo.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class UIController {
    private final Stage stage;
    private final StackPane pauseOverlay;  // Encapsulate overlay in UIController
    private final List<GameStateObserver> observers = new ArrayList<>();  // List of observers

    public UIController(Stage stage) {
        this.stage = stage;
        this.pauseOverlay = createPauseOverlay();
    }

    public void addGameStateObserver(GameStateObserver observer) {
        observers.add(observer);
    }

    public void removeGameStateObserver(GameStateObserver observer) {
        observers.remove(observer);
    }

    public void notifyResumeGame() {
        for (GameStateObserver observer : observers) {
            observer.onResumeGame();  // Notify each observer to resume the game
        }
    }

    // Method to show the pause overlay
    public void showPauseOverlay() {
        pauseOverlay.setVisible(true);
    }

    // Method to hide the pause overlay
    public void hidePauseOverlay() {
        pauseOverlay.setVisible(false);
    }

    // Method to show a game over alert
    public void showGameOverAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Game Over! Better luck next time!");
        alert.showAndWait();
    }

    // Helper method to create the pause overlay UI component
    private StackPane createPauseOverlay() {
        VBox overlayContent = new VBox(10);
        overlayContent.setAlignment(Pos.CENTER);

        Rectangle background = new Rectangle(300, 200, Color.rgb(0, 0, 0, 0.7));
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> {
            notifyResumeGame();
            hidePauseOverlay();
        });  // Hide overlay when resume is clicked

        overlayContent.getChildren().addAll(resumeButton);
        StackPane overlay = new StackPane(background, overlayContent);
        overlay.setAlignment(Pos.CENTER);
        overlay.setVisible(false);  // Start with overlay hidden
        return overlay;
    }

    // Method to add overlay to a layout, used by other controllers to integrate the overlay
    public void addOverlayToLayout(StackPane layout) {
        layout.getChildren().add(pauseOverlay);
    }
}
