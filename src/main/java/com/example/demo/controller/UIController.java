package com.example.demo.controller;

import com.example.demo.observer.GameStateObservable;
import com.example.demo.utils.NavigationHandler;
import com.example.demo.view.overlays.GameOverOverlay;
import com.example.demo.view.overlays.LevelCompleteOverlay;
import com.example.demo.view.overlays.PauseMenuOverlay;
import com.example.demo.view.screens.CreditsScreen;
import com.example.demo.view.screens.LevelSelectScreen;
import com.example.demo.view.screens.MenuScreen;
import com.example.demo.view.screens.SettingsScreen;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Manages the user interface screens and overlays.
 * <p>
 * Handles transitions between UI screens such as the menu, settings, and level select,
 * and provides methods to show or hide various overlays during gameplay.
 * </p>
 */
public class UIController extends GameStateObservable {
    private final Stage stage;
    private Scene menuScene;
    private Scene levelSelectScene;
    private Scene settingsScene;
    private Scene creditsScene;

    private LevelCompleteOverlay levelCompleteOverlay;
    private GameOverOverlay gameOverOverlay;
    private PauseMenuOverlay pauseOverlay;
    private CreditsScreen creditsScreen;

    /**
     * Creates a new {@code UIController}.
     *
     * @param stage             the primary {@link Stage} used for displaying UI scenes.
     * @param navigationHandler the {@link NavigationHandler} for handling navigation events.
     */
    public UIController(Stage stage, NavigationHandler navigationHandler) {
        this.stage = stage;

        initializeScreens(navigationHandler);
    }

    /**
     * Initializes the screens for menu, level selection, settings, and credits.
     *
     * @param navigationHandler the {@link NavigationHandler} for screen navigation.
     */
    private void initializeScreens(NavigationHandler navigationHandler) {
        MenuScreen menuScreen = new MenuScreen(navigationHandler);
        menuScene = menuScreen.createScene();

        LevelSelectScreen levelSelectScreen = new LevelSelectScreen(navigationHandler);
        levelSelectScene = levelSelectScreen.createScene();

        SettingsScreen settingsScreen = new SettingsScreen(navigationHandler);
        settingsScene = settingsScreen.createScene();

        creditsScreen = new CreditsScreen(navigationHandler);
        creditsScene = creditsScreen.createScene();
    }

    /**
     * Sets the {@link PauseMenuOverlay} to be displayed during gameplay.
     *
     * @param pauseMenuOverlay the {@link PauseMenuOverlay} to set.
     */
    public void setPauseMenu(PauseMenuOverlay pauseMenuOverlay) {
        this.pauseOverlay = pauseMenuOverlay;
    }

    /**
     * Sets the {@link GameOverOverlay} to be displayed during gameplay.
     *
     * @param gameOverOverlay the {@link GameOverOverlay} to set.
     */
    public void setGameOver(GameOverOverlay gameOverOverlay) {
        this.gameOverOverlay = gameOverOverlay;
    }

    /**
     * Sets the {@link LevelCompleteOverlay} to be displayed when a level is completed.
     *
     * @param levelCompleteOverlay the {@link LevelCompleteOverlay} to set.
     */
    public void setLevelCompleteOverlay(LevelCompleteOverlay levelCompleteOverlay) {
        this.levelCompleteOverlay = levelCompleteOverlay;
    }

    /**
     * Displays the main menu screen.
     */
    public void showMenuScreen() {
        stage.setOpacity(0.0);
        stage.setScene(menuScene);
        stage.show();
        stage.setOpacity(1.0);
    }

    /**
     * Displays the level selection screen.
     */
    public void showLevelSelectScreen() {
        stage.setOpacity(0.0);
        stage.setScene(levelSelectScene);
        stage.show();
        stage.setOpacity(1.0);
    }

    /**
     * Displays the settings screen.
     */
    public void showSettingsScreen() {
        stage.setOpacity(0.0);
        stage.setScene(settingsScene);
        stage.show();
        stage.setOpacity(1.0);
    }

    /**
     * Displays the credits screen and starts the credits animation.
     */
    public void showCreditsScreen() {
        stage.setOpacity(0.0);
        stage.setScene(creditsScene);
        stage.show();
        stage.setOpacity(1.0);
        creditsScreen.startCredits();
    }

    /**
     * Adds the overlays (Pause, Game Over, and Level Complete) to the specified layout.
     *
     * @param layout the {@link StackPane} to which overlays are added.
     */
    public void addOverlayToLayout(StackPane layout) {
        layout.getChildren().addAll(
                pauseOverlay.getPane(),
                gameOverOverlay.getPane(),
                levelCompleteOverlay.getPane()
        );
    }

    /**
     * Shows the Game Over overlay.
     */
    public void showGameOverOverlay() {
        gameOverOverlay.show();
    }

    /**
     * Hides the Game Over overlay.
     */
    public void hideGameOverOverlay() {
        gameOverOverlay.hide();
    }

    /**
     * Shows the Level Complete overlay with the provided score and star count.
     *
     * @param score     the player's score.
     * @param starCount the number of stars earned.
     */
    public void showLevelCompleteOverlay(int score, int starCount) {
        levelCompleteOverlay.setStarCount(starCount);
        levelCompleteOverlay.setScore(score);
        levelCompleteOverlay.show();
    }

    /**
     * Hides the Level Complete overlay.
     */
    public void hideLevelCompleteOverlay() {
        levelCompleteOverlay.hide();
    }

    /**
     * Hides all overlays, including Pause, Game Over, and Level Complete.
     */
    public void hideOverlays() {
        hideLevelCompleteOverlay();
        hideGameOverOverlay();
    }
}
