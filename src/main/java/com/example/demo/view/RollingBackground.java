package com.example.demo.view;

import com.example.demo.context.AppContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class RollingBackground extends Group {
    private final List<ImageView> backgrounds;
    private final Timeline timeline;

    public RollingBackground(Image... images) {
        this.backgrounds = new ArrayList<>();

        AppContext context = AppContext.getInstance();

        double initialX = 0;
        for (Image image : images) {
            ImageView background = new ImageView(image);
            background.setPreserveRatio(true);
            background.setFitHeight(context.getScreenHeight());
            double imageWidth = image.getWidth() * (context.getScreenHeight() / image.getHeight());
            background.setFitWidth(imageWidth);
            background.setLayoutX(initialX);
            initialX += imageWidth;  // Use the calculated width instead of getFitWidth()
            backgrounds.add(background);
        }

        this.getChildren().addAll(backgrounds);

        timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> updateBackground()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateBackground() {
        for (ImageView background : backgrounds) {
            background.setLayoutX(background.getLayoutX() - 2);
        }

        // If a background moves completely out of the scene, reset its position
        for (int i = 0; i < backgrounds.size(); i++) {
            ImageView background = backgrounds.get(i);
            if (background.getLayoutX() + background.getFitWidth() <= 0) {
                ImageView lastBackground = backgrounds.get((i - 1 + backgrounds.size()) % backgrounds.size());
                background.setLayoutX(lastBackground.getLayoutX() + lastBackground.getFitWidth());
            }
        }
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }

    public void restart() {
        timeline.stop();

        // Reset the positions of all backgrounds
        double initialX = 0;
        for (ImageView background : backgrounds) {
            background.setLayoutX(initialX);
            initialX += background.getFitWidth();
        }

        timeline.play();
    }
}
