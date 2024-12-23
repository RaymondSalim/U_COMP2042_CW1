package com.example.demo.view.screens;

import com.example.demo.context.AppContext;
import com.example.demo.utils.NavigationHandler;
import com.example.demo.view.components.ImageButton;
import com.example.demo.view.components.ImageCheckbox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.utils.Constants.SHARED_RESOURCE_FOLDER;

/**
 * Represents the settings screen of the game.
 */
public class SettingsScreen implements Screen {
    private final NavigationHandler navigationHandler;

    /**
     * Constructs a {@code SettingsScreen}.
     *
     * @param navigationHandler the handler to navigate between screens.
     */
    public SettingsScreen(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    @Override
    public Scene createScene() {
        AppContext context = AppContext.getInstance();

        StackPane layout = new StackPane();
        Background background = new Background(
                new BackgroundImage(
                        new Image(getClass().getResource(SHARED_RESOURCE_FOLDER + "/bg.png").toExternalForm()),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(1.0, 1.0, true, true, false, false)
                )
        );
        layout.setBackground(background);

        // FPS Selection
        ImageCheckbox fps30Checkbox = new ImageCheckbox(context.getTargetFPS().doubleValue() == 30.0f);
        ImageCheckbox fps60Checkbox = new ImageCheckbox(context.getTargetFPS().doubleValue() == 60.0f);

        List<ImageButton> fpsButtons = Arrays.asList(fps30Checkbox, fps60Checkbox);
        fpsButtons.forEach(b -> {
            b.setPreserveRatio(true);
            b.fitWidthProperty().bind(context.getScreenWidthPropertyProperty().multiply(0.06));
            b.minWidth(50);
        });

        fps30Checkbox.setOnMouseClicked(event -> {
            if (!fps30Checkbox.isChecked()) {
                fps30Checkbox.setChecked(true);
                fps60Checkbox.setChecked(false);
                context.setTargetFPS(30);
            }
        });

        fps60Checkbox.setOnMouseClicked(event -> {
            if (!fps60Checkbox.isChecked()) {
                fps60Checkbox.setChecked(true);
                fps30Checkbox.setChecked(false);
                context.setTargetFPS(60);
            }
        });

        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), 24);
        Label fpsLabel = new Label("FPS Cap:");
        fpsLabel.setStyle("-fx-text-fill: white;");
        fpsLabel.setFont(font);
        Label fps30Label = new Label("30 FPS");
        fps30Label.setStyle("-fx-text-fill: white;");
        fps30Label.setFont(font);
        Label fps60Label = new Label("60 FPS");
        fps60Label.setStyle("-fx-text-fill: white;");
        fps60Label.setFont(font);

        HBox fpsOptionBox1 = new HBox(5, fps30Checkbox, fps30Label);
        fpsOptionBox1.setAlignment(Pos.CENTER);
        HBox fpsOptionBox2 = new HBox(5, fps60Checkbox, fps60Label);
        fpsOptionBox2.setAlignment(Pos.CENTER);

        HBox fpsBox = new HBox(50);
        fpsBox.setAlignment(Pos.CENTER);
        fpsBox.getChildren().addAll(fpsLabel, fpsOptionBox1, fpsOptionBox2);

        // Volume Adjustment
        Label volumeLabel = new Label("Volume:");
        volumeLabel.setStyle("-fx-text-fill: white;");
        volumeLabel.setFont(font);

        Slider volumeSlider = new Slider(0, 100, context.getVolume());
        volumeSlider.setMajorTickUnit(25);
        volumeSlider.setMinorTickCount(4);
        volumeSlider.setBlockIncrement(10);
        volumeSlider.setPrefWidth(400);
        volumeSlider.setPrefHeight(35);
        volumeSlider.valueProperty().bindBidirectional(context.volumeProperty());

        HBox volumeBox = new HBox(20, volumeLabel, volumeSlider);
        volumeBox.setAlignment(Pos.CENTER);

        // Back Button
        ImageButton backButton = new ImageButton(SHARED_RESOURCE_FOLDER + "/backButton.png");
        backButton.setPreserveRatio(true);
        backButton.setFitWidth(50);
        backButton.setOnMouseClicked(e -> navigationHandler.showMenuScreen());
        StackPane.setMargin(backButton, new Insets(20, 0, 0, 20));
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

        // Layout
        VBox settingsBox = new VBox(30, fpsBox, volumeBox);
        settingsBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(settingsBox, backButton);

        return new Scene(layout, context.getScreenWidthPropertyProperty().get(), context.getScreenHeightPropertyProperty().get());
    }
}