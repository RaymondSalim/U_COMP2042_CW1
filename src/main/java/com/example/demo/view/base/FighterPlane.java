package com.example.demo.view.base;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class FighterPlane extends ActiveActorDestructible {
    private final IntegerProperty healthProperty;

    public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        this.healthProperty = new SimpleIntegerProperty(health);
    }

    public abstract ActiveActorDestructible fireProjectile();

    @Override
    public void takeDamage() {
        healthProperty.set(healthProperty.get() - 1); // todo!
        if (healthProperty.get() == 0) {
            this.destroy();
        }
    }

    protected double getProjectileXPosition(double xPositionOffset) {
        return getLayoutX() + getTranslateX() + xPositionOffset;
    }

    protected double getProjectileYPosition(double yPositionOffset) {
        return getLayoutY() + getTranslateY() + yPositionOffset;
    }

    public IntegerProperty getHealthProperty() {
        return healthProperty;
    }
}
