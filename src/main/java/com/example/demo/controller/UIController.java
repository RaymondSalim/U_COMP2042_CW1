package com.example.demo.controller;

import com.example.demo.events.GameEventListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class UIController implements GameEventListener {
    private final Stage stage;

    public UIController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onGamePaused() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Paused");
        alert.setContentText("The game is paused. Press resume to continue.");
        alert.show();
    }

    @Override
    public void onGameResumed() {
        // Code to hide the pause menu or resume UI.
    }

    @Override
    public void onGameOver() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setContentText("Game over. Thank you for playing!");
        alert.show();
    }
}
