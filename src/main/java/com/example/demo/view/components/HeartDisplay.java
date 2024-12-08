package com.example.demo.view.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Displays the player's remaining health as hearts.
 * <p>
 * The heart display consists of an image of a heart and a label indicating the count.
 * It is designed to be added to the game's UI.
 * </p>
 */
public class HeartDisplay {

    private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
    private static final int HEART_HEIGHT = 50;
    private static final int TEXT_FONT_SIZE = 24;
    private static final int GAP_BETWEEN_HEART_AND_TEXT = 10;

    private final HBox container;
    private final double containerXPosition;
    private final double containerYPosition;
    private final ImageView heartIcon;
    private final Label heartLabel;
    private final int heartAmount;

    /**
     * Constructs a new {@code HeartDisplay}.
     *
     * @param xPosition       the x-coordinate of the heart display.
     * @param yPosition       the y-coordinate of the heart display.
     * @param heartsToDisplay the initial number of hearts to display.
     */
    public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.heartAmount = heartsToDisplay;

        this.container = new HBox(GAP_BETWEEN_HEART_AND_TEXT);
        this.heartIcon = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));
        this.heartLabel = new Label();

        initializeContainer();
        setHearts(heartAmount);
    }

    /**
     * Initializes the heart display's container and styles.
     */
    private void initializeContainer() {
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);

        heartIcon.setFitHeight(HEART_HEIGHT);
        heartIcon.setPreserveRatio(true);

        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), TEXT_FONT_SIZE);
        heartLabel.setFont(font);
        heartLabel.setTextAlignment(TextAlignment.CENTER);
        heartLabel.setStyle("-fx-text-fill: white; -fx-padding: 4px 0 0 0");

        container.getChildren().addAll(heartIcon, heartLabel);
    }

    /**
     * Resets the heart display to its initial amount.
     */
    public void resetHearts() {
        setHearts(this.heartAmount);
    }

    /**
     * Updates the heart display with the specified number of hearts.
     *
     * @param currentHearts the number of hearts to display.
     */
    public void setHearts(int currentHearts) {
        heartLabel.setText("x " + currentHearts);
    }

    /**
     * Gets the container for this heart display.
     *
     * @return the {@link HBox} container.
     */
    public HBox getContainer() {
        return container;
    }
}