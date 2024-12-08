package com.example.demo.view.entities;

public abstract class Projectile extends ActiveActorDestructible {

    public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
    }

    @Override
    public void takeDamage() {
        this.destroy();
    }
}
