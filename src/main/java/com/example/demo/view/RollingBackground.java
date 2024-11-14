package com.example.demo.view;

import com.example.demo.context.AppContext;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class RollingBackground {
    private final Stage stage;
    private final List<ImageView> backgrounds;
    private final Timeline timeline;

    public RollingBackground(Stage stage, Image... images) {
        this.stage = stage;
        this.backgrounds = new ArrayList<>();

        AppContext context = AppContext.getInstance();

        double initialX = 0;
        for (Image image : images) {
            ImageView background = new ImageView(image);
            background.setPreserveRatio(true);
            background.setFitHeight(context.getScreenHeight());
            background.setLayoutX(initialX);
            initialX += background.getFitWidth();
            backgrounds.add(background);
        }

        Group root = new Group();
        root.getChildren().addAll(backgrounds);
        Scene scene = new Scene(root, context.getScreenWidth(), context.getScreenHeight());
        stage.setScene(scene);

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
        stage.show();
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}
