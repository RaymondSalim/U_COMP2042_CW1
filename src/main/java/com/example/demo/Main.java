package com.example.demo;

import com.example.demo.audio.AudioEnum;
import com.example.demo.audio.AudioManager;
import com.example.demo.context.AppContext;
import com.example.demo.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main entry point of the application.
 * <p>
 * Initializes the application context, sets up the game stage,
 * and starts the main menu.
 * </p>
 */
public class Main extends Application {
    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;

    private static final int MIN_SCREEN_WIDTH = 800;
    private static final int MIN_SCREEN_HEIGHT = 600;

    private static final String TITLE = "Sky Battle";

    private Stage stage;

    /**
     * The main method, serving as the entry point of the application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * The JavaFX start method, which initializes the game window and sets up the application context.
     *
     * @param stage the primary stage for this application.
     * @throws SecurityException        if a security manager exists and denies certain operations.
     * @throws IllegalArgumentException if an illegal argument is passed to the setup.
     */
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

        GameController gameController = new GameController(stage);
        gameController.showMenuScreen();
    }
}