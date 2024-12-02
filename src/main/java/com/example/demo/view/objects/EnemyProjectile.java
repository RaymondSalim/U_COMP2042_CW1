package com.example.demo.view.objects;

import com.example.demo.view.base.Projectile;

public class EnemyProjectile extends Projectile {

    private static final String IMAGE_NAME = "enemyFire.png";
    private static final int IMAGE_HEIGHT = 50;
    private static final int HORIZONTAL_VELOCITY = -80;

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
