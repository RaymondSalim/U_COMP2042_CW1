package com.example.demo.view.levels;

import com.example.demo.context.AppContext;
import com.example.demo.view.components.HeartDisplay;
import com.example.demo.view.components.RollingBackground;
import com.example.demo.view.entities.ActiveActorDestructible;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Abstract base class for defining visuals and UI for game levels.
 *
 * <p>
 * Includes functionality for managing background animations, player health, score display,
 * progress tracking, and FPS monitoring.
 * </p>
 */
public abstract class LevelView {

    private static final double HEART_DISPLAY_X_POSITION = 25;
    private static final double HEART_DISPLAY_Y_POSITION = 25;
    private static final double PROGRESS_BAR_Y_POSITION = 40;
    private static final double SCORE_FONT_SIZE = 24;
    private static final double FPS_FONT_SIZE = 16;

    /**
     * The root {@link Group} to which all level components are added.
     */
    private final Group root;

    /**
     * The heart display showing the player's current health.
     */
    private final HeartDisplay heartDisplay;

    /**
     * The label showing the player's current score.
     */
    private final Label scoreLabel;

    /**
     * The progress bar for tracking level progress.
     */
    private final ProgressBar progressBar;

    /**
     * The text element for displaying the FPS (frames per second).
     */
    private final Text fpsText;

    /**
     * The rolling background animation for the level.
     */
    private RollingBackground rollingBackground;

    /**
     * Constructs a new {@code LevelView}.
     *
     * @param root            the {@link Group} to which all level elements are added.
     * @param heartsToDisplay the number of hearts to display for the player's health.
     */
    public LevelView(Group root, int heartsToDisplay) {
        this.root = root;

        // Initialize heart display
        this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);

        // Initialize score label
        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), SCORE_FONT_SIZE);
        this.scoreLabel = new Label("Score: 0");
        this.scoreLabel.setFont(font);
        this.scoreLabel.setStyle("-fx-text-fill: white;");

        // Initialize progress bar
        this.progressBar = new ProgressBar(0);
        this.progressBar.setPrefWidth(400); // Adjust width as needed

        // Initialize FPS text
        Font fpsFont = Font.font("Verdana", FPS_FONT_SIZE);
        this.fpsText = new Text("FPS: 0");
        this.fpsText.setFont(fpsFont);
        this.fpsText.setStyle("-fx-fill: white;");
    }

    /**
     * Initializes the rolling background for the level using the specified images.
     *
     * @param backgroundImages the images to use for the background.
     */
    protected void initializeBackground(Image... backgroundImages) {
        this.rollingBackground = new RollingBackground(backgroundImages);
        root.getChildren().add(rollingBackground);
        rollingBackground.start();
    }

    /**
     * Initializes the level's UI elements, including health, score, progress bar, and FPS display.
     */
    public void initializeUI() {
        AppContext context = AppContext.getInstance();

        // Top-left: Vertical layout for hearts and score
        VBox healthAndScoreBox = new VBox(5); // Add spacing between hearts and score
        healthAndScoreBox.setLayoutX(HEART_DISPLAY_X_POSITION);
        healthAndScoreBox.setLayoutY(HEART_DISPLAY_Y_POSITION);
        healthAndScoreBox.getChildren().addAll(heartDisplay.getContainer(), scoreLabel);
        root.getChildren().add(healthAndScoreBox);

        // Top-center: Progress bar
        progressBar.layoutXProperty().bind(context.getScreenWidthPropertyProperty().divide(2).subtract(200));
        progressBar.setLayoutY(PROGRESS_BAR_Y_POSITION);
        root.getChildren().add(progressBar);

        // Bottom-right: FPS text
        fpsText.layoutXProperty().bind(context.getScreenWidthPropertyProperty().subtract(100));
        fpsText.layoutYProperty().bind(context.getScreenHeightPropertyProperty().subtract(50));
        root.getChildren().add(fpsText);
    }

    /**
     * Resets the level's UI elements to their initial state.
     */
    public void reset() {
        heartDisplay.resetHearts();
        updateScore(0); // Reset score
        updateProgress(0, 0); // Reset progress
        if (rollingBackground != null) {
            rollingBackground.restart();
        }
    }

    /**
     * Updates the player's health in the heart display.
     *
     * @param health the current health value to display.
     */
    public void updateHealth(int health) {
        heartDisplay.setHearts(health);
    }

    /**
     * Updates the player's score in the score label.
     *
     * @param score the current score to display.
     */
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Updates the progress bar with a new value over a specified duration.
     *
     * @param targetProgress     the target progress value (0 to 1).
     * @param durationInSeconds the duration over which the progress bar should update.
     */
    public void updateProgress(double targetProgress, double durationInSeconds) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(durationInSeconds),
                        new KeyValue(progressBar.progressProperty(), targetProgress)
                )
        );
        timeline.play();
    }

    /**
     * Updates the displayed FPS value.
     *
     * @param fps the current frames per second value to display.
     */
    public void updateFPS(double fps) {
        AppContext context = AppContext.getInstance();
        fps = Math.min(context.getTargetFPS().doubleValue(), fps);
        fpsText.setText(String.format("FPS: %.0f", fps));
    }

    /**
     * Adds an actor to the level's root group if it's not already present.
     *
     * @param actor the {@link ActiveActorDestructible} to add.
     */
    public void addActor(ActiveActorDestructible actor) {
        if (!root.getChildren().contains(actor)) {
            root.getChildren().add(actor);
        }
    }

    /**
     * Removes an actor from the level's root group.
     *
     * @param actor the {@link ActiveActorDestructible} to remove.
     */
    public void removeActor(ActiveActorDestructible actor) {
        root.getChildren().remove(actor);
    }

    /**
     * Updates the visual state of an actor and removes it if it's destroyed.
     *
     * @param actor     the {@link ActiveActorDestructible} to update.
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
    public void updateActor(ActiveActorDestructible actor, double deltaTime) {
        if (actor.isDestroyed()) {
            removeActor(actor);
        } else {
            actor.updateVisual(deltaTime);
        }
    }

    /**
     * Pauses the level, including stopping any background animations.
     */
    public void pause() {
        if (rollingBackground != null) {
            rollingBackground.stop();
        }
    }

    /**
     * Resumes the level, including restarting any background animations.
     */
    public void resume() {
        if (rollingBackground != null) {
            rollingBackground.start();
        }
    }

    /**
     * Gets the root {@link Group} containing all elements of the level.
     *
     * @return the root group.
     */
    public Group getRoot() {
        return root;
    }
}