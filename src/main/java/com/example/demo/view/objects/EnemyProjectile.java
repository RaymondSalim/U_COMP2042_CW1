package com.example.demo.view.objects;

import com.example.demo.view.entities.Projectile;

/**
 * Represents a projectile fired by an enemy plane.
 */
public class EnemyProjectile extends Projectile {

    private static final String IMAGE_NAME = "enemyFire.png";
    private static final int IMAGE_HEIGHT = 25;
    private static final int HORIZONTAL_VELOCITY = -80;

    /**
     * Constructs an {@code EnemyProjectile} instance.
     *
     * @param initialXPos the initial x-coordinate of the projectile.
     * @param initialYPos the initial y-coordinate of the projectile.
     */
    public EnemyProjectile(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    @Override
    public void updatePosition(double deltaTime) {
        moveHorizontally(HORIZONTAL_VELOCITY * deltaTime);
    }

    @Override
    public void updateActor(double deltaTime) {
        updatePosition(deltaTime);
    }
}