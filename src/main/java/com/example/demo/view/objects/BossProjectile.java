package com.example.demo.view.objects;

import com.example.demo.view.entities.Projectile;

public class BossProjectile extends Projectile {

    private static final String IMAGE_NAME = "fireball.png";
    private static final int IMAGE_HEIGHT = 75;
    private static final int HORIZONTAL_VELOCITY = -80;
    private static final int INITIAL_X_POSITION = 950;

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
