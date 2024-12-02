package com.example.demo.model;

import com.example.demo.model.base.LevelParent;
import com.example.demo.view.base.LevelView;
import com.example.demo.view.objects.Boss;

public class LevelFour extends LevelParent {
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;

    public LevelFour() {
        super(PLAYER_INITIAL_HEALTH);
        boss = new Boss();

        getLevelView().updateProgress(
                1,
                0.2
        );
    }

    @Override
    protected int calculateStars(int score) {
        return 3;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.screens.levels.LevelFour(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkLevelCompleted() {
        if (boss.isDestroyed()) {
            levelComplete();
        }
    }

    @Override
    protected void updateProgressBar() {
        int maxHealth = boss.getMaxHealth();
        boss.getHealthProperty().addListener((observable, oldValue, newValue) -> {
            getLevelView().updateProgress(
                    newValue.doubleValue() / maxHealth,
                    0.2
            );
        });
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getEnemyUnits().isEmpty()) {  // Only add the boss if there are no other enemies
            addEnemyUnit(boss);
            getRoot().getChildren().add(boss.getShieldImage());
        }
    }
}
