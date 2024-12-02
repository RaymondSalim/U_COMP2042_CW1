package com.example.demo.view.screens;

import com.example.demo.context.AppContext;
import com.example.demo.view.base.ImageButton;
import com.example.demo.view.base.NavigationHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import static com.example.demo.view.base.Constants.SHARED_RESOURCE_FOLDER;

public class CreditsScreen {
    private final NavigationHandler navigationHandler;
    private final String CREDITS_TEXT = """
            DEVELOPED BY    RAYMOND SALIM
            DESIGNED BY     RAYMOND SALIM
            """;
    private Scene scene;
    private StackPane mainContainer;
    private VBox creditsContainer;
    private ImageButton backButton;
    private ImageView titleImageView;
    private Timeline timeline;

    public CreditsScreen(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public Scene createScene() {
        mainContainer = new StackPane();
        scene = new Scene(mainContainer);
        mainContainer.setAlignment(Pos.CENTER);

        setupContent();

        timeline = createTimeline();

        return scene;
    }

    private void setupContent() {
        Background background = new Background(
                new BackgroundImage(
                        new Image(getClass().getResource(SHARED_RESOURCE_FOLDER + "/bg.png").toExternalForm()),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(1.0, 1.0, true, true, false, false)
                )
        );
        mainContainer.setBackground(background);

        creditsContainer = new VBox();
        creditsContainer.setAlignment(Pos.CENTER);

        // Title
        Image titleImage = new Image(getClass().getResource(SHARED_RESOURCE_FOLDER + "/1942.png").toExternalForm());
        titleImageView = new ImageView(titleImage);
        titleImageView.setPreserveRatio(true);
        titleImageView.fitWidthProperty().bind(scene.widthProperty().multiply(0.4));

        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), 24);
        Label creditsLabel = new Label(CREDITS_TEXT);
        creditsLabel.setTextAlignment(TextAlignment.CENTER);
        creditsLabel.setStyle("-fx-text-fill: white; -fx-padding: 20px;");
        creditsLabel.setWrapText(true);
        creditsLabel.setAlignment(Pos.CENTER);
        creditsLabel.setFont(font);

        creditsContainer.getChildren().addAll(titleImageView, creditsLabel);

        AppContext context = AppContext.getInstance();
        creditsContainer.setTranslateY(context.getScreenHeight() / 3.0 * 2); // Start at the bottom of the screen
        mainContainer.getChildren().addAll(creditsContainer);
    }

    private void showEndOfCreditsScene() {
        // Back Button
        backButton = new ImageButton(SHARED_RESOURCE_FOLDER + "/backButton.png");
        backButton.setPreserveRatio(true);
        backButton.setFitWidth(50); // Set a fixed width for the back button
        backButton.setOnMouseClicked(e -> {
            navigationHandler.showMenuScreen();
            timeline.stop();
        });
        StackPane.setMargin(backButton, new Insets(20, 0, 0, 20));
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

        mainContainer.getChildren().clear();
        mainContainer.getChildren().addAll(titleImageView, backButton);
    }

    private Timeline createTimeline() {
        timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
            creditsContainer.setTranslateY(creditsContainer.getTranslateY() - 1); // Move up 1px per frame
            if (creditsContainer.getBoundsInParent().getMaxY() < 0) { // Check if the text has exited the screen
                timeline.stop(); // Stop the timeline when text exits
                showEndOfCreditsScene();
            }
        });
        timeline.getKeyFrames().add(keyFrame);

        timeline.setCycleCount(Timeline.INDEFINITE); // Keep running until stopped
        return timeline;
    }

    public void startCredits() {
        mainContainer.getChildren().clear();
        setupContent();
        timeline.play();
    }
}