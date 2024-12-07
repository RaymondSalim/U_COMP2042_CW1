package com.example.demo;

import com.example.demo.audio.AudioEnum;
import com.example.demo.audio.AudioManager;
import com.example.demo.context.AppContext;
import com.example.demo.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;

    private static final int MIN_SCREEN_WIDTH = 800;
    private static final int MIN_SCREEN_HEIGHT = 600;

    private static final String TITLE = "Sky Battle";

    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws SecurityException, IllegalArgumentException {
        this.stage = stage;

        AppContext context = AppContext.getInstance();
        context.getScreenHeightPropertyProperty().bind(stage.heightProperty());
        context.getScreenWidthPropertyProperty().bind(stage.widthProperty());

        stage.setTitle(TITLE);
        stage.setResizable(true);
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setMinWidth(MIN_SCREEN_WIDTH);
        stage.setMinHeight(MIN_SCREEN_HEIGHT);

        AudioManager audioManager = AudioManager.getInstance();
        audioManager.preloadSounds();
        audioManager.setBackgroundMusic(AudioEnum.BG_MUSIC);
        audioManager.playBackgroundMusic();

//        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
//            context.setScreenSize(context.getScreenHeight(), newValue.intValue());
//        });
//
//        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
//            context.setScreenSize(newValue.intValue(), context.getScreenWidth());
//        });

        GameController gameController = new GameController(stage);
        gameController.showMenuScreen();
    }
}