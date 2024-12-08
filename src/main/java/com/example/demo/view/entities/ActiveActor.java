package com.example.demo.view.entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an active actor in the game, which can perform actions or interact with other objects.
 * This is a base class for entities like planes, projectiles, and other interactive objects.
 */
public abstract class ActiveActor extends ImageView {

    private static final String IMAGE_LOCATION = "/com/example/demo/images/";

    /**
     * Constructs an {@code ActiveActor} with the specified image and position.
     *
     * @param imageName   the name of the image file for this actor.
     * @param imageHeight the height of the image.
     * @param initialXPos the initial x-coordinate of the actor.
     * @param initialYPos the initial y-coordinate of the actor.
     */
    public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        this.setImage(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
        this.setLayoutX(initialXPos);
        this.setLayoutY(initialYPos);
        this.setFitHeight(imageHeight);
        this.setPreserveRatio(true);
    }

    /**
     * Updates the position of the actor.
     *
     * @param deltaTime the time elapsed since the last update, in seconds
     */
    public abstract void updatePosition(double deltaTime);

    /**
     * Updates the visual representation and behavior of the actor.
     *
     * @param deltaTime the time elapsed since the last update, in seconds
     */
    public abstract void updateActor(double deltaTime);

    /**
     * Moves the actor horizontally by a specified amount.
     *
     * @param horizontalMove the distance to move horizontally.
     */
    protected void moveHorizontally(double horizontalMove) {
        this.setTranslateX(getTranslateX() + horizontalMove);
    }

    /**
     * Moves the actor vertically by a specified amount.
     *
     * @param verticalMove the distance to move vertically.
     */
    protected void moveVertically(double verticalMove) {
        this.setTranslateY(getTranslateY() + verticalMove);
    }
}