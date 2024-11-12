package com.example.demo.model.base;

import com.example.demo.context.AppContext;
import com.example.demo.enums.GameState;
import com.example.demo.enums.LevelType;
import com.example.demo.model.Player;
import com.example.demo.observer.GameStateObservable;
import com.example.demo.observer.ScreenSizeObserver;
import com.example.demo.view.EnemyPlane;
import com.example.demo.view.UserPlane;
import com.example.demo.view.base.ActiveActorDestructible;
import com.example.demo.view.base.FighterPlane;
import com.example.demo.view.base.LevelView;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LevelParent extends GameStateObservable implements ScreenSizeObserver {
    protected int TOTAL_ENEMIES = 5;
    protected int KILLS_TO_ADVANCE = 10;
    protected double ENEMY_SPAWN_PROBABILITY = .20;
    protected LevelType NEXT_LEVEL;

    private static final double BOTTOM_MARGIN = 150;
    private double screenHeight;
    private double screenWidth;
    private double enemyMaximumYPosition;

    private final Group root;
    private final UserPlane user;
    private final ImageView background;

    private final Player player;
    private final List<ActiveActorDestructible> friendlyUnits;
    private final List<ActiveActorDestructible> enemyUnits;
    private final List<ActiveActorDestructible> userProjectiles;
    private final List<ActiveActorDestructible> enemyProjectiles;

    private final LevelView levelView;

    public LevelParent(String backgroundImageName, int playerInitialHealth) {
        this.root = new Group();
        root.layoutXProperty().addListener((_, _, _) -> root.setLayoutX(0));
        root.layoutYProperty().addListener((_, _, _) -> root.setLayoutY(0));

        AppContext context = AppContext.getInstance();
        this.screenHeight = context.getScreenHeight();
        this.screenWidth = context.getScreenWidth();

        this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
        this.enemyMaximumYPosition = screenHeight - BOTTOM_MARGIN;
        this.levelView = instantiateLevelView();
        initializeBackground();

        this.friendlyUnits = new ArrayList<>();
        this.enemyUnits = new ArrayList<>();
        this.userProjectiles = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();

        this.player = new Player(playerInitialHealth);
        this.user = new UserPlane(player);

        levelView.showHeartDisplay();
        friendlyUnits.add(user);
        initializeFriendlyUnits();
    }

    @Override
    public void onScreenSizeChanged(int newHeight, int newWidth) {
        this.screenHeight = newHeight;
        this.screenWidth = newWidth;

        this.enemyMaximumYPosition = this.screenHeight - BOTTOM_MARGIN;

        root.getChildren().remove(background);
        background.setFitHeight(this.screenHeight);
        background.setFitWidth(this.screenWidth);
        root.getChildren().add(0, background);

        repositionOutOfBoundsActors();
    }

    private void repositionOutOfBoundsActors() {
        for (ActiveActorDestructible enemy : enemyUnits) {
            double enemyYPosition = enemy.getLayoutY() + enemy.getTranslateY();
            if (enemyYPosition > enemyMaximumYPosition) {
                enemy.setTranslateY(enemyMaximumYPosition - enemy.getLayoutY()); // Move back within bounds
            } else if (enemyYPosition < 0) {
                enemy.setTranslateY(-enemy.getLayoutY()); // Move to the top bound
            }
        }

        List<ActiveActorDestructible> projectiles = new ArrayList<>();
        projectiles.addAll(userProjectiles);
        projectiles.addAll(enemyProjectiles);

        for (ActiveActorDestructible projectile : projectiles) {
            double projectileYPosition = projectile.getLayoutY() + projectile.getTranslateY();
            if (projectileYPosition > screenHeight || projectileYPosition < 0) {
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

    protected Player getPlayer() {
        return player;
    }

    public Group getRoot() {
        return root;
    }

    public LevelView getLevelView() {
        return levelView;
    }

    public void updateScene() {
        spawnEnemyUnits();
        updateActors();
        generateEnemyFire();
        handleEnemyPenetration();
        handleUserProjectileCollisions();
        handleEnemyProjectileCollisions();
        handlePlaneCollisions();
        removeAllDestroyedActors();
        updateLevelView();
        checkPlayerHealth();
        checkKillCount();
    }

    private void checkPlayerHealth() {
        if (player.isDestroyed()) {
            notifyEvent(GameState.GAME_OVER);
        }
    }

    private void checkKillCount() {
        if (player.getKillCount() >= KILLS_TO_ADVANCE) {
            notifyEvent(GameState.LEVEL_COMPLETED);
        }
    }

    private void initializeFriendlyUnits() {
        this.root.getChildren().addAll(this.user);
    }

    private void initializeBackground() {
        background.setFocusTraversable(true);
        background.setFitHeight(screenHeight);
        background.setFitWidth(screenWidth);
        background.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();
            if (kc == KeyCode.UP) user.moveUp();
            if (kc == KeyCode.DOWN) user.moveDown();
            if (kc == KeyCode.SPACE) fireProjectile();
        });
        background.setOnKeyReleased(e -> {
            KeyCode kc = e.getCode();
            if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
        });
        root.getChildren().add(background);
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
        for (int i = 0; i < TOTAL_ENEMIES - enemyUnits.size(); i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyInitialYPosition = Math.random() * enemyMaximumYPosition;
                ActiveActorDestructible newEnemy = new EnemyPlane(screenWidth, newEnemyInitialYPosition);
                addEnemyUnit(newEnemy);
            }
        }
    }

    private void updateActors() {
        friendlyUnits.forEach(plane -> plane.updateActor());
        enemyUnits.forEach(enemy -> enemy.updateActor());
        userProjectiles.forEach(projectile -> projectile.updateActor());
        enemyProjectiles.forEach(projectile -> projectile.updateActor());
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
        handleCollisions(friendlyUnits, enemyUnits);
    }

    private void handleUserProjectileCollisions() {
        handleCollisions(userProjectiles, enemyUnits);
        handleCollisions(userProjectiles, enemyProjectiles);
    }

    private void handleEnemyProjectileCollisions() {
        handleCollisions(enemyProjectiles, friendlyUnits);
    }

    private void handleCollisions(List<ActiveActorDestructible> actors1,
                                  List<ActiveActorDestructible> actors2) {
        for (ActiveActorDestructible actor : actors2) {
            for (ActiveActorDestructible otherActor : actors1) {
                if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
                    actor.takeDamage();
                    otherActor.takeDamage();
                }
            }
        }
    }

    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth;
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
        levelView.removeHearts(player.getHealth());
    }

    protected void addEnemyUnit(ActiveActorDestructible enemy) {
        enemyUnits.add(enemy);
        root.getChildren().add(enemy);
    }

    public void notifyWin() {
        notifyEvent(GameState.WIN);
    }
}
