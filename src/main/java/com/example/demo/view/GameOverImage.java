package com.example.demo.view;

import com.example.demo.context.AppContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameOverImage extends ImageView {

    private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

    public GameOverImage() {
        AppContext context = AppContext.getInstance();
        setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()));

        setFitWidth(context.getScreenWidth() * 0.5);
        setFitHeight(context.getScreenHeight());
        setPreserveRatio(true);
    }
}
