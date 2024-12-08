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

    public void goToLevel(LevelType levelType) {
        currentLevel = LevelFactory.createLevel(levelType);
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

        // Calculate FPS and update it in LevelView
        if (deltaTime > 0) {
            double fps = 1.0 / deltaTime;
            currentLevelView.updateFPS(fps);
        }
    }

    public void startGame() {
        AppContext context = AppContext.getInstance();
//        context.getTargetFPS().addListener((obs, oldVal, newVal) -> {
//            initializeGameTimeline(newVal.doubleValue());
//        }); TODO! Check if listener is necessary
        initializeGameTimeline(context.getTargetFPS().doubleValue());
        pauseMenuOverlay.hide();
        uiController.hideOverlays();
    }

    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
            currentLevel.pause();
            pauseMenuOverlay.updateScore(currentLevel.getScore());
            pauseMenuOverlay.show();

            lastUpdateTime = 0;
        }
    }

    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.play();
            pauseMenuOverlay.hide();
            currentLevel.resume();
            lastUpdateTime = System.nanoTime();
        }
    }

    // Toggle pause/resume based on current game state
    private void togglePause() {
        if (gameTimeline != null && gameTimeline.getStatus() == Timeline.Status.RUNNING) {
            pauseGame();
        } else {
            resumeGame();
        }
    }

    private void setKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));
        scene.setOnKeyReleased(event -> handleKeyRelease(event.getCode()));
    }

    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case UP -> currentLevel.moveUserPlane(Direction.UP);
            case DOWN -> currentLevel.moveUserPlane(Direction.DOWN);
            case SPACE -> currentLevel.userFireProjectile();
            case P, ESCAPE -> togglePause();
        }
    }

    private void handleKeyRelease(KeyCode keyCode) {
        if (keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
            currentLevel.stopUserPlane();
        }
    }
}
