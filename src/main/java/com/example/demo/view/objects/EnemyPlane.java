package com.example.demo.view.objects;

import com.example.demo.view.entities.ActiveActorDestructible;
import com.example.demo.view.entities.FighterPlane;

public class EnemyPlane extends FighterPlane {
    private static final String IMAGE_NAME = "enemyplane.png";
    private static final int IMAGE_HEIGHT = 75;
    private static final int HORIZONTAL_VELOCITY = -60;
    private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
    private static final int INITIAL_HEALTH = 1;
    private static final double FIRE_RATE = .01;

    /**
     * Constructs an {@code EnemyPlane} instance.
     *
     * @param initialXPos the initial x-coordinate of the plane.
     * @param initialYPos the initial y-coordinate of the plane.
     */
    public EnemyPlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    @Override
    public void updatePosition(double deltaTime) {
        moveHorizontally(HORIZONTAL_VELOCITY * deltaTime);
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
            double projectileYPostion = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
            return new EnemyProjectile(projectileXPosition, projectileYPostion);
        }
        return null;
    }

    @Override
    public void updateActor(double deltaTime) {
        updatePosition(deltaTime);
    }
}