package com.example.demo.controller;

import com.example.demo.context.AppContext;
import com.example.demo.enums.Direction;
import com.example.demo.enums.LevelType;
import com.example.demo.factory.LevelFactory;
import com.example.demo.model.base.LevelParent;
import com.example.demo.observer.GameStateObserver;
import com.example.demo.view.levels.LevelView;
import com.example.demo.view.overlays.GameOverOverlay;
import com.example.demo.view.overlays.LevelCompleteOverlay;
import com.example.demo.view.overlays.PauseMenuOverlay;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Manages level-specific logic, including level transitions, game updates, and overlays.
 * <p>
 * Implements the {@link GameStateObserver} interface to handle game state events such as
 * pausing, restarting, or completing a level.
 * </p>
 */
public class LevelController implements GameStateObserver {
    private final Stage stage;
    private final UIController uiController;

    private PauseMenuOverlay pauseMenuOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompleteOverlay levelCompleteOverlay;

    private LevelParent currentLevel;
    private LevelView currentLevelView;
    private Timeline gameTimeline;

    private long lastUpdateTime = 0;

    /**
     * Creates a new {@code LevelController}.
     *
     * @param stage        the primary {@link Stage} for displaying levels.
     * @param uiController the {@link UIController} managing UI overlays.
     */
    public LevelController(Stage stage, UIController uiController) {
        this.stage = stage;
        this.uiController = uiController;
        this.uiController.addGameStateObserver(this);

        initializeOverlays();
    }

    @Override
    public void onResumeGame() {
        resumeGame();
    }

    @Override
    public void onGameOver() {
        gameTimeline.stop();
        currentLevel.pause();
        uiController.showGameOverOverlay();
    }

    @Override
    public void onLevelRestart() {
        currentLevel.resetLevel();
        currentLevelView.reset();
        startGame();
    }

    @Override
    public void onLevelComplete() {
        gameTimeline.stop();
        currentLevel.pause();
        int score = currentLevel.getScore();
        int starCount = currentLevel.getStarCount();
        uiController.showLevelCompleteOverlay(score, starCount);
    }

    @Override
    public void onLevelAdvance() {
        LevelType levelType = currentLevel.getNextLevel();
        if (levelType != null) {
            goToLevel(levelType);
            startGame();
        }
    }

    @Override
    public void onGameWin() {
        gameTimeline.stop();
        uiController.showCreditsScreen();
    }

    /**
     * Initializes overlays for pause, game over, and level complete states.
     */
    private void initializeOverlays() {
        Runnable showLevelSelect = () -> {
            gameTimeline.stop();
            currentLevel.resetLevel();
            pauseMenuOverlay.hide();
            uiController.showLevelSelectScreen();
        };

        pauseMenuOverlay = new PauseMenuOverlay(
                this::resumeGame, // Resume button action
                this::onLevelRestart,
                showLevelSelect
        );

        gameOverOverlay = new GameOverOverlay(
                this::onLevelRestart,
                showLevelSelect
        );

        levelCompleteOverlay = new LevelCompleteOverlay(
                showLevelSelect,
                this::onLevelRestart,
                this::onLevelAdvance
        );

        uiController.setPauseMenu(pauseMenuOverlay);
        uiController.setGameOver(gameOverOverlay);
        uiController.setLevelCompleteOverlay(levelCompleteOverlay);
    }

    /**
     * Transitions to the specified level and sets up its scene.
     *
     * @param levelType the {@link LevelType} to load.
     */
    public void goToLevel(LevelType levelType) {
        currentLevel = createLevel(levelType);
        currentLevel.addGameStateObserver(this);

        currentLevelView = currentLevel.getLevelView();
        StackPane mainLayout = new StackPane(currentLevelView.getRoot());

        uiController.addOverlayToLayout(mainLayout);

        Scene levelScene = new Scene(mainLayout);
        setKeyHandlers(levelScene);

        stage.setScene(levelScene);
        startGame();
    }

    private void initializeGameTimeline(double targetFPS) {
        if (gameTimeline != null) {
            gameTimeline.stop();
        }

        double frameDuration = 1000.0 / targetFPS;
        gameTimeline = new Timeline(new KeyFrame(Duration.millis(frameDuration), event -> {
            long currentTime = System.nanoTime();
            if (lastUpdateTime > 0) {
                double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
                updateGame(deltaTime);
            }
            lastUpdateTime = currentTime;
        }));
        gameTimeline.setCycleCount(Timeline.INDEFINITE);
        gameTimeline.play();
    }

    private void updateGame(double deltaTime) {
        currentLevel.updateScene(deltaTime);
        currentLevel.updateView(currentLevelView, deltaTime);

        if (deltaTime > 0) {
            double fps = 1.0 / deltaTime;
            currentLevelView.updateFPS(fps);
        }
    }

    /**
     * Starts the game loop and hides overlays.
     */
    public void startGame() {
        AppContext context = AppContext.getInstance();
        initializeGameTimeline(context.getTargetFPS().doubleValue());
        uiController.hideOverlays();
    }

    /**
     * Pauses the game and displays the pause menu overlay.
     */
    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
            currentLevel.pause();
            pauseMenuOverlay.updateScore(currentLevel.getScore());
            pauseMenuOverlay.show();

            lastUpdateTime = 0;
        }
    }

    /**
     * Resumes the game from a paused state.
     */
    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.play();
            pauseMenuOverlay.hide();
            currentLevel.resume();
            lastUpdateTime = System.nanoTime();
        }
    }

    /**
     * Sets key handlers for the current level's scene.
     *
     * @param scene the {@link Scene} to attach key handlers to.
     */
    private void setKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));
        scene.setOnKeyReleased(event -> handleKeyRelease(event.getCode()));
    }

    /**
     * Handles key press events for level interaction.
     *
     * @param keyCode the pressed {@link KeyCode}.
     */
    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case UP -> currentLevel.moveUserPlane(Direction.UP);
            case DOWN -> currentLevel.moveUserPlane(Direction.DOWN);
            case SPACE -> currentLevel.userFireProjectile();
            case P, ESCAPE -> togglePause();
        }
    }

    /**
     * Handles key release events for level interaction.
     *
     * @param keyCode the released {@link KeyCode}.
     */
    private void handleKeyRelease(KeyCode keyCode) {
        if (keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
            currentLevel.stopUserPlane();
        }
    }

    /**
     * Toggles the game state between paused and resumed.
     */
    private void togglePause() {
        if (gameTimeline != null && gameTimeline.getStatus() == Timeline.Status.RUNNING) {
            pauseGame();
        } else {
            resumeGame();
        }
    }

    protected LevelParent createLevel(LevelType levelType) {
        return LevelFactory.createLevel(levelType);
    }

    protected void setGameTimeline(Timeline gameTimeline) {
        this.gameTimeline = gameTimeline;
    }

    protected void setCurrentLevel(LevelParent currentLevel) {
        this.currentLevel = currentLevel;
    }

    protected void setCurrentLevelView(LevelView currentLevelView) {
        this.currentLevelView = currentLevelView;
    }
}
