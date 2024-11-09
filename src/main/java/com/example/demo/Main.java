package com.example.demo;

import com.example.demo.controller.GameController;
import com.example.demo.controller.LevelController;
import com.example.demo.controller.UIController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;
    private static final String TITLE = "Sky Battle";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws SecurityException, IllegalArgumentException {
        GameController gameController = new GameController(stage);
        LevelController levelController = new LevelController();
        UIController uiController = new UIController(stage);

        gameController.setLevelController(levelController);
        gameController.setGameEventListener(uiController);

        stage.setTitle(TITLE);
        stage.setResizable(false);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setWidth(SCREEN_WIDTH);

        gameController.launchGame();
    }
}