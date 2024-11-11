package com.example.demo.model;

import com.example.demo.model.base.LevelParent;
import com.example.demo.view.Boss;
import com.example.demo.view.base.LevelView;

public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final Boss boss;

    public LevelTwo() {
        super(BACKGROUND_IMAGE_NAME, PLAYER_INITIAL_HEALTH);
        boss = new Boss();
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.LevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    @Override
    public void updateScene() {
        super.updateScene();  // This will check for game over and level completion based on player health and kills

        // Check if the boss is defeated to trigger a win state
        if (boss.isDestroyed()) {
            notifyWin();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        if (getEnemyUnits().isEmpty()) {  // Only add the boss if there are no other enemies
            addEnemyUnit(boss);
        }
    }
}
