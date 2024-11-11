package com.example.demo.view;

import com.example.demo.model.Player;
import com.example.demo.view.base.ActiveActorDestructible;
import com.example.demo.view.base.FighterPlane;

public class UserPlane extends FighterPlane {

    private static final String IMAGE_NAME = "userplane.png";
    private static final double Y_UPPER_BOUND = -40;
    private static final double Y_LOWER_BOUND = 600.0;
    private static final double INITIAL_X_POSITION = 5.0;
    private static final double INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 150;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int PROJECTILE_X_POSITION = 110;
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20;

    private int velocityMultiplier;
    private final Player player;

    public UserPlane(Player player) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, player.getHealth());
        this.velocityMultiplier = 0;
        this.player = player;
    }

    public void resetPosition() {
        this.setLayoutX(INITIAL_X_POSITION);
        this.setLayoutY(INITIAL_Y_POSITION);
    }

    @Override
    public void updatePosition() {
        if (isMoving()) {
            double initialTranslateY = getTranslateY();
            this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
            double newPosition = getLayoutY() + getTranslateY();
            if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
                this.setTranslateY(initialTranslateY);
            }
        }
    }

    @Override
    public void updateActor() {
        updatePosition();
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        return new UserProjectile(PROJECTILE_X_POSITION, getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
    }

    private boolean isMoving() {
        return velocityMultiplier != 0;
    }

    public void moveUp() {
        velocityMultiplier = -1;
    }

    public void moveDown() {
        velocityMultiplier = 1;
    }

    public void stop() {
        velocityMultiplier = 0;
    }

    public int getHealth() {
        return player.getHealth();
    }

    public void incrementKillCount() {
        player.incrementKillCount();
    }

    public void takeDamage() {
        player.takeDamage();
    }

    public boolean isDestroyed() {
        return player.isDestroyed();
    }
}
