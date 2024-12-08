package com.example.demo.view.components;

import com.example.demo.audio.AudioEnum;
import com.example.demo.audio.AudioManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;

/**
 * A button with image, hover, and click effects.
 * <p>
 * The {@code ImageButton} provides a visual button that reacts to user interaction.
 * </p>
 */
public class ImageButton extends ImageView {

    /**
     * Constructs a new {@code ImageButton} with the specified image.
     *
     * @param path the path to the image resource for the button.
     */
    public ImageButton(String path) {
        URL imageRes = getClass().getResource(path);
        if (imageRes != null) {
            this.setImage(new Image(imageRes.toExternalForm()));
        }

        this.addClickEffect();
        this.addHoverEffect();
    }

    /**
     * Adds a scaling effect for click events.
     */
    private void addClickEffect() {
        ScaleTransition pressTransition = new ScaleTransition(Duration.millis(100), this);
        pressTransition.setToX(0.9);
        pressTransition.setToY(0.9);

        ScaleTransition releaseTransition = new ScaleTransition(Duration.millis(100), this);
        releaseTransition.setToX(1.0);
        releaseTransition.setToY(1.0);

        this.setOnMousePressed(event -> {
            pressTransition.playFromStart();
            playAudio();
        });
        this.setOnMouseReleased(event -> releaseTransition.playFromStart());
    }

    /**
     * Adds a brightness effect for hover events.
     */
    private void addHoverEffect() {
        ColorAdjust hoverEffect = new ColorAdjust();
        Timeline hoverEnterTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(hoverEffect.brightnessProperty(), 0.0)),
                new KeyFrame(Duration.millis(150), new KeyValue(hoverEffect.brightnessProperty(), 0.15))
        );
        Timeline hoverExitTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(hoverEffect.brightnessProperty(), 0.15)),
                new KeyFrame(Duration.millis(150), new KeyValue(hoverEffect.brightnessProperty(), 0.0))
        );

        this.setOnMouseEntered(event -> {
            this.setEffect(hoverEffect);
            hoverEnterTimeline.playFromStart();
        });
        this.setOnMouseExited(event -> hoverExitTimeline.playFromStart());
    }

    /**
     * Plays the click sound effect.
     */
    protected void playAudio() {
        AudioManager manager = AudioManager.getInstance();
        manager.playSoundEffect(AudioEnum.BUTTON_CLICK, false);
    }
}
