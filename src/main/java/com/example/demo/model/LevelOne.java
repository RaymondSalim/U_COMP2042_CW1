package com.example.demo.model;

import com.example.demo.enums.GameState;
import com.example.demo.enums.LevelType;
import com.example.demo.model.base.LevelParent;
import com.example.demo.view.base.LevelView;

public class LevelOne extends LevelParent {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;

    public LevelOne() {
        super(BACKGROUND_IMAGE_NAME, PLAYER_INITIAL_HEALTH);
        this.NEXT_LEVEL = LevelType.LEVEL_TWO;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.LevelOne(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    @Override
    public void updateScene() {
        super.updateScene();
        if (playerHasReachedKillTarget()) {
            notifyEvent(GameState.LEVEL_COMPLETED);
        }
    }

    private boolean playerHasReachedKillTarget() {
        return getPlayer().getKillCount() >= KILLS_TO_ADVANCE;
    }
}
