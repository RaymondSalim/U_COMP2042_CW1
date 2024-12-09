package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.utils.NavigationHandler;
import javafx.stage.Stage;

/**
 * Manages the overall game flow and transitions between different screens.
 * <p>
 * Implements the {@link NavigationHandler} interface to handle navigation requests
 * such as showing the menu, starting levels, or exiting the game.
 * </p>
 */
public class GameController implements NavigationHandler {
    private final Stage stage;
    private final LevelController levelController;
    private final UIController uiController;

    /**
     * Creates a new {@code GameController}.
     *
     * @param stage the primary {@link Stage} used for the application.
     */
    public GameController(Stage stage) {
        this.stage = stage;
        this.uiController = createUIController();
        this.levelController = createLevelController();
    }

    @Override
    public void showMenuScreen() {
        uiController.showMenuScreen();
    }

    @Override
    public void exitGame() {
        stage.close();
    }

    @Override
    public void showLevelSelectScreen() {
        uiController.showLevelSelectScreen();
    }

    @Override
    public void showSettingsScreen() {
        uiController.showSettingsScreen();
    }

    @Override
    public void showCreditsScreen() {
        uiController.showCreditsScreen();
    }

    @Override
    public void startLevel(LevelType levelType) {
        levelController.goToLevel(levelType);
    }

    protected UIController createUIController() {
        return new UIController(stage, this);
    }

    protected LevelController createLevelController() {
        return new LevelController(stage, uiController);
    }
}
