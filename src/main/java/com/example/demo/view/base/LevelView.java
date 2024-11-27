package com.example.demo.view.base;

import com.example.demo.view.RollingBackground;
import com.example.demo.view.objects.HeartDisplay;
import javafx.scene.Group;
import javafx.scene.image.Image;

public abstract class LevelView {
    private static final double HEART_DISPLAY_X_POSITION = 25;
    private static final double HEART_DISPLAY_Y_POSITION = 25;

    private final Group root;
    private final HeartDisplay heartDisplay;
    private RollingBackground rollingBackground;

    public LevelView(Group root, int heartsToDisplay) {
        this.root = root;
        this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
    }

    protected void initializeBackground(Image... backgroundImages) {
        this.rollingBackground = new RollingBackground(backgroundImages);
        root.getChildren().add(rollingBackground);
        rollingBackground.start();
    }

    /**
     * Initializes the heart display on the screen.
     */
    public void initializeHeartDisplay() {
        root.getChildren().add(heartDisplay.getContainer());
    }

    public void reset() {
        heartDisplay.resetHearts();
        if (rollingBackground != null) {
            rollingBackground.restart();
        }
    }

    public void updateHealth(int health) {
        heartDisplay.setHearts(health);
    }

    public void addActor(ActiveActorDestructible actor) {
        if (!root.getChildren().contains(actor)) {
            root.getChildren().add(actor);
        }
    }

    public void removeActor(ActiveActorDestructible actor) {
        root.getChildren().remove(actor);
    }

    public void updateActor(ActiveActorDestructible actor) {
        if (actor.isDestroyed()) {
            removeActor(actor);
        } else {
            // Ensure the actor's position or state is synchronized with the model.
            actor.updateVisual();
        }
    }

    public void pause() {
        if (rollingBackground != null) {
            rollingBackground.stop();
        }
    }

    public void resume() {
        if (rollingBackground != null) {
            rollingBackground.start();
        }
    }

    /**
     * Returns the root node containing all visual elements for the level.
     *
     * @return The root Group object.
     */
    public Group getRoot() {
        return root;
    }
}