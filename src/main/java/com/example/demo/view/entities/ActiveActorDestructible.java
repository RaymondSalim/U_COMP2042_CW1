package com.example.demo.view.entities;

/**
 * Extends {@link ActiveActor} to represent an actor that can take damage and be destroyed.
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

    private boolean isDestroyed;

    /**
     * Constructs a destructible {@code ActiveActor}.
     *
     * @param imageName   the name of the image file for this actor.
     * @param imageHeight the height of the image.
     * @param initialXPos the initial x-coordinate of the actor.
     * @param initialYPos the initial y-coordinate of the actor.
     */
    public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        isDestroyed = false;
    }

    @Override
    public abstract void updatePosition(double deltaTime);

    @Override
    public void updateActor(double deltaTime) {
        if (isDestroyed) return;
        updatePosition(deltaTime);
    }

    /**
     * Inflicts damage on the actor.
     */
    @Override
    public abstract void takeDamage();

    @Override
    public void destroy() {
        setDestroyed(true);
    }

    /**
     * Checks if the actor is destroyed.
     *
     * @return {@code true} if the actor is destroyed; {@code false} otherwise
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Sets the destroyed state of the actor.
     *
     * @param isDestroyed {@code true} to mark the actor as destroyed; {@code false} otherwise.
     */
    protected void setDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    /**
     * Updates the visual state of the actor.
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
    public void updateVisual(double deltaTime) {
        if (isDestroyed()) {
            this.setVisible(false);
        } else {
            this.setVisible(true);
            updatePosition(deltaTime);
        }
    }
}