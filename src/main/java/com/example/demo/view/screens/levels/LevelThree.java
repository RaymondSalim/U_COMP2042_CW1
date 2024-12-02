package com.example.demo.view.screens.levels;

import com.example.demo.view.base.LevelView;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class LevelThree extends LevelView {
    private static final String BACKGROUND_IMAGE_NAME_1 = "/com/example/demo/images/level/bg3.png";
    private static final String BACKGROUND_IMAGE_NAME_2 = "/com/example/demo/images/level/bg3r.png";

    public LevelThree(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);

        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_1).toExternalForm());
        Image backgroundImage2 = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_2).toExternalForm());

        this.initializeBackground(backgroundImage, backgroundImage2);
    }
}
