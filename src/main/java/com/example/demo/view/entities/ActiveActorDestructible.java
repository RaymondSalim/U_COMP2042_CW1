package com.example.demo.view.entities;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

    private boolean isDestroyed;

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
    @Override
    public abstract void takeDamage();

    @Override
    public void destroy() {
        setDestroyed(true);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    protected void setDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    public void updateVisual(double deltaTime) {
        // If the actor is destroyed, make it invisible
        if (isDestroyed()) {
            this.setVisible(false);
        } else {
            // Ensure visibility if not destroyed
            this.setVisible(true);

            // Update position based on current logical state
            updatePosition(deltaTime);
        }
    }
}
