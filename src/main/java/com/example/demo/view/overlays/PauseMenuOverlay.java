package com.example.demo.view.overlays;

import com.example.demo.context.AppContext;
import com.example.demo.view.components.ImageButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * Represents the pause menu overlay displayed when the game is paused.
 * <p>
 * This overlay provides options to resume the game, restart the level, or return to the level select screen.
 */
public class PauseMenuOverlay implements Overlay {
    private final String BG_IMAGE = "/com/example/demo/images/pauseMenu/pauseWindow.png";
    private final VBox menu;

    private final Image bgImageFile;
    private final Label scoreLabel;
    private final Slider volumeSlider;

    /**
     * Constructs a {@code PauseMenuOverlay}.
     *
     * @param onResume      the action to perform when the "Resume" button is clicked.
     * @param onRestart     the action to perform when the "Restart" button is clicked.
     * @param onLevelSelect the action to perform when the "Level Select" button is clicked.
     */
    public PauseMenuOverlay(
            Runnable onResume,
            Runnable onRestart,
            Runnable onLevelSelect
    ) {
        bgImageFile = new Image(getClass().getResource(BG_IMAGE).toExternalForm());

        AppContext context = AppContext.getInstance();
        double aspectRatio = bgImageFile.getWidth() / bgImageFile.getHeight();

        menu = new VBox(20);
        menu.maxHeightProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.65));
        menu.maxWidthProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.65).multiply(aspectRatio));
        menu.setPadding(new Insets(20));
        menu.setAlignment(Pos.TOP_CENTER);

        BackgroundImage bgImage = new BackgroundImage(
                bgImageFile,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 65, true, true, true, false)
        );

        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), 24);
        this.scoreLabel = new Label("Score: 0");
        this.scoreLabel.setFont(font);
        this.scoreLabel.setStyle("-fx-text-fill: white;");

        Label volumeLabel = new Label("Volume:");
        volumeLabel.setStyle("-fx-text-fill: white;");
        volumeLabel.setFont(font);

        volumeSlider = new Slider(0, 100, context.getVolume());
        volumeSlider.setMajorTickUnit(25);
        volumeSlider.setMinorTickCount(4);
        volumeSlider.setBlockIncrement(10);
        volumeSlider.setPrefWidth(250);
        volumeSlider.setPrefHeight(35);
        volumeSlider.valueProperty().bindBidirectional(context.volumeProperty());

        HBox volumeBox = new HBox(20, volumeLabel, volumeSlider);
        volumeBox.setAlignment(Pos.CENTER);

        // Buttons
        HBox buttonsPane = new HBox(10);
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.prefWidthProperty().bind(menu.widthProperty());

        ImageButton levelSelectButton = new ImageButton("/com/example/demo/images/shared/levelSelectButton.png");
        ImageButton restartButton = new ImageButton("/com/example/demo/images/shared/restartButton.png");
        ImageButton resumeButton = new ImageButton("/com/example/demo/images/shared/playButton.png");

        resumeButton.setOnMouseClicked(event -> onResume.run());
        levelSelectButton.setOnMouseClicked(event -> onLevelSelect.run());
        restartButton.setOnMouseClicked(event -> {
            menu.setVisible(false);
            onRestart.run();
        });

        buttonsPane.getChildren().addAll(levelSelectButton, restartButton, resumeButton);

        double buttonSpacingFactor = 1.0 / buttonsPane.getChildren().size();
        buttonsPane.getChildren().forEach(button -> {
            ImageButton imageButton = (ImageButton) button;
            imageButton.fitWidthProperty().bind(buttonsPane.widthProperty().multiply(buttonSpacingFactor).subtract(20));
            imageButton.fitHeightProperty().bind(imageButton.fitWidthProperty().multiply(imageButton.getImage().getHeight() / imageButton.getImage().getWidth()));
        });

        // Top Spacer
        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        // Bottom Spacer (to keep buttons at the bottom)
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        menu.getChildren().addAll(topSpacer, scoreLabel, volumeBox, bottomSpacer, buttonsPane);

        menu.setBackground(new Background(bgImage));
        menu.setVisible(false);
    }

    /**
     * Updates the score displayed on the overlay.
     *
     * @param score the current score to display.
     */
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public Pane getPane() {
        return menu;
    }

    @Override
    public void show() {
        menu.setVisible(true);
    }

    @Override
    public void hide() {
        menu.setVisible(false);
    }
}
