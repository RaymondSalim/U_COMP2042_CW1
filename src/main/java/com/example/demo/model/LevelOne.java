package com.example.demo.model;

import com.example.demo.enums.LevelType;
import com.example.demo.model.base.LevelParent;
import com.example.demo.view.EnemyPlane;
import com.example.demo.view.base.ActiveActorDestructible;
import com.example.demo.view.base.LevelView;

public class LevelOne extends LevelParent {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;

    public LevelOne(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        this.NEXT_LEVEL = LevelType.LEVEL_TWO;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.LevelOne(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkIfGameOver() {
        super.checkIfGameOver();
        if (userHasReachedKillTarget()) {
            goToNextLevel(NEXT_LEVEL);
        }
    }

    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }
}
