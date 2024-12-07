package com.example.demo.view;

import com.example.demo.context.AppContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
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

        // Initialize backgrounds
        for (Image image : images) {
            ImageView background = new ImageView(image);
            background.setPreserveRatio(true);

            // Bind the height to the screen height property
            background.fitHeightProperty().bind(context.getScreenHeightPropertyProperty().asObject());

            // Calculate width based on aspect ratio and bind it
            background.fitWidthProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> image.getWidth() * (context.getScreenHeightPropertyProperty().get() / image.getHeight()),
                            context.getScreenHeightPropertyProperty()
                    )
            );

            backgrounds.add(background);
        }

        this.getChildren().addAll(backgrounds);

        // Listen for resizing and adjust positions dynamically
        context.getScreenWidthPropertyProperty().addListener((obs, oldVal, newVal) -> repositionBackgrounds());
        context.getScreenHeightPropertyProperty().addListener((obs, oldVal, newVal) -> repositionBackgrounds());

        // Initialize positions
        repositionBackgrounds();

        // Setup timeline for scrolling effect
        timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> updateBackground()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateBackground() {
        // Move each background to the left
        for (ImageView background : backgrounds) {
            background.setLayoutX(background.getLayoutX() - 2);
        }

        // Wrap backgrounds when they scroll out of view
        for (int i = 0; i < backgrounds.size(); i++) {
            ImageView background = backgrounds.get(i);
            if (background.getLayoutX() + background.getFitWidth() <= 0) {
                ImageView lastBackground = backgrounds.get((i - 1 + backgrounds.size()) % backgrounds.size());
                background.setLayoutX(lastBackground.getLayoutX() + lastBackground.getFitWidth());
            }
        }
    }

    private void repositionBackgrounds() {
        double totalWidth = 0;

        // Recalculate positions based on the current width of each background
        for (ImageView background : backgrounds) {
            background.setLayoutX(totalWidth);
            totalWidth += background.getFitWidth(); // Adjust for the new width
        }

        // Ensure enough backgrounds to fill the screen width without gaps
        AppContext context = AppContext.getInstance();
        double screenWidth = context.getScreenWidthPropertyProperty().get();

        while (totalWidth < screenWidth) {
            // Duplicate the last background if necessary
            ImageView lastBackground = backgrounds.get(backgrounds.size() - 1);
            ImageView duplicate = new ImageView(lastBackground.getImage());
            duplicate.setPreserveRatio(true);

            duplicate.fitHeightProperty().bind(context.getScreenHeightPropertyProperty().asObject());
            duplicate.fitWidthProperty().bind(lastBackground.fitWidthProperty());

            duplicate.setLayoutX(totalWidth);
            totalWidth += duplicate.getFitWidth();
            this.getChildren().add(duplicate);
            backgrounds.add(duplicate);
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
        repositionBackgrounds();
        timeline.play();
    }
}