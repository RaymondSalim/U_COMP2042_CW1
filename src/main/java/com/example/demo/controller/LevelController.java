package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.factory.LevelFactory;
import com.example.demo.model.base.LevelParent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class LevelController {
    private LevelParent currentLevel;
    private Timeline gameTimeline;

    public LevelParent loadLevel(LevelType levelType, double width, double height) {
        currentLevel = LevelFactory.createLevel(levelType, height, width);
        initializeGameTimeline();
        return currentLevel;
    }

    private void initializeGameTimeline() {
        if (gameTimeline == null) {
            gameTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> currentLevel.updateScene()));
            gameTimeline.setCycleCount(Timeline.INDEFINITE);
        }
    }

    public Timeline getGameTimeline() {
        return gameTimeline;
    }
}
