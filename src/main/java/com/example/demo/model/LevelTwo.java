package com.example.demo.model;

import com.example.demo.model.base.LevelParent;
import com.example.demo.view.Boss;
import com.example.demo.view.base.LevelView;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;
    private com.example.demo.view.LevelTwo levelView;

    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        boss = new Boss();
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.LevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    @Override
    protected void checkIfGameOver() {
        super.checkIfGameOver();
        if (boss.isDestroyed()) {
            winGame();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(boss);
        }
    }
}
