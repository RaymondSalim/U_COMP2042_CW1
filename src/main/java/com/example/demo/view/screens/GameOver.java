package com.example.demo.view.screens;

import com.example.demo.context.AppContext;
import com.example.demo.observer.ScreenSizeObserver;
import com.example.demo.view.base.Constants;
import com.example.demo.view.base.ImageButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class GameOver implements ScreenSizeObserver {
    private final String BG_IMAGE = "/com/example/demo/images/gameOverMenu/gameOverWindow.png";

    private VBox menu;
    private Image bgImageFile;

    public GameOver(
            Runnable onRestart,
            Runnable onLevelSelect
    ) {
        bgImageFile = new Image(getClass().getResource(BG_IMAGE).toExternalForm());

        AppContext context = AppContext.getInstance();
        context.addGameStateObserver(this);

        double height = context.getScreenHeight() * 0.65;
        double aspectRatio = bgImageFile.getWidth() / bgImageFile.getHeight();
        double width = height * aspectRatio;

        menu = new VBox();
        menu.setMaxHeight(height);
        menu.setMaxWidth(width);
        menu.setPadding(new Insets(50, 10, 20, 10));

        BackgroundImage bgImage = new BackgroundImage(
                bgImageFile,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 65, true, true, true, false)
        );

        HBox buttonsPane = new HBox(25);
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.prefWidthProperty().bind(menu.widthProperty());

        ImageButton restartButton = new ImageButton(Constants.SHARED_RESOURCE_FOLDER + "/restartButton.png");
        ImageButton levelSelectButton = new ImageButton(Constants.SHARED_RESOURCE_FOLDER + "/levelSelectButton.png");

        restartButton.setPreserveRatio(true);
        levelSelectButton.setPreserveRatio(true);
        restartButton.fitWidthProperty().bind(menu.widthProperty().divide(3).subtract(25));
        levelSelectButton.fitWidthProperty().bind(menu.widthProperty().divide(3).subtract(25));

        levelSelectButton.setOnMouseClicked(event -> onLevelSelect.run());
        restartButton.setOnMouseClicked(event -> {
            menu.setVisible(false);
            onRestart.run();
        });

        buttonsPane.getChildren().addAll(restartButton, levelSelectButton);

        // Make sure buttons are at the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        menu.getChildren().addAll(spacer, buttonsPane);

        menu.setBackground(new Background(bgImage));
        menu.setVisible(false);
    }

    @Override
    public void onScreenSizeChanged(int newHeight, int newWidth) {
        double height = newHeight * 0.65;
        double aspectRatio = bgImageFile.getWidth() / bgImageFile.getHeight();
        double width = height * aspectRatio;

        menu.setMaxHeight(height);
        menu.setMaxWidth(width);
    }

    public Pane getPane() {
        return menu;
    }

    public void show() {
        menu.setVisible(true);
    }

    public void hide() {
        menu.setVisible(false);
    }
}