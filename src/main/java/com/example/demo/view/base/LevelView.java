package com.example.demo.view.base;

import com.example.demo.context.AppContext;
import com.example.demo.view.RollingBackground;
import com.example.demo.view.objects.HeartDisplay;
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

public abstract class LevelView {
    private static final double HEART_DISPLAY_X_POSITION = 25;
    private static final double HEART_DISPLAY_Y_POSITION = 25;
    private static final double PROGRESS_BAR_Y_POSITION = 40; // Y position for the progress bar
    private static final double SCORE_FONT_SIZE = 24;
    private static final double FPS_FONT_SIZE = 16;

    private final Group root;
    private final HeartDisplay heartDisplay;
    private final Label scoreLabel;
    private final ProgressBar progressBar;
    private final Text fpsText;
    private RollingBackground rollingBackground;

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

    protected void initializeBackground(Image... backgroundImages) {
        this.rollingBackground = new RollingBackground(backgroundImages);
        root.getChildren().add(rollingBackground);
        rollingBackground.start();
    }

    public void initializeUI() {
        AppContext context = AppContext.getInstance();

        // Top-left: Vertical layout for hearts and score
        VBox healthAndScoreBox = new VBox(5); // Add spacing between hearts and score
        healthAndScoreBox.setLayoutX(HEART_DISPLAY_X_POSITION);
        healthAndScoreBox.setLayoutY(HEART_DISPLAY_Y_POSITION);
        healthAndScoreBox.getChildren().addAll(heartDisplay.getContainer(), scoreLabel);
        root.getChildren().add(healthAndScoreBox);

        // Top-center: Progress bar
        progressBar.setLayoutX(((double) context.getScreenWidth() / 2) - 200); // Center horizontally
        progressBar.setLayoutY(PROGRESS_BAR_Y_POSITION);
        root.getChildren().add(progressBar);

        // Bottom-right: FPS text
        fpsText.setLayoutX(context.getScreenWidth() - 100);
        fpsText.setLayoutY(context.getScreenHeight() - 50);
        root.getChildren().add(fpsText);
    }

    public void reset() {
        heartDisplay.resetHearts();
        updateScore(0); // Reset score
        updateProgress(0, 0); // Reset progress
        if (rollingBackground != null) {
            rollingBackground.restart();
        }
    }

    public void updateHealth(int health) {
        heartDisplay.setHearts(health);
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateProgress(double targetProgress, double durationInSeconds) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(durationInSeconds),
                        new KeyValue(progressBar.progressProperty(), targetProgress)
                )
        );
        timeline.play();
    }

    public void updateFPS(double fps) {
        AppContext context = AppContext.getInstance();
        fps = Math.max(context.getTargetFPS().doubleValue(), fps);
        fpsText.setText(String.format("FPS: %.0f", fps));
    }

    public void addActor(ActiveActorDestructible actor) {
        if (!root.getChildren().contains(actor)) {
            root.getChildren().add(actor);
        }
    }

    public void removeActor(ActiveActorDestructible actor) {
        root.getChildren().remove(actor);
    }

    public void updateActor(ActiveActorDestructible actor, double deltaTime) {
        if (actor.isDestroyed()) {
            removeActor(actor);
        } else {
            actor.updateVisual(deltaTime);
        }
    }

    public void pause() {
        if (rollingBackground != null) {
            rollingBackground.stop();
        }
    }

    public void resume() {
        if (rollingBackground != null) {
            rollingBackground.start();
        }
    }

    public Group getRoot() {
        return root;
    }
}