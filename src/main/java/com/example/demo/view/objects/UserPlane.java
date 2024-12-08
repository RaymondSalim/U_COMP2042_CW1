package com.example.demo.view.objects;

import com.example.demo.audio.AudioEnum;
import com.example.demo.audio.AudioManager;
import com.example.demo.context.AppContext;
import com.example.demo.model.Player;
import com.example.demo.observer.ScreenSizeObserver;
import com.example.demo.view.entities.ActiveActorDestructible;
import com.example.demo.view.entities.FighterPlane;
import javafx.beans.property.SimpleDoubleProperty;

public class UserPlane extends FighterPlane implements ScreenSizeObserver {

    private static final String IMAGE_NAME = "userplane.png";
    private static final double Y_UPPER_BOUND = -40;
    private static final SimpleDoubleProperty Y_LOWER_BOUND = new SimpleDoubleProperty(600.0);
    private static final double INITIAL_X_POSITION = 5.0;
    private static final SimpleDoubleProperty INITIAL_Y_POSITION = new SimpleDoubleProperty(300.0);
    private static final int IMAGE_HEIGHT = 150;
    private static final int VERTICAL_VELOCITY = 80;
    private static final int PROJECTILE_X_POSITION = 110;
    private static final int PROJECTILE_Y_POSITION_OFFSET = 20;

    private int velocityMultiplier;
    private final Player player;

    public UserPlane(Player player) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION.get(), player.getHealth());

        this.velocityMultiplier = 0;
        this.player = player;
    }

    @Override
    public void onScreenSizeChanged(int newHeight, int newWidth) {
        AppContext context = AppContext.getInstance();
        Y_LOWER_BOUND.bind(context.getScreenHeightPropertyProperty().subtract(IMAGE_HEIGHT));
        INITIAL_Y_POSITION.bind(Y_LOWER_BOUND.divide(2));
    }

    public void resetPosition() {
        this.setLayoutX(INITIAL_X_POSITION);
        this.setLayoutY(INITIAL_Y_POSITION.get());
    }

    @Override
    public void updatePosition(double deltaTime) {
        if (isMoving()) {
            double initialTranslateY = getTranslateY();
            this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier * deltaTime);
            double newPosition = getLayoutY() + getTranslateY();
            if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND.get()) {
                this.setTranslateY(initialTranslateY);
            }
        } else {
            // Check if out of bounds when resizing
            double currentYPosition = getLayoutY() + getTranslateY();

            if (currentYPosition < Y_UPPER_BOUND) {
                setTranslateY(Y_UPPER_BOUND - getLayoutY());
            } else if (currentYPosition > Y_LOWER_BOUND.get()) {
                setTranslateY(Y_LOWER_BOUND.get() - getLayoutY());
            }
        }
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        AudioManager manager = AudioManager.getInstance();
        manager.playSoundEffect(AudioEnum.PLAYER_SHOOT, false);
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

    public int getKillCount() {
        return player.getKillCount();
    }
}
