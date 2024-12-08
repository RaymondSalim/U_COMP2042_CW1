package com.example.demo.view.overlays;

import com.example.demo.context.AppContext;
import com.example.demo.utils.Constants;
import com.example.demo.view.components.ImageButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

/**
 * Represents the "Game Over" overlay displayed when the player loses.
 */
public class GameOverOverlay implements Overlay {
    private final String BG_IMAGE = "/com/example/demo/images/gameOverMenu/gameOverWindow.png";

    private VBox menu;
    private Image bgImageFile;

    /**
     * Constructs a {@code GameOverOverlay}.
     *
     * @param onRestart     the action to perform when the "Restart" button is clicked.
     * @param onLevelSelect the action to perform when the "Level Select" button is clicked.
     */
    public GameOverOverlay(
            Runnable onRestart,
            Runnable onLevelSelect
    ) {
        bgImageFile = new Image(getClass().getResource(BG_IMAGE).toExternalForm());

        AppContext context = AppContext.getInstance();
        double aspectRatio = bgImageFile.getWidth() / bgImageFile.getHeight();

        menu = new VBox();
        menu.maxHeightProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.65));
        menu.maxWidthProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.65).multiply(aspectRatio));
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

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        menu.getChildren().addAll(spacer, buttonsPane);
        menu.setBackground(new Background(bgImage));
        menu.setVisible(false);
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