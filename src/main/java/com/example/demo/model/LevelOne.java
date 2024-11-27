package com.example.demo.model;

import com.example.demo.enums.LevelType;
import com.example.demo.model.base.LevelParent;
import com.example.demo.view.base.LevelView;

public class LevelOne extends LevelParent {
    private static final int PLAYER_INITIAL_HEALTH = 5;

    public LevelOne() {
        super(PLAYER_INITIAL_HEALTH);

        super.NEXT_LEVEL = LevelType.LEVEL_TWO;
        super.KILLS_TO_ADVANCE = 20;
        super.MAX_ENEMY_SPAWN = 20;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.screens.LevelOne(getRoot(), PLAYER_INITIAL_HEALTH);
    }
}
