package com.example.demo;

import com.example.demo.context.AppContext;
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
        AppContext context = AppContext.getInstance();
        context.setScreenSize(SCREEN_HEIGHT, SCREEN_WIDTH);

        GameController gameController = new GameController(stage);

        stage.setTitle(TITLE);
        stage.setResizable(false);
        stage.setHeight(context.getScreenHeight());
        stage.setWidth(context.getScreenWidth());

        gameController.launchGame();
    }
}