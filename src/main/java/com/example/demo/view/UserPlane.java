package com.example.demo.view;

import com.example.demo.context.AppContext;
import com.example.demo.model.Player;
import com.example.demo.observer.ScreenSizeObserver;
import com.example.demo.view.base.ActiveActorDestructible;
import com.example.demo.view.base.FighterPlane;

public class UserPlane extends FighterPlane implements ScreenSizeObserver {

    private static final String IMAGE_NAME = "userplane.png";
    private static final double Y_UPPER_BOUND = -40;
    private static double Y_LOWER_BOUND = 600.0;
    private static final double INITIAL_X_POSITION = 5.0;
    private static double INITIAL_Y_POSITION = 300.0;
    private static final int IMAGE_HEIGHT = 150;
    private static final int VERTICAL_VELOCITY = 8;
    private static final int PROJECTILE_X_POSITION = 110;
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20;

    private int velocityMultiplier;
    private final Player player;

    public UserPlane(Player player) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, player.getHealth());

        AppContext context = AppContext.getInstance();
        context.addGameStateObserver(this);

        this.velocityMultiplier = 0;
        this.player = player;
    }

    @Override
    public void onScreenSizeChanged(int newHeight, int newWidth) {
        AppContext context = AppContext.getInstance();
        Y_LOWER_BOUND = context.getScreenHeight() - IMAGE_HEIGHT;
        INITIAL_Y_POSITION = Y_LOWER_BOUND / 2;
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
        } else {
            // Check if out of bounds when resizing
            double currentYPosition = getLayoutY() + getTranslateY();

            if (currentYPosition < Y_UPPER_BOUND) {
                setTranslateY(Y_UPPER_BOUND - getLayoutY());
            } else if (currentYPosition > Y_LOWER_BOUND) {
                setTranslateY(Y_LOWER_BOUND - getLayoutY());
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
