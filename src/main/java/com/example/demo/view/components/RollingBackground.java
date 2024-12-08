package com.example.demo.view.components;

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

/**
 * Creates a continuously scrolling background effect.
 * <p>
 * The {@code RollingBackground} uses multiple background images to create a seamless
 * scrolling effect that adapts to screen resizing.
 * </p>
 */
public class RollingBackground extends Group {
    private final List<ImageView> backgrounds;
    private final Timeline timeline;

    /**
     * Constructs a new {@code RollingBackground} with the specified images.
     *
     * @param images the images to use as the background.
     */
    public RollingBackground(Image... images) {
        this.backgrounds = new ArrayList<>();
        AppContext context = AppContext.getInstance();

        for (Image image : images) {
            ImageView background = new ImageView(image);
            background.setPreserveRatio(true);

            background.fitHeightProperty().bind(context.getScreenHeightPropertyProperty().asObject());
            background.fitWidthProperty().bind(
                    Bindings.createDoubleBinding(
                            () -> image.getWidth() * (context.getScreenHeightPropertyProperty().get() / image.getHeight()),
                            context.getScreenHeightPropertyProperty()
                    )
            );

            backgrounds.add(background);
        }

        this.getChildren().addAll(backgrounds);

        context.getScreenWidthPropertyProperty().addListener((obs, oldVal, newVal) -> repositionBackgrounds());
        context.getScreenHeightPropertyProperty().addListener((obs, oldVal, newVal) -> repositionBackgrounds());

        repositionBackgrounds();

        timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> updateBackground()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Updates the positions of the backgrounds for the scrolling effect.
     */
    private void updateBackground() {
        for (ImageView background : backgrounds) {
            background.setLayoutX(background.getLayoutX() - 2);
        }

        for (int i = 0; i < backgrounds.size(); i++) {
            ImageView background = backgrounds.get(i);
            if (background.getLayoutX() + background.getFitWidth() <= 0) {
                ImageView lastBackground = backgrounds.get((i - 1 + backgrounds.size()) % backgrounds.size());
                background.setLayoutX(lastBackground.getLayoutX() + lastBackground.getFitWidth());
            }
        }
    }

    /**
     * Repositions backgrounds to ensure seamless scrolling.
     */
    private void repositionBackgrounds() {
        double totalWidth = 0;

        for (ImageView background : backgrounds) {
            background.setLayoutX(totalWidth);
            totalWidth += background.getFitWidth();
        }

        AppContext context = AppContext.getInstance();
        double screenWidth = context.getScreenWidthPropertyProperty().get();

        while (totalWidth < screenWidth) {
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

    /**
     * Starts the scrolling effect.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the scrolling effect.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Restarts the scrolling effect, repositioning the backgrounds.
     */
    public void restart() {
        timeline.stop();
        repositionBackgrounds();
        timeline.play();
    }
}