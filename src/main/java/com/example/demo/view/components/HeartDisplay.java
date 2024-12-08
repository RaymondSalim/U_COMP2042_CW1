package com.example.demo.view.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class HeartDisplay {

    private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
    private static final int HEART_HEIGHT = 50;
    private static final int TEXT_FONT_SIZE = 24; // Increased font size
    private static final int GAP_BETWEEN_HEART_AND_TEXT = 10; // Gap between heart and text

    private final HBox container;
    private final double containerXPosition;
    private final double containerYPosition;

    private final ImageView heartIcon; // Single heart image
    private final Label heartLabel;   // Label to show "x N"

    private final int heartAmount;

    public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.heartAmount = heartsToDisplay;

        // Initialize components
        this.container = new HBox(GAP_BETWEEN_HEART_AND_TEXT); // Set gap between heart and text
        this.heartIcon = new ImageView(new Image(getClass().getResource(HEART_IMAGE_NAME).toExternalForm()));
        this.heartLabel = new Label();

        initializeContainer();
        setHearts(heartAmount);
    }

    private void initializeContainer() {
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);

        // Configure the heart icon
        heartIcon.setFitHeight(HEART_HEIGHT);
        heartIcon.setPreserveRatio(true);

        // Configure the label
        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), TEXT_FONT_SIZE);
        heartLabel.setFont(font);
        heartLabel.setTextAlignment(TextAlignment.CENTER);
        heartLabel.setStyle("-fx-text-fill: white; -fx-padding: 4px 0 0 0");

        // Add components to the container
        container.getChildren().addAll(heartIcon, heartLabel);
    }

    public void resetHearts() {
        setHearts(this.heartAmount);
    }

    public void setHearts(int currentHearts) {
        heartLabel.setText("x " + currentHearts);
    }

    public HBox getContainer() {
        return container;
    }
}