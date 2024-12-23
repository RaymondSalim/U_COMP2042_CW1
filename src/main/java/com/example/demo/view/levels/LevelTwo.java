package com.example.demo.view.levels;

import javafx.scene.Group;
import javafx.scene.image.Image;

/**
 * Represents the visual and UI configuration for Level Two.
 */
public class LevelTwo extends LevelView {
    private static final String BACKGROUND_IMAGE_NAME_1 = "/com/example/demo/images/level/bg2.png";
    private static final String BACKGROUND_IMAGE_NAME_2 = "/com/example/demo/images/level/bg2r.png";

    /**
     * Constructs a {@code LevelTwo} instance with its unique background and UI.
     *
     * @param root            the {@link Group} to which all level elements are added.
     * @param heartsToDisplay the number of hearts to display for the player's health.
     */
    public LevelTwo(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);

        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_1).toExternalForm());
        Image backgroundImage2 = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_2).toExternalForm());

        this.initializeBackground(backgroundImage, backgroundImage2);
    }
}
