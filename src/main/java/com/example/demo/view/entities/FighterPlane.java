package com.example.demo.view.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Represents a fighter plane in the game.
 *
 * <p>Fighter planes are active actors capable of movement and firing projectiles.</p>
 */
public abstract class FighterPlane extends ActiveActorDestructible {
    private final IntegerProperty healthProperty;

    /**
     * Constructs a {@code FighterPlane}.
     *
     * @param imageName   the name of the image file for the plane.
     * @param imageHeight the height of the image.
     * @param initialXPos the initial x-coordinate of the plane.
     * @param initialYPos the initial y-coordinate of the plane.
     * @param health      the initial health of the plane.
     */
    public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        this.healthProperty = new SimpleIntegerProperty(health);
    }

    /**
     * Fires a projectile from the fighter plane.
     *
     * @return the {@link Projectile} representing the fired shot
     */
    public abstract ActiveActorDestructible fireProjectile();

    @Override
    public void takeDamage() {
        healthProperty.set(healthProperty.get() - 1);
        if (healthProperty.get() <= 0) {
            this.destroy();
        }
    }

    /**
     * Calculates the x-coordinate for firing a projectile.
     *
     * @param xPositionOffset the offset to add to the x-coordinate of the plane.
     * @return the x-coordinate where the projectile should be positioned.
     */
    protected double getProjectileXPosition(double xPositionOffset) {
        return getLayoutX() + getTranslateX() + xPositionOffset;
    }

    /**
     * Calculates the y-coordinate for firing a projectile.
     *
     * @param yPositionOffset the offset to add to the y-coordinate of the plane.
     * @return the y-coordinate where the projectile should be positioned.
     */
    protected double getProjectileYPosition(double yPositionOffset) {
        return getLayoutY() + getTranslateY() + yPositionOffset;
    }

    /**
     * Gets the health property of the plane.
     *
     * @return the {@link IntegerProperty} representing the plane's health.
     */
    public IntegerProperty getHealthProperty() {
        return healthProperty;
    }
}
