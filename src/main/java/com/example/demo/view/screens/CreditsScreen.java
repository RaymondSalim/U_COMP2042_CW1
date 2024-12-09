package com.example.demo.view.screens;

import com.example.demo.context.AppContext;
import com.example.demo.utils.NavigationHandler;
import com.example.demo.view.components.ImageButton;
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

import static com.example.demo.utils.Constants.SHARED_RESOURCE_FOLDER;

/**
 * Represents the credits screen displaying developer credits and design information.
 */
public class CreditsScreen implements Screen {
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

    /**
     * Constructs a {@code CreditsScreen}.
     *
     * @param navigationHandler the handler to navigate between screens.
     */
    public CreditsScreen(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    @Override
    public Scene createScene() {
        mainContainer = new StackPane();
        scene = new Scene(mainContainer);
        mainContainer.setAlignment(Pos.CENTER);

        setupContent();

        timeline = createTimeline();

        return scene;
    }

    /**
     * Sets up the layout and content for the credits screen.
     */
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
        creditsContainer.translateYProperty().set(context.getScreenHeightPropertyProperty().divide(3).multiply(2).get());
        mainContainer.getChildren().addAll(creditsContainer);
    }

    /**
     * Displays the end-of-credits scene with a back button.
     */
    private void showEndOfCreditsScene() {
        backButton = new ImageButton(SHARED_RESOURCE_FOLDER + "/backButton.png");
        backButton.setPreserveRatio(true);
        backButton.setFitWidth(50);
        backButton.setOnMouseClicked(e -> {
            navigationHandler.showMenuScreen();
            timeline.stop();
        });
        StackPane.setMargin(backButton, new Insets(20, 0, 0, 20));
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

        mainContainer.getChildren().clear();
        mainContainer.getChildren().addAll(titleImageView, backButton);
    }

    /**
     * Creates the timeline animation for scrolling the credits.
     *
     * @return the timeline controlling the credits scroll.
     */
    private Timeline createTimeline() {
        timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
            creditsContainer.translateYProperty().set(creditsContainer.getTranslateY() - 1);
            if (creditsContainer.getBoundsInParent().getMaxY() < 0) {
                timeline.stop();
                showEndOfCreditsScene();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    /**
     * Starts the scrolling animation for the credits.
     */
    public void startCredits() {
        mainContainer.getChildren().clear();
        setupContent();
        timeline.play();
    }
}