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

public class ImageButton extends ImageView {
    public ImageButton(String path) {
        URL imageRes = getClass().getResource(path);
        if (imageRes != null) {
            this.setImage(new Image(imageRes.toExternalForm()));
        }

        this.addClickEffect();
        this.addHoverEffect();
    }

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

    protected void playAudio() {
        AudioManager manager = AudioManager.getInstance();
        manager.playSoundEffect(AudioEnum.BUTTON_CLICK, false);
    }
}
