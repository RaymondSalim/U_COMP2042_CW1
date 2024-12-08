package com.example.demo.view.levels;

import com.example.demo.context.AppContext;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents the visual and UI configuration for Level One, including unique elements like tutorial messages.
 */
public class LevelOne extends LevelView {
    private static final String BACKGROUND_IMAGE_NAME_1 = "/com/example/demo/images/level/bg1.png";
    private static final String BACKGROUND_IMAGE_NAME_2 = "/com/example/demo/images/level/bg1r.png";

    private final StackPane messageContainer;
    private final Text messageText;

    /**
     * Constructs a {@code LevelOne} instance with its unique background and UI elements.
     *
     * @param root            the {@link Group} to which all level elements are added.
     * @param heartsToDisplay the number of hearts to display for the player's health.
     */
    public LevelOne(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);

        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_1).toExternalForm());
        Image backgroundImage2 = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_2).toExternalForm());

        this.initializeBackground(backgroundImage, backgroundImage2);

        AppContext context = AppContext.getInstance();

        Font font = Font.loadFont(getClass().getResource("/com/example/demo/fonts/Audiowide/Audiowide.ttf").toExternalForm(), 24);
        messageText = new Text();
        messageText.setFont(font);
        messageText.setFill(Color.WHITE);
        messageText.setStyle("-fx-font-size: 28px;");
        messageText.wrappingWidthProperty().bind(context.getScreenWidthPropertyProperty().multiply(0.8));
        StackPane.setAlignment(messageText, Pos.CENTER);

        BackgroundFill backgroundFill = new BackgroundFill(
                Color.rgb(0, 0, 0, 0.8),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );

        messageContainer = new StackPane();
        messageContainer.setBackground(new Background(backgroundFill));
        messageContainer.setVisible(false);
        messageContainer.prefWidthProperty().bind(context.getScreenWidthPropertyProperty());
        messageContainer.prefHeightProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.25));
        messageContainer.setLayoutX(0);
        messageContainer.layoutYProperty().bind(context.getScreenHeightPropertyProperty().multiply(0.75));
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setPadding(new Insets(20));
        messageContainer.getChildren().add(messageText);

        getRoot().getChildren().add(messageContainer);
    }

    /**
     * Displays a message on the screen during the tutorial phase.
     *
     * @param message the message to display.
     */
    public void displayMessage(String message) {
        messageText.setText(message);
        messageContainer.setVisible(true);
    }

    /**
     * Hides the tutorial message from the screen.
     */
    public void deleteMessage() {
        messageText.setText("");
        messageContainer.setVisible(false);
    }
}