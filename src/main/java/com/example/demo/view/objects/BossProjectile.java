package com.example.demo.view.objects;

import com.example.demo.view.entities.Projectile;

/**
 * Represents a projectile fired by the boss enemy.
 */
public class BossProjectile extends Projectile {

    private static final String IMAGE_NAME = "fireball.png";
    private static final int IMAGE_HEIGHT = 75;
    private static final int HORIZONTAL_VELOCITY = -80;
    private static final int INITIAL_X_POSITION = 950;

    /**
     * Constructs a {@code BossProjectile} instance.
     *
     * @param initialYPos the initial y-coordinate of the projectile.
     */
    public BossProjectile(double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
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
