package com.example.demo.model;

import com.example.demo.context.AppContext;
import com.example.demo.enums.LevelType;
import com.example.demo.model.base.LevelParent;
import com.example.demo.view.levels.LevelView;
import com.example.demo.view.objects.EnemyPlane;

public class LevelOne extends LevelParent {
    private static final int PLAYER_INITIAL_HEALTH = 1;
    private static final double STEP_DELAY = 1.5; // Delay duration in seconds
    private int tutorialPhase = 0;
    private boolean phaseComplete = false;
    private double movementTime = 0; // Time spent moving
    private double stepDelayTimer = 0; // Delay timer for phases
    private boolean hasSpawned = false;
    private int missileShot = 0;

    public LevelOne() {
        super(PLAYER_INITIAL_HEALTH);

        super.NEXT_LEVEL = LevelType.LEVEL_TWO;
        super.MAX_ENEMY_SPAWN = 0;
    }

    @Override
    protected int calculateStars(int score) {
        return 3;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new com.example.demo.view.levels.LevelOne(getRoot(), PLAYER_INITIAL_HEALTH);
    }

    @Override
    public void updateScene(double deltaTime) {
        com.example.demo.view.levels.LevelOne levelView = (com.example.demo.view.levels.LevelOne) getLevelView();

        // Handle delay between phases
        if (stepDelayTimer > 0) {
            stepDelayTimer -= deltaTime;
            return; // Wait until delay is over
        }

        switch (tutorialPhase) {
            case 0:
                if (!phaseComplete) {
                    levelView.displayMessage("Press P or ESC to pause the game!");
                    stepDelayTimer = 2.0;
                    phaseComplete = true;
                } else {
                    levelView.deleteMessage();
                    transitionToNextPhase();
                }
                break;
            case 1:
                if (!phaseComplete) {
                    levelView.displayMessage("You can resize the game window however you'd like!");
                    stepDelayTimer = 2.0;
                    phaseComplete = true;
                } else {
                    levelView.deleteMessage();
                    transitionToNextPhase();
                }
                break;
            case 2: // Movement tutorial
                if (!phaseComplete) {
                    levelView.displayMessage("Use UP and DOWN keys to move your plane!");
                    if (userIsMoving(deltaTime)) {
                        movementTime += deltaTime;
                    } else {
                        movementTime = 0; // Reset timer if user stops moving
                    }
                    phaseComplete = movementTime >= 2.0;
                } else {
                    levelView.deleteMessage();
                    transitionToNextPhase();
                }
                break;

            case 3: // Firing tutorial
                if (!phaseComplete) {
                    levelView.displayMessage("Press SPACE to fire your missile! Fire at least four missiles!");
                    phaseComplete = missileShot > 3 && missilesPastHalfway();
                } else {
                    getUserProjectiles().forEach(p -> getRoot().getChildren().remove(p));
                    getUserProjectiles().clear();
                    levelView.deleteMessage();
                    transitionToNextPhase();
                }
                break;

            case 4: // Enemy introduction
                if (!phaseComplete) {
                    levelView.displayMessage("Destroy the enemy plane!");
                    if (!hasSpawned) {
                        spawnEnemy(); // Spawn one enemy for the tutorial
                    }
                    phaseComplete = getUser().getKillCount() == 1;
                    super.updateScene(deltaTime);
                } else {
                    levelView.deleteMessage();
                    transitionToNextPhase(); // Transition to next phase, now to level completion
                }
                break;

            default:
                super.updateScene(deltaTime);
        }
    }

    @Override
    public void userFireProjectile() {
        missileShot++;
        super.userFireProjectile();
    }

    @Override
    public void resetLevel() {
        super.resetLevel();

        missileShot = 0;
        tutorialPhase = 0;
        movementTime = 0.0;
        phaseComplete = false;
        hasSpawned = false;

        com.example.demo.view.levels.LevelOne levelView = (com.example.demo.view.levels.LevelOne) getLevelView();
        levelView.deleteMessage();
    }

    private boolean userIsMoving(double deltaTime) {
        // Detect if the user is moving up or down
        return getUser().getTranslateY() != 0;
    }

    private boolean missilesPastHalfway() {
        // Check if all user projectiles are past halfway across the screen
        return getUserProjectiles().stream()
                .allMatch(projectile -> {
                    double projectileX = projectile.getLayoutX() + projectile.getTranslateX();
                    return projectileX > AppContext.getInstance().getScreenWidthPropertyProperty().divide(2).get();
                });
    }

    private void transitionToNextPhase() {
        tutorialPhase++;
        phaseComplete = false;
        stepDelayTimer = STEP_DELAY; // Add delay before next phase
    }

    private void spawnEnemy() {
        hasSpawned = true;
        // Spawn one enemy plane in the tutorial
        double enemyInitialY = AppContext.getInstance().getScreenHeightPropertyProperty().divide(3).get(); // Position enemy at 1/3 screen height
        EnemyPlane enemy = new EnemyPlane(AppContext.getInstance().getScreenWidthPropertyProperty().get(), enemyInitialY);
        addEnemyUnit(enemy);
    }
}