package com.example.demo.view.screens;

import com.example.demo.audio.AudioEnum;
import com.example.demo.audio.AudioManager;
import com.example.demo.context.AppContext;
import com.example.demo.view.base.ImageButton;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class LevelComplete {
    private static final int TEXT_FONT_SIZE = 24; // Increased font size
    private final String BG_IMAGE = "/com/example/demo/images/levelComplete/levelComplete.png";
    private final String STAR_IMAGE = "/com/example/demo/images/levelComplete/star.png";
    private final String GLOWING_STAR_IMAGE = "/com/example/demo/images/levelComplete/starGlow.png";
    private final VBox menu;

    private final Image bgImageFile;
    private final ImageView[] stars = new ImageView[3]; // Three stars

    private final Label scoreLabel; // Label for displaying the score
    private int starCount = 0;
    private int score = 0;

    public LevelComplete(
            Runnable onLevelSelect,
            Runnable onRestart,
            Runnable onNextLevel
    ) {
        bgImageFile = new Image(getClass().getResource(BG_IMAGE).toExternalForm());

        AppContext context = AppContext.getInstance();
        double aspectRatio = bgImageFile.getWidth() / bgImageFile.getHeight();

        menu = new VBox();
        menu.maxHeightProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.65));
        menu.maxWidthProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.65).multiply(aspectRatio));
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(50, 10, 20, 10));

        BackgroundImage bgImage = new BackgroundImage(
                bgImageFile,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 65, true, true, true, false)
        );

        // Initialize the stars
        HBox starsPane = new HBox(20);
        starsPane.setAlignment(Pos.CENTER);
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new ImageView(new Image(getClass().getResource(STAR_IMAGE).toExternalForm()));
            stars[i].fitWidthProperty().bind(menu.widthProperty().divide(4));
            stars[i].setPreserveRatio(true);
            starsPane.getChildren().add(stars[i]);
        }

        stars[0].rotateProperty().set(-20);
        stars[1].setTranslateY(-20);
        stars[2].rotateProperty().set(20);

        // Initialize the score label
        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), TEXT_FONT_SIZE);
        scoreLabel = new Label("Score: 0");
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setFont(font);
        scoreLabel.paddingProperty().set(new Insets(20, 0, 0, 0));
        scoreLabel.setStyle("-fx-text-fill: white");

        // Spacer to center stars vertically
        Region topSpacer = new Region();
        Region bottomSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        // Buttons at the bottom
        HBox buttonsPane = new HBox(10);
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.prefWidthProperty().bind(menu.widthProperty());

        ImageButton levelSelectButton = new ImageButton("/com/example/demo/images/shared/levelSelectButton.png");
        ImageButton restartButton = new ImageButton("/com/example/demo/images/shared/restartButton.png");
        ImageButton nextLevelButton = new ImageButton("/com/example/demo/images/levelComplete/nextButton.png");

        restartButton.setOnMouseClicked(event -> onRestart.run());
        levelSelectButton.setOnMouseClicked(event -> onLevelSelect.run());
        nextLevelButton.setOnMouseClicked(event -> {
            menu.setVisible(false);
            onNextLevel.run();
        });

        buttonsPane.getChildren().addAll(levelSelectButton, restartButton, nextLevelButton);

        // Bind button widths to fit evenly within buttonsPane
        double buttonSpacingFactor = 1.0 / buttonsPane.getChildren().size();
        buttonsPane.getChildren().forEach(button -> {
            ImageButton imageButton = (ImageButton) button;
            imageButton.fitWidthProperty().bind(buttonsPane.widthProperty().multiply(buttonSpacingFactor).subtract(20));
            imageButton.fitHeightProperty().bind(imageButton.fitWidthProperty().multiply(imageButton.getImage().getHeight() / imageButton.getImage().getWidth()));
        });

        menu.getChildren().addAll(topSpacer, starsPane, scoreLabel, bottomSpacer, buttonsPane);
        menu.setBackground(new Background(bgImage));
        menu.setVisible(false);
    }

    public void setStarCount(int starCount) {
        this.starCount = Math.max(0, Math.min(3, starCount));
    }

    public void setScore(int score) {
        this.score = score;
        animateScore();
    }

    private void animateScore() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(score);
        AtomicInteger currentScore = new AtomicInteger();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1), event -> {
            scoreLabel.setText("Score: " + (currentScore.incrementAndGet()));
        }));
        timeline.play();
    }

    private void glowStars() {
        AudioManager manager = AudioManager.getInstance();
        AudioEnum[] audios = new AudioEnum[]{AudioEnum.STAR_1, AudioEnum.STAR_2, AudioEnum.STAR_3};
        for (int i = 0; i < starCount; i++) {
            ImageView star = stars[i];

            // Create a PauseTransition for the delay
            PauseTransition delay = new PauseTransition(Duration.seconds(i * 0.5)); // Delay increases for each star

            // After the delay, set the glowing image and play the animation
            int finalI = i;
            delay.setOnFinished(event -> {
                manager.playSoundEffect(audios[finalI], false);
                star.setImage(new Image(getClass().getResource(GLOWING_STAR_IMAGE).toExternalForm()));

                // Add a glowing animation
                FadeTransition glow = new FadeTransition(Duration.seconds(0.5), star);
                glow.setFromValue(0.5);
                glow.setToValue(1.0);
                glow.setCycleCount(2);
                glow.play();
            });

            // Play the delay
            delay.play();
        }
    }

    private void reset() {
        setScore(0);
        for (int i = 0; i < starCount; i++) {
            ImageView star = stars[i];
            star.setImage(new Image(getClass().getResource(STAR_IMAGE).toExternalForm()));
        }
    }

    public Pane getPane() {
        return menu;
    }

    public void show() {
        menu.setVisible(true);
        glowStars();
    }

    public void hide() {
        menu.setVisible(false);
        reset();
    }
}
