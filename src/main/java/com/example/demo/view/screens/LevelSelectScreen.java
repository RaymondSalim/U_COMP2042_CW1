package com.example.demo.view.screens;

import com.example.demo.context.AppContext;
import com.example.demo.enums.LevelType;
import com.example.demo.view.components.ImageButton;
import com.example.demo.view.utils.NavigationHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;

import static com.example.demo.view.utils.Constants.SHARED_RESOURCE_FOLDER;

public class LevelSelectScreen implements Screen {
    private final String LEVEL_MENU_ASSETS_FOLDER = "/com/example/demo/images/levelSelectMenu";

    private final NavigationHandler navigationHandler;

    public LevelSelectScreen(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    @Override
    public Scene createScene() {
        AppContext context = AppContext.getInstance();
        StackPane root = new StackPane();
        root.prefWidthProperty().bind(context.getScreenWidthPropertyProperty());
        root.prefHeightProperty().bind(context.getScreenHeightPropertyProperty());

        // Main Background
        Image backgroundImage = new Image(getClass().getResource(SHARED_RESOURCE_FOLDER + "/bg.png").toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitHeightProperty().bind(root.heightProperty());
        backgroundImageView.fitWidthProperty().bind(root.widthProperty());

        // Sub Background
        Image containerImage = new Image(getClass().getResource(LEVEL_MENU_ASSETS_FOLDER + "/bg.png").toExternalForm());
        ImageView containerImageView = new ImageView(containerImage);
        containerImageView.fitHeightProperty().bind(root.heightProperty().multiply(0.8));
        containerImageView.fitWidthProperty().bind(root.widthProperty().multiply(0.8));

        // Buttons
        ImageButton level1Button = new ImageButton(LEVEL_MENU_ASSETS_FOLDER + "/level1.png");
        ImageButton level2Button = new ImageButton(LEVEL_MENU_ASSETS_FOLDER + "/level2.png");
        ImageButton level3Button = new ImageButton(LEVEL_MENU_ASSETS_FOLDER + "/level3.png");
        ImageButton level4Button = new ImageButton(LEVEL_MENU_ASSETS_FOLDER + "/level4.png");

        List<ImageButton> levelButtons = Arrays.asList(level1Button, level2Button, level3Button, level4Button);
        levelButtons.forEach(b -> {
            b.setPreserveRatio(true);
        });

        level1Button.setOnMouseClicked(e -> navigationHandler.startLevel(LevelType.LEVEL_ONE));
        level2Button.setOnMouseClicked(e -> navigationHandler.startLevel(LevelType.LEVEL_TWO));
        level3Button.setOnMouseClicked(e -> navigationHandler.startLevel(LevelType.LEVEL_THREE));
        level4Button.setOnMouseClicked(e -> navigationHandler.startLevel(LevelType.LEVEL_FOUR));

        HBox mainButtonsPane = new HBox(30, level1Button, level2Button, level3Button, level4Button);
        mainButtonsPane.setAlignment(Pos.CENTER);
        mainButtonsPane.setPadding(new Insets(80, 40, 0, 40));
        mainButtonsPane.prefWidthProperty().bind(containerImageView.fitWidthProperty().subtract(80));

        levelButtons.forEach(button ->
                button.fitWidthProperty().bind(
                        containerImageView.fitWidthProperty()
                                .subtract(80)
                                .divide(levelButtons.size())
                                .subtract(30)
                                .multiply(0.8)
                )
        );

        // Back Button
        ImageButton backButton = new ImageButton(SHARED_RESOURCE_FOLDER + "/backButton.png");
        backButton.setPreserveRatio(true);
        backButton.setFitWidth(50); // Set a fixed width for the back button
        backButton.setOnMouseClicked(e -> navigationHandler.showMenuScreen());
        StackPane.setMargin(backButton, new Insets(20, 0, 0, 20));
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

        root.getChildren().addAll(backgroundImageView, containerImageView, mainButtonsPane, backButton);

        return new Scene(root, context.getScreenWidthPropertyProperty().get(), context.getScreenHeightPropertyProperty().get());
    }
}
