package com.example.demo.model;

import com.example.demo.model.base.LevelParent;
import com.example.demo.view.base.LevelView;
import com.example.demo.view.objects.Boss;

public class LevelTwo extends LevelParent {
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;

    public LevelTwo() {
        super(PLAYER_INITIAL_HEALTH);
        boss = new Boss();
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.screens.LevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkKillCount() {
        if (boss.isDestroyed()) {
            levelComplete();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getEnemyUnits().isEmpty()) {  // Only add the boss if there are no other enemies
            addEnemyUnit(boss);
        }
    }
}
