package com.example.demo.view.base;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

    private boolean isDestroyed;

    public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        isDestroyed = false;
    }

    @Override
    public abstract void updatePosition();

    public abstract void updateActor();

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

    public void updateVisual() {
        // If the actor is destroyed, make it invisible
        if (isDestroyed()) {
            this.setVisible(false);
        } else {
            // Ensure visibility if not destroyed
            this.setVisible(true);

            // Update position based on current logical state
            updatePosition();
        }
    }
}
