package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import javafx.stage.Stage;

public class GameController {
    private final Stage stage;
    private final LevelController levelController;
    private final UIController uiController;

    public GameController(Stage stage) {
        this.stage = stage;
        this.uiController = new UIController(stage);
        this.levelController = new LevelController(stage, uiController);
    }

    public void goToLevel(LevelType levelType) {
        stage.show();
        levelController.goToLevel(levelType);
    }
}
