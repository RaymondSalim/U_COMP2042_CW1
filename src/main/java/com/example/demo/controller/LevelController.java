package com.example.demo.controller;

import com.example.demo.context.AppContext;
import com.example.demo.enums.Direction;
import com.example.demo.enums.LevelType;
import com.example.demo.factory.LevelFactory;
import com.example.demo.model.base.LevelParent;
import com.example.demo.observer.GameStateObserver;
import com.example.demo.view.base.LevelView;
import com.example.demo.view.screens.GameOver;
import com.example.demo.view.screens.PauseMenu;
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
    private PauseMenu pauseMenu;
    private GameOver gameOverOverlay;

    private LevelParent currentLevel;
    private LevelView currentLevelView;
    private Timeline gameTimeline;

    private long lastUpdateTime = 0;

    public LevelController(Stage stage, UIController uiController) {
        this.stage = stage;
        this.uiController = uiController;
        this.uiController.addGameStateObserver(this);

        initializePauseMenu();
        initializeGameOverOverlay();
    }

    @Override
    public void onResumeGame() {
        resumeGame();
    }

    @Override
    public void onGameOver() {
        gameTimeline.pause();
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
        gameTimeline.pause();
        currentLevel.pause();
        uiController.showLevelCompleteOverlay();
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
        uiController.showGameWinScreen();
    }

    private void initializePauseMenu() {
        pauseMenu = new PauseMenu(
                this::resumeGame, // Resume button action
                this::onLevelRestart,
                () -> {
                    gameTimeline.pause();
                    currentLevel.pause();
                    pauseMenu.hide();
                    uiController.showLevelSelectScreen();
                }
        );
        uiController.setPauseMenu(pauseMenu);
    }

    private void initializeGameOverOverlay() {
        gameOverOverlay = new GameOver(
                this::onLevelRestart,
                () -> {
                    gameTimeline.pause();
                    currentLevel.pause();
                    pauseMenu.hide();
                    uiController.showLevelSelectScreen();
                }
        );
        uiController.setGameOver(gameOverOverlay);
    }

    public void goToLevel(LevelType levelType) {
        currentLevel = LevelFactory.createLevel(levelType);
        currentLevel.addGameStateObserver(this);

        AppContext context = AppContext.getInstance();
        context.addGameStateObserver(currentLevel);

        currentLevelView = currentLevel.getLevelView();
        StackPane mainLayout = new StackPane(currentLevelView.getRoot());

        uiController.addOverlayToLayout(mainLayout);

        Scene levelScene = new Scene(mainLayout);
        setKeyHandlers(levelScene);

        stage.setScene(levelScene);
        startGame();
    }

    private void initializeGameTimeline() {
        if (gameTimeline == null) {
            gameTimeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
                long currentTime = System.nanoTime();
                if (lastUpdateTime > 0) {
                    double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
                    updateGame(/*deltaTime*/);
                }
                lastUpdateTime = currentTime;
            }));
            gameTimeline.setCycleCount(Timeline.INDEFINITE);
        }
    }

    private void updateGame() {
        currentLevel.updateScene();
        currentLevel.updateView(currentLevelView);
    }

    public void startGame() {
        initializeGameTimeline();
        gameTimeline.play();
        pauseMenu.hide();
        uiController.hideOverlays();
    }

    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
            currentLevel.pause();
            pauseMenu.show();
        }
    }

    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.play();
            pauseMenu.hide();
            currentLevel.resume();
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
