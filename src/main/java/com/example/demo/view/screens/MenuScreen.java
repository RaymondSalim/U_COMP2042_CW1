package com.example.demo.view.screens;

import com.example.demo.context.AppContext;
import com.example.demo.utils.NavigationHandler;
import com.example.demo.view.components.ImageButton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
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

/**
 * Represents the main menu screen of the game.
 */
public class MenuScreen implements Screen {
    private final NavigationHandler navigationHandler;

    private final int MAX_METEOR = 5;
    private final String MENU_ASSETS_FOLDER = "/com/example/demo/images/menu";

    /**
     * Constructs a {@code MenuScreen}.
     *
     * @param navigationHandler the handler to navigate between screens.
     */
    public MenuScreen(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    @Override
    public Scene createScene() {
        AppContext context = AppContext.getInstance();

        // Background
        Image backgroundImage = new Image(getClass().getResource(MENU_ASSETS_FOLDER + "/bg.png").toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(context.getScreenWidthPropertyProperty());
        backgroundImageView.fitHeightProperty().bind(context.getScreenHeightPropertyProperty());

        // Title
        Image titleImage = new Image(getClass().getResource(MENU_ASSETS_FOLDER + "/1942.png").toExternalForm());
        ImageView titleImageView = new ImageView(titleImage);
        titleImageView.setPreserveRatio(true);
        titleImageView.fitWidthProperty().bind(context.getScreenWidthPropertyProperty().multiply(0.4));

        // Main Buttons
        ImageButton startButton = new ImageButton(MENU_ASSETS_FOLDER + "/start2.png");
        ImageButton settingsButton = new ImageButton(MENU_ASSETS_FOLDER + "/settings.png");
        ImageButton exitButton = new ImageButton(MENU_ASSETS_FOLDER + "/exit.png");

        List<ImageButton> mainButtons = Arrays.asList(startButton, settingsButton, exitButton);
        mainButtons.forEach(b -> {
            b.setPreserveRatio(true);
            b.fitWidthProperty().bind(context.getScreenWidthPropertyProperty().multiply(0.15));
            b.minWidth(150);
        });

        startButton.setOnMouseClicked(e -> navigationHandler.showLevelSelectScreen());
        settingsButton.setOnMouseClicked(e -> navigationHandler.showSettingsScreen());
        exitButton.setOnMouseClicked(e -> navigationHandler.exitGame());

        VBox mainButtonsPane = new VBox(15, startButton, settingsButton, exitButton);
        mainButtonsPane.setAlignment(Pos.CENTER);

        // Credits Button
        ImageButton creditsButton = new ImageButton(MENU_ASSETS_FOLDER + "/creditsBtn.png");
        creditsButton.setPreserveRatio(true);
        creditsButton.setFitWidth(50);
        creditsButton.setOnMouseClicked(e -> navigationHandler.showCreditsScreen());
        StackPane.setMargin(creditsButton, new Insets(20));
        StackPane.setAlignment(creditsButton, Pos.BOTTOM_RIGHT);

        VBox centerPane = new VBox(100, titleImageView, mainButtonsPane);
        centerPane.setAlignment(Pos.CENTER);

        StackPane root = new StackPane();
        root.prefWidthProperty().bind(context.getScreenWidthPropertyProperty());
        root.prefHeightProperty().bind(context.getScreenHeightPropertyProperty());

        // Random Meteor
        Pane meteorPane = new Pane();
        Random random = new Random();
        Timeline meteorTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> spawnMeteor(random, meteorPane, context)));
        meteorTimeline.setCycleCount(Timeline.INDEFINITE);
        meteorTimeline.play();

        root.getChildren().addAll(backgroundImageView, meteorPane, centerPane, creditsButton);
        StackPane.setAlignment(centerPane, Pos.CENTER);

        return new Scene(root, context.getScreenWidthPropertyProperty().get(), context.getScreenHeightPropertyProperty().get());
    }

    /**
     * Spawns a random meteor animation.
     *
     * @param random  the random object for meteor properties.
     * @param root    the root container for meteors.
     * @param context the application context for screen properties.
     */
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

        double initialX = -100 + random.nextDouble() * (context.getScreenWidthPropertyProperty().get() + 200);
        double initialY = -100;
        meteor.setLayoutX(initialX);
        meteor.setLayoutY(initialY);

        root.getChildren().add(meteor);

        double targetX = random.nextDouble() * (context.getScreenWidthPropertyProperty().get() + 200) - 100;
        double targetY = context.getScreenHeightPropertyProperty().get() + 100;
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
