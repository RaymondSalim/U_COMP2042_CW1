package com.example.demo.view;

import com.example.demo.context.AppContext;
import com.example.demo.view.base.ImageButton;
import com.example.demo.view.base.NavigationHandler;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MenuScreen {
    private final NavigationHandler navigationHandler;

    private final int MAX_METEOR = 5;
    private final String MENU_ASSETS_FOLDER = "/com/example/demo/images/menu";

    public MenuScreen(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public Scene createScene() {
        AppContext context = AppContext.getInstance();

        // Background
        Image backgroundImage = new Image(getClass().getResource(MENU_ASSETS_FOLDER + "/bg.png").toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(context.getScreenWidth());
        backgroundImageView.setFitHeight(context.getScreenHeight());

        // Title
        Image titleImage = new Image(getClass().getResource(MENU_ASSETS_FOLDER + "/1942.png").toExternalForm());
        ImageView titleImageView = new ImageView(titleImage);
        titleImageView.setPreserveRatio(true);
        titleImageView.setFitWidth(context.getScreenWidth() * 0.4);

        // Buttons
        ImageButton startButton = new ImageButton(MENU_ASSETS_FOLDER + "/start2.png");
        ImageButton settingsButton = new ImageButton(MENU_ASSETS_FOLDER + "/settings.png");
        ImageButton exitButton = new ImageButton(MENU_ASSETS_FOLDER + "/exit.png");

        List<ImageButton> mainButtons = Arrays.asList(startButton, settingsButton, exitButton);
        mainButtons.forEach(b -> {
            b.setPreserveRatio(true);
            b.setFitWidth(context.getScreenWidth() * 0.15);
            b.minWidth(150);
        });

        startButton.setOnMouseClicked(e -> navigationHandler.showLevelSelectScreen());
        settingsButton.setOnMouseClicked(e -> navigationHandler.showSettingsScreen());
        exitButton.setOnMouseClicked(e -> navigationHandler.exitGame());

        VBox mainButtonsPane = new VBox(15, startButton, settingsButton, exitButton);
        mainButtonsPane.setAlignment(Pos.CENTER);

        VBox centerPane = new VBox(100, titleImageView, mainButtonsPane);
        centerPane.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.setPrefWidth(context.getScreenWidth());
        root.setPrefHeight(context.getScreenHeight());

        // Random Meteor
        Pane meteorPane = new Pane();
        Random random = new Random();
        Timeline meteorTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> spawnMeteor(random, meteorPane, context)));
        meteorTimeline.setCycleCount(Timeline.INDEFINITE);
        meteorTimeline.play();

        root.getChildren().addAll(backgroundImageView, meteorPane, centerPane);
        StackPane.setAlignment(centerPane, Pos.CENTER);

        return new Scene(root, context.getScreenWidth(), context.getScreenHeight());
    }

    private void spawnMeteor(Random random, Pane root, AppContext context) {
        long existingMeteors = root.getChildren().stream().filter(child -> {
            if (child.getUserData() instanceof String) {
                return child.getUserData().equals("meteor");
            }
            return false;
        }).count();

        if (existingMeteors >= MAX_METEOR) return;

        int meteorIndex = random.nextInt(10) + 1;
        Image meteorImage = new Image(getClass().getResource("/com/example/demo/images/meteors/meteor" + meteorIndex + ".png").toExternalForm());
        ImageView meteor = new ImageView(meteorImage);
        meteor.setUserData("meteor");
        meteor.setFitWidth(100);
        meteor.setPreserveRatio(true);

        double initialX = -100 + random.nextDouble() * (context.getScreenWidth() + 200);
        double initialY = -100;
        meteor.setLayoutX(initialX);
        meteor.setLayoutY(initialY);

        root.getChildren().add(meteor);

        double targetX = random.nextDouble() * (context.getScreenWidth() + 200) - 100;
        double targetY = context.getScreenHeight() + 100;
        double distanceX = targetX - initialX;
        double distanceY = targetY - initialY;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        double duration = distance / (50 * (random.nextDouble() * 1 + 0.5));

        Timeline moveMeteor = new Timeline(new KeyFrame(Duration.seconds(duration), new KeyValue(meteor.layoutXProperty(), targetX), new KeyValue(meteor.layoutYProperty(), targetY)));
        moveMeteor.setOnFinished(event -> root.getChildren().remove(meteor));
        moveMeteor.play();

        int rotationDuration = random.nextInt(1, 10);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(rotationDuration), meteor);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setInterpolator(javafx.animation.Interpolator.LINEAR);
        rotateTransition.play();
    }
}
