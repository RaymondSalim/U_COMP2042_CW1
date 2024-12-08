package com.example.demo.model.base;

import com.example.demo.context.AppContext;
import com.example.demo.enums.Direction;
import com.example.demo.enums.GameState;
import com.example.demo.enums.LevelType;
import com.example.demo.model.Player;
import com.example.demo.observer.GameStateObservable;
import com.example.demo.view.entities.ActiveActorDestructible;
import com.example.demo.view.entities.FighterPlane;
import com.example.demo.view.levels.LevelView;
import com.example.demo.view.objects.EnemyPlane;
import com.example.demo.view.objects.UserPlane;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LevelParent extends GameStateObservable {
    protected int MAX_ENEMIES_AT_A_TIME = 5;
    protected int MAX_ENEMY_SPAWN;
    protected double ENEMY_SPAWN_PROBABILITY = .20;
    private final double ENEMY_SPAWN_DELAY = 3.0;
    protected LevelType NEXT_LEVEL;

    private static final double BOTTOM_MARGIN = 150;
    private final SimpleDoubleProperty screenHeight;
    private final SimpleDoubleProperty screenWidth;
    private final SimpleDoubleProperty enemyMaximumYPosition;

    private final Group root;
    private final UserPlane user;

    private final Player player;
    private final List<ActiveActorDestructible> friendlyUnits;
    private final List<ActiveActorDestructible> enemyUnits;
    private final List<ActiveActorDestructible> userProjectiles;
    private final List<ActiveActorDestructible> enemyProjectiles;

    private int score = 0;

    private final LevelView levelView;

    private int spawnedEnemies = 0;
    private double enemySpawnDelay = ENEMY_SPAWN_DELAY;

    public LevelParent(int playerInitialHealth) {
        this.root = new Group();
        root.layoutXProperty().addListener((obs, oldVal, newVal) -> root.setLayoutX(0));
        root.layoutYProperty().addListener((obs, oldVal, newVal) -> root.setLayoutY(0));

        this.friendlyUnits = new ArrayList<>();
        this.enemyUnits = new ArrayList<>();
        this.userProjectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();

        AppContext context = AppContext.getInstance();
        this.screenHeight = new SimpleDoubleProperty();
        this.screenHeight.addListener(e -> repositionOutOfBoundsActors());
        this.screenHeight.bind(context.getScreenHeightPropertyProperty());
        this.screenWidth = new SimpleDoubleProperty();
        this.screenHeight.addListener(e -> repositionOutOfBoundsActors());
        this.screenWidth.bind(context.getScreenWidthPropertyProperty());

        this.enemyMaximumYPosition = new SimpleDoubleProperty();
        this.enemyMaximumYPosition.bind(this.screenHeight.subtract(BOTTOM_MARGIN));
        this.levelView = instantiateLevelView();

        this.player = new Player(playerInitialHealth);
        this.user = new UserPlane(player);

        levelView.initializeUI();
        friendlyUnits.add(user);
        initializeFriendlyUnits();
    }

    public LevelView getLevelView() {
        return levelView;
    }

    public int getStarCount() {
        return this.calculateStars(score);
    }

    public int getScore() {
        return score;
    }

    public List<ActiveActorDestructible> getUserProjectiles() {
        return userProjectiles;
    }

    public UserPlane getUser() {
        return user;
    }

    protected abstract int calculateStars(int score);

    private void repositionOutOfBoundsActors() {
        for (ActiveActorDestructible enemy : enemyUnits) {
            double enemyYPosition = enemy.getLayoutY() + enemy.getTranslateY();
            if (enemyYPosition > enemyMaximumYPosition.get()) {
                enemy.setTranslateY(enemyMaximumYPosition.subtract(enemy.getLayoutY()).get()); // Move back within bounds
            } else if (enemyYPosition < 0) {
                enemy.setTranslateY(-enemy.getLayoutY()); // Move to the top bound
            }
        }

        List<ActiveActorDestructible> projectiles = new ArrayList<>();
        projectiles.addAll(userProjectiles);
        projectiles.addAll(enemyProjectiles);

        for (ActiveActorDestructible projectile : projectiles) {
            double projectileYPosition = projectile.getLayoutY() + projectile.getTranslateY();
            if (projectileYPosition > screenHeight.get() || projectileYPosition < 0) {
                projectile.destroy();
            }
        }
    }

    public void resetLevel() {
        removeAllActors();

        this.friendlyUnits.clear();
        this.enemyUnits.clear();
        this.userProjectiles.clear();
        this.enemyProjectiles.clear();

        this.player.reset();
        this.user.resetPosition();
        this.levelView.reset();

        spawnedEnemies = 0;
        score = 0;
        enemySpawnDelay = ENEMY_SPAWN_DELAY;

        friendlyUnits.add(user);
        initializeFriendlyUnits();
    }

    private void removeAllActors() {
        root.getChildren().removeAll(this.friendlyUnits);
        root.getChildren().removeAll(this.enemyUnits);
        root.getChildren().removeAll(this.userProjectiles);
        root.getChildren().removeAll(this.enemyProjectiles);
    }

    protected abstract LevelView instantiateLevelView();

    protected List<ActiveActorDestructible> getEnemyUnits() {
        return enemyUnits;
    }

    public Group getRoot() {
        return root;
    }

    public LevelType getNextLevel() {
        return NEXT_LEVEL;
    }

    public void updateScene(double deltaTime) {
        updateActors(deltaTime);

        if (enemySpawnDelay > 0) {
            enemySpawnDelay -= deltaTime; // Decrement the delay timer
            return; // Skip enemy spawning and other updates during delay
        } else {
            spawnEnemyUnits();
            generateEnemyFire();
            handleEnemyPenetration();
            handleUserProjectileCollisions();
            handleEnemyProjectileCollisions();
            handleCollisions(userProjectiles, enemyProjectiles);
            handlePlaneCollisions();
            removeAllDestroyedActors();
        }

        updateLevelView();
        boolean playerDestroyed = checkPlayerHealth();
        if (!playerDestroyed) checkLevelCompleted();
    }

    private boolean checkPlayerHealth() {
        if (player.isDestroyed()) {
            notifyEvent(GameState.GAME_OVER);
            return true;
        }
        return false;
    }

    protected void checkLevelCompleted() {
        if (MAX_ENEMY_SPAWN == spawnedEnemies && enemyUnits.isEmpty()) {
            levelComplete();
        }
    }

    protected void levelComplete() {
        if (this.NEXT_LEVEL != null) {
            notifyEvent(GameState.LEVEL_COMPLETED);
        } else {
            notifyEvent(GameState.WIN);
        }
    }

    private void initializeFriendlyUnits() {
        this.root.getChildren().addAll(this.user);
    }

    private void fireProjectile() {
        ActiveActorDestructible projectile = user.fireProjectile();
        root.getChildren().add(projectile);
        userProjectiles.add(projectile);
    }

    private void generateEnemyFire() {
        enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
    }

    private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null) {
            root.getChildren().add(projectile);
            enemyProjectiles.add(projectile);
        }
    }

    protected void spawnEnemyUnits() {
        int enemiesToSpawn = Math.min(MAX_ENEMIES_AT_A_TIME - enemyUnits.size(), MAX_ENEMY_SPAWN - spawnedEnemies);

        for (int i = 0; i < enemiesToSpawn; i++) {
            if (spawnedEnemies < MAX_ENEMY_SPAWN && Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyYPos = Math.random() * enemyMaximumYPosition.get();
                ActiveActorDestructible newEnemy = new EnemyPlane(screenWidth.get(), newEnemyYPos);
                addEnemyUnit(newEnemy);
                spawnedEnemies++;
            }
        }
    }

    private void updateActors(double deltaTime) {
        friendlyUnits.forEach(actor -> actor.updateActor(deltaTime));
        enemyUnits.forEach(actor -> actor.updateActor(deltaTime));
        userProjectiles.forEach(actor -> actor.updateActor(deltaTime));
        enemyProjectiles.forEach(actor -> actor.updateActor(deltaTime));
    }

    private void removeAllDestroyedActors() {
        removeDestroyedActors(friendlyUnits);
        removeDestroyedActors(enemyUnits);
        removeDestroyedActors(userProjectiles);
        removeDestroyedActors(enemyProjectiles);
    }

    private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
        List<ActiveActorDestructible> destroyedActors = actors.stream().filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());
        root.getChildren().removeAll(destroyedActors);
        actors.removeAll(destroyedActors);
    }

    private void handlePlaneCollisions() {
        for (ActiveActorDestructible friendly : friendlyUnits) {
            for (ActiveActorDestructible enemy : enemyUnits) {
                if (!friendly.isDestroyed() && !enemy.isDestroyed()) {
                    if (friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        friendly.takeDamage();
                        enemy.takeDamage();

                        // Check if enemy is destroyed
                        if (enemy.isDestroyed()) {
                            user.incrementKillCount();
                            score += 100;
                        }
                    }
                }
            }
        }
    }

    private void handleUserProjectileCollisions() {
        List<ActiveActorDestructible> projectilesToRemove = new ArrayList<>();
        for (ActiveActorDestructible projectile : userProjectiles) {
            for (ActiveActorDestructible enemy : enemyUnits) {
                // Check if the projectile is out of bounds along the X-axis
                double projectileX = projectile.getLayoutX() + projectile.getTranslateX();
                if (projectileX < 0 || projectileX > screenWidth.get()) {
                    projectilesToRemove.add(projectile);
                    continue; // Skip collision checks for out-of-bounds projectiles
                }

                if (!projectile.isDestroyed() && !enemy.isDestroyed()) {
                    if (projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        projectile.takeDamage();
                        enemy.takeDamage();

                        // Check if enemy is destroyed
                        if (enemy.isDestroyed()) {
                            user.incrementKillCount();
                            score += 100;
                        }
                    }
                }
            }
        }
        userProjectiles.removeAll(projectilesToRemove);
    }

    private void handleEnemyProjectileCollisions() {
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    private void handleCollisions(List<ActiveActorDestructible> actors1,
                                  List<ActiveActorDestructible> actors2) {
        for (ActiveActorDestructible actor : actors2) {
            for (ActiveActorDestructible otherActor : actors1) {
                if (!actor.isDestroyed() && !otherActor.isDestroyed()) {
                    if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
                        actor.takeDamage();
                        otherActor.takeDamage();
                    }
                }
            }
        }
    }

    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth.get();
    }

    private void handleEnemyPenetration() {
        for (ActiveActorDestructible enemy : enemyUnits) {
            if (enemyHasPenetratedDefenses(enemy)) {
                user.takeDamage();
                enemy.destroy();
            }
        }
    }

    private void updateLevelView() {
        levelView.updateHealth(player.getHealth());
    }

    protected void addEnemyUnit(ActiveActorDestructible enemy) {
        enemyUnits.add(enemy);
        root.getChildren().add(enemy);
    }

    public void pause() {
        this.levelView.pause();
    }

    public void resume() {
        this.levelView.resume();
    }

    public void moveUserPlane(Direction direction) {
        switch (direction) {
            case UP -> user.moveUp();
            case DOWN -> user.moveDown();
        }
    }

    public void userFireProjectile() {
        fireProjectile();
    }

    public void stopUserPlane() {
        user.stop();
    }

    protected void updateProgressBar() {
        levelView.updateProgress(
                (double) spawnedEnemies / MAX_ENEMY_SPAWN,
                0.2
        );
    }

    public void updateView(LevelView levelView, double deltaTime) {
        friendlyUnits.forEach(u -> levelView.updateActor(u, deltaTime));
        enemyUnits.forEach(u -> levelView.updateActor(u, deltaTime));
        userProjectiles.forEach(u -> levelView.updateActor(u, deltaTime));
        enemyProjectiles.forEach(u -> levelView.updateActor(u, deltaTime));

        levelView.updateHealth(player.getHealth());
        levelView.updateScore(score);
        updateProgressBar();
    }
}
