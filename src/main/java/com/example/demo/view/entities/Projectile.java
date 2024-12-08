package com.example.demo.view.entities;

/**
 * Represents a projectile in the game.
 *
 * <p>Projectiles are fired by actors such as {@link FighterPlane} and interact with other objects.</p>
 */
public abstract class Projectile extends ActiveActorDestructible {

    /**
     * Constructs a {@code Projectile}.
     *
     * @param imageName   the name of the image file for the projectile.
     * @param imageHeight the height of the image.
     * @param initialXPos the initial x-coordinate of the projectile.
     * @param initialYPos the initial y-coordinate of the projectile.
     */
    public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
    }

    @Override
    public void takeDamage() {
        this.destroy();
    }
}
