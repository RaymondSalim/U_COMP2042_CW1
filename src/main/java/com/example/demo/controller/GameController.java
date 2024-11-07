package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.events.GameEventListener;
import javafx.animation.Timeline;
import javafx.stage.Stage;

public class GameController {
    private final Stage stage;
    private LevelController levelController;
    private Timeline gameTimeline;
    private GameEventListener gameEventListener;

    public GameController(Stage stage) {
        this.stage = stage;
    }

    public void setLevelController(LevelController levelController) {
        this.levelController = levelController;
    }

    public void setGameEventListener(GameEventListener listener) {
        this.gameEventListener = listener;
    }

    public void launchGame() {
        stage.show();
        goToLevel(LevelType.LEVEL_ONE);
    }

    public void goToLevel(LevelType levelType) {
        var level = levelController.loadLevel(levelType, stage.getWidth(), stage.getHeight());
        stage.setScene(level.initializeScene());
        startGame();
    }

    public void startGame() {
        if (gameTimeline == null) {
            gameTimeline = levelController.getGameTimeline();
        }
        gameTimeline.play();
    }

    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
            if (gameEventListener != null) {
                gameEventListener.onGamePaused();
            }
        }
    }

    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.play();
            if (gameEventListener != null) {
                gameEventListener.onGameResumed();
            }
        }
    }

    public void endGame() {
        if (gameTimeline != null) {
            gameTimeline.stop();
        }
        if (gameEventListener != null) {
            gameEventListener.onGameOver();
        }
    }
}
