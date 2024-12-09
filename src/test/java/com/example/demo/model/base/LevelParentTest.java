package com.example.demo.model.base;

import com.example.demo.JFXTestBase;
import com.example.demo.view.levels.LevelView;
import com.example.demo.view.objects.EnemyPlane;
import javafx.scene.Group;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class LevelParentTest extends JFXTestBase {
    private TestLevelParent levelParent;

    @BeforeEach
    void setUp() {
        levelParent = new TestLevelParent(3); // Initial health of 3 for the player
    }

    @AfterEach
    void tearDown() {
        levelParent.getRoot().getChildren().clear();
    }

    @Test
    void testGetLevelView() {
        assertNotNull(levelParent.getLevelView(), "LevelView should not be null");
    }

    @Test
    void testGetUser() {
        assertNotNull(levelParent.getUser(), "User plane should not be null");
    }

    @Test
    void testGetScore() {
        assertEquals(0, levelParent.getScore(), "Initial score should be 0");
    }

    @Test
    void testResetLevel() {
        levelParent.resetLevel();
        assertEquals(0, levelParent.getScore(), "Score should reset to 0");
        assertTrue(levelParent.getEnemyUnits().isEmpty(), "Enemy units should be cleared");
        assertEquals(3, levelParent.getUser().getHealth(), "Player health should reset");
    }

    @Test
    void testAddEnemyUnit() {
        levelParent.spawnEnemyUnits();

        AtomicBoolean hasEnemy = new AtomicBoolean(false);
        levelParent.getRoot().getChildren().forEach(c -> {
            if (c instanceof EnemyPlane) hasEnemy.set(true);
        });

        assertTrue(hasEnemy.get(), "Enemy plane should be added");
    }

    @Test
    void testUserFireProjectile() {
        int projectilesBefore = levelParent.getUserProjectiles().size();
        levelParent.userFireProjectile();
        int projectilesAfter = levelParent.getUserProjectiles().size();
        assertEquals(projectilesBefore + 1, projectilesAfter, "User should fire a new projectile");
    }

    @Test
    void testUpdateScene() {
        levelParent.updateScene(0.1);
        // Check score or any state changes depending on deltaTime actions
        assertEquals(0, levelParent.getScore(), "Score should not change initially");
    }

    // Add more test cases to validate collisions, health depletion, or specific scenarios as needed.

    // Inner Test Subclass for Testing LevelParent
    private static class TestLevelParent extends LevelParent {
        public TestLevelParent(int playerInitialHealth) {
            super(playerInitialHealth);
            super.ENEMY_SPAWN_PROBABILITY = 1;
            super.MAX_ENEMY_SPAWN = 100;
            super.ENEMY_SPAWN_DELAY = 0;
        }

        @Override
        protected int calculateStars(int score) {
            if (score > 1000) return 3;
            if (score > 500) return 2;
            return 1;
        }

        @Override
        protected LevelView instantiateLevelView() {
            return new LevelView(new Group(), 3) {
                @Override
                protected void initializeBackground(javafx.scene.image.Image... images) {
                    // No-op for testing
                }
            };
        }
    }
}