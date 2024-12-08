package com.example.demo.model;

import com.example.demo.enums.LevelType;
import com.example.demo.model.base.LevelParent;
import com.example.demo.view.levels.LevelView;

public class LevelTwo extends LevelParent {
    private static final int PLAYER_INITIAL_HEALTH = 3;

    public LevelTwo() {
        super(PLAYER_INITIAL_HEALTH);

        super.NEXT_LEVEL = LevelType.LEVEL_THREE;
        super.MAX_ENEMY_SPAWN = 5;
        super.MAX_ENEMIES_AT_A_TIME = 2;
    }

    @Override
    protected int calculateStars(int score) {
        if (score < 200) {
            return 0;
        }

        if (score <= 300) {
            return 1;
        }

        if (score <= 400) {
            return 2;
        }

        return 3;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.levels.LevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
    }
}
