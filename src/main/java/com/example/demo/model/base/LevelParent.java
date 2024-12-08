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

/**
 * Abstract base class for all levels in the game.
 * <p>
 * Handles core level functionality, such as player movement, enemy spawning, collision detection,
 * scoring, and managing game state transitions.
 * </p>
 */
public abstract class LevelParent extends GameStateObservable {
    /**
     * The margin at the bottom of the screen where enemies should not spawn or move beyond.
     */
    private static final double BOTTOM_MARGIN = 150;
    /**
     * The initial delay (in seconds) before enemies can start spawning in the level.
     */
    private final double ENEMY_SPAWN_DELAY = 3.0;
    /**
     * The maximum number of enemies that can be active at the same time during the level.
     */
    protected int MAX_ENEMIES_AT_A_TIME = 5;
    /**
     * The total number of enemies to spawn in the level.
     */
    protected int MAX_ENEMY_SPAWN;
    /**
     * The probability of spawning an enemy during each update cycle.
     */
    protected double ENEMY_SPAWN_PROBABILITY = .20;
    /**
     * The {@link LevelType} of the next level. If null, this is the final level.
     */
    protected LevelType NEXT_LEVEL;

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

    /**
     * Constructs a new {@code LevelParent}.
     *
     * @param playerInitialHealth the initial health of the player.
     */
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

    /**
     * Gets the {@link LevelView} associated with the level.
     *
     * @return the {@link LevelView}.
     */
    public LevelView getLevelView() {
        return levelView;
    }

    /**
     * Gets the number of stars earned based on the score.
     *
     * @return the number of stars.
     */
    public int getStarCount() {
        return this.calculateStars(score);
    }

    /**
     * Gets the player's current score.
     *
     * @return the player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the list of user projectiles.
     *
     * @return a list of {@link ActiveActorDestructible} representing the user's projectiles.
     */
    public List<ActiveActorDestructible> getUserProjectiles() {
        return userProjectiles;
    }

    /**
     * Gets the user's plane.
     *
     * @return the {@link UserPlane}.
     */
    public UserPlane getUser() {
        return user;
    }

    /**
     * Abstract method to calculate the number of stars based on the score.
     *
     * @param score the score achieved by the player.
     * @return the number of stars earned.
     */
    protected abstract int calculateStars(int score);

    /**
     * Abstract method to instantiate the {@link LevelView} for the level.
     *
     * @return the instantiated {@link LevelView}.
     */
    protected abstract LevelView instantiateLevelView();

    /**
     * Ensures that all actors (enemies and projectiles) remain within screen bounds,
     * and destroys any projectiles that move out of bounds.
     */
    private void repositionOutOfBoundsActors() {
        for (ActiveActorDestructible enemy : enemyUnits) {
            double enemyYPosition = enemy.getLayoutY() + enemy.getTranslateY();
            if (enemyYPosition > enemyMaximumYPosition.get()) {
                enemy.setTranslateY(enemyMaximumYPosition.subtract(enemy.getLayoutY()).get());
            } else if (enemyYPosition < 0) {
                enemy.setTranslateY(-enemy.getLayoutY());
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

    /**
     * Resets the level to its initial state, clearing all actors and resetting the player's state.
     */
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

    /**
     * Removes all actors from the level's root node.
     */
    private void removeAllActors() {
        root.getChildren().removeAll(this.friendlyUnits);
        root.getChildren().removeAll(this.enemyUnits);
        root.getChildren().removeAll(this.userProjectiles);
        root.getChildren().removeAll(this.enemyProjectiles);
    }

    /**
     * Gets the list of all enemy units currently active in the level.
     *
     * @return a list of {@link ActiveActorDestructible} representing enemy units.
     */
    protected List<ActiveActorDestructible> getEnemyUnits() {
        return enemyUnits;
    }

    /**
     * Gets the root {@link Group} containing all actors in the level.
     *
     * @return the root node of the level.
     */
    public Group getRoot() {
        return root;
    }

    /**
     * Gets the {@link LevelType} of the next level, or {@code null} if this is the final level.
     *
     * @return the next level's {@link LevelType}.
     */
    public LevelType getNextLevel() {
        return NEXT_LEVEL;
    }

    /**
     * Updates the level's game state, including actors and UI components.
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
    public void updateScene(double deltaTime) {
        updateActors(deltaTime);

        if (enemySpawnDelay > 0) {
            enemySpawnDelay -= deltaTime;
            return;
        }

        spawnEnemyUnits();
        generateEnemyFire();
        handleEnemyPenetration();
        handleUserProjectileCollisions();
        handleEnemyProjectileCollisions();
        handleCollisions(userProjectiles, enemyProjectiles);
        handlePlaneCollisions();
        removeAllDestroyedActors();
        updateLevelView();

        boolean playerDestroyed = checkPlayerHealth();
        if (!playerDestroyed) checkLevelCompleted();
    }

    /**
     * Checks the player's health and triggers a game-over event if health is depleted.
     *
     * @return {@code true} if the player is destroyed, otherwise {@code false}.
     */
    private boolean checkPlayerHealth() {
        if (player.isDestroyed()) {
            notifyEvent(GameState.GAME_OVER);
            return true;
        }
        return false;
    }

    /**
     * Checks if the level is completed and triggers a transition if so.
     */
    protected void checkLevelCompleted() {
        if (MAX_ENEMY_SPAWN == spawnedEnemies && enemyUnits.isEmpty()) {
            levelComplete();
        }
    }

    /**
     * Marks the level as complete and notifies the appropriate game state.
     */
    protected void levelComplete() {
        if (this.NEXT_LEVEL != null) {
            notifyEvent(GameState.LEVEL_COMPLETED);
        } else {
            notifyEvent(GameState.WIN);
        }
    }

    /**
     * Initializes the friendly units, including adding the player's plane to the root group.
     */
    private void initializeFriendlyUnits() {
        this.root.getChildren().addAll(this.user);
    }

    /**
     * Fires a projectile from the user's plane.
     */
    private void fireProjectile() {
        ActiveActorDestructible projectile = user.fireProjectile();
        root.getChildren().add(projectile);
        userProjectiles.add(projectile);
    }

    /**
     * Fires projectiles from each enemy unit, if applicable.
     */
    private void generateEnemyFire() {
        enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
    }

    /**
     * Adds a newly spawned enemy projectile to the root group and tracks it.
     *
     * @param projectile the enemy projectile to spawn.
     */
    private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
        if (projectile != null) {
            root.getChildren().add(projectile);
            enemyProjectiles.add(projectile);
        }
    }

    /**
     * Spawns enemy units according to the level's spawning rules.
     */
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

    /**
     * Updates all actors in the level.
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
    private void updateActors(double deltaTime) {
        friendlyUnits.forEach(actor -> actor.updateActor(deltaTime));
        enemyUnits.forEach(actor -> actor.updateActor(deltaTime));
        userProjectiles.forEach(actor -> actor.updateActor(deltaTime));
        enemyProjectiles.forEach(actor -> actor.updateActor(deltaTime));
    }

    /**
     * Removes all destroyed actors from the level.
     */
    private void removeAllDestroyedActors() {
        removeDestroyedActors(friendlyUnits);
        removeDestroyedActors(enemyUnits);
        removeDestroyedActors(userProjectiles);
        removeDestroyedActors(enemyProjectiles);
    }

    /**
     * Removes destroyed actors from a given list.
     *
     * @param actors the list of actors to process.
     */
    private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
        List<ActiveActorDestructible> destroyedActors = actors.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());
        root.getChildren().removeAll(destroyedActors);
        actors.removeAll(destroyedActors);
    }

    /**
     * Handles plane-to-plane collisions, damaging both planes upon contact.
     */
    private void handlePlaneCollisions() {
        for (ActiveActorDestructible friendly : friendlyUnits) {
            for (ActiveActorDestructible enemy : enemyUnits) {
                if (!friendly.isDestroyed() && !enemy.isDestroyed()) {
                    if (friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        friendly.takeDamage();
                        enemy.takeDamage();

                        if (enemy.isDestroyed()) {
                            user.incrementKillCount();
                            score += 100;
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles collisions between user projectiles and enemies.
     */
    private void handleUserProjectileCollisions() {
        List<ActiveActorDestructible> projectilesToRemove = new ArrayList<>();
        for (ActiveActorDestructible projectile : userProjectiles) {
            for (ActiveActorDestructible enemy : enemyUnits) {
                double projectileX = projectile.getLayoutX() + projectile.getTranslateX();
                if (projectileX < 0 || projectileX > screenWidth.get()) {
                    projectilesToRemove.add(projectile);
                    continue;
                }

                if (!projectile.isDestroyed() && !enemy.isDestroyed()) {
                    if (projectile.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        projectile.takeDamage();
                        enemy.takeDamage();

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

    /**
     * Handles collisions between enemy projectiles and friendly units.
     */
    private void handleEnemyProjectileCollisions() {
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    /**
     * Handles collisions between two lists of actors.
     *
     * @param actors1 the first list of actors.
     * @param actors2 the second list of actors.
     */
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

    /**
     * Handles enemies penetrating the player's defenses, reducing health if they escape.
     */
    private void handleEnemyPenetration() {
        for (ActiveActorDestructible enemy : enemyUnits) {
            if (enemyHasPenetratedDefenses(enemy)) {
                user.takeDamage();
                enemy.destroy();
            }
        }
    }

    /**
     * Checks if an enemy has penetrated the player's defenses.
     *
     * @param enemy the enemy to check.
     * @return {@code true} if the enemy has penetrated, {@code false} otherwise.
     */
    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth.get();
    }

    /**
     * Updates the level view with the player's current health.
     */
    private void updateLevelView() {
        levelView.updateHealth(player.getHealth());
    }

    /**
     * Adds an enemy unit to the level, including adding it to the root group for rendering.
     *
     * @param enemy the {@link ActiveActorDestructible} representing the enemy unit.
     */
    protected void addEnemyUnit(ActiveActorDestructible enemy) {
        enemyUnits.add(enemy);
        root.getChildren().add(enemy);
    }

    /**
     * Pauses the level.
     */
    public void pause() {
        this.levelView.pause();
    }

    /**
     * Resumes the level.
     */
    public void resume() {
        this.levelView.resume();
    }

    /**
     * Moves the user's plane in the specified direction.
     *
     * @param direction the {@link Direction} to move the user's plane.
     */
    public void moveUserPlane(Direction direction) {
        switch (direction) {
            case UP -> user.moveUp();
            case DOWN -> user.moveDown();
        }
    }

    /**
     * Fires a projectile from the user's plane.
     */
    public void userFireProjectile() {
        fireProjectile();
    }

    /**
     * Stops the user's plane from moving.
     */
    public void stopUserPlane() {
        user.stop();
    }

    /**
     * Updates the progress bar for the level view.
     */
    protected void updateProgressBar() {
        levelView.updateProgress(
                (double) spawnedEnemies / MAX_ENEMY_SPAWN,
                0.2
        );
    }

    /**
     * Updates the visual representation of all actors in the level view.
     *
     * @param levelView the {@link LevelView} to update.
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
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