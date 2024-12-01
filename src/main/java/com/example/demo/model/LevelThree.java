package com.example.demo.model;

import com.example.demo.enums.LevelType;
import com.example.demo.model.base.LevelParent;
import com.example.demo.view.base.LevelView;

public class LevelThree extends LevelParent {
    private static final int PLAYER_INITIAL_HEALTH = 5;

    public LevelThree() {
        super(PLAYER_INITIAL_HEALTH);

        super.NEXT_LEVEL = LevelType.LEVEL_FOUR;
        super.MAX_ENEMY_SPAWN = 20;
        super.MAX_ENEMIES_AT_A_TIME = 4;
    }

    @Override
    protected int calculateStars(int score) {
        if (score < 1500) {
            return 0;
        }

        if (score <= 1700) {
            return 1;
        }

        if (score <= 1900) {
            return 2;
        }

        return 3;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.screens.levels.LevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
    }
}
