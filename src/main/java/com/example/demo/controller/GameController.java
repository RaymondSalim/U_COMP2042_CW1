package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import javafx.stage.Stage;

public class GameController {
    private final Stage stage;
    private final LevelController levelController;
    private final UIController uiController;  // Reference UIController

    public GameController(Stage stage) {
        this.stage = stage;
        this.uiController = new UIController(stage);
        this.levelController = new LevelController(stage, uiController);  // Pass UIController to LevelController
    }

    public void launchGame() {
        stage.show();
        levelController.goToLevel(LevelType.LEVEL_ONE);
    }

    public void showGameOver() {
        uiController.showGameOverAlert();  // Show game over alert using UIController
    }
}
