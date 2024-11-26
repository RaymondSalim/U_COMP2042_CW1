package com.example.demo.view.screens;

import com.example.demo.view.base.LevelView;
import com.example.demo.view.objects.ShieldImage;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class LevelTwo extends LevelView {
    private static final String BACKGROUND_IMAGE_NAME_1 = "/com/example/demo/images/level/bg2.png";
    private static final String BACKGROUND_IMAGE_NAME_2 = "/com/example/demo/images/level/bg2r.png";

    private static final int SHIELD_X_POSITION = 1150;
    private static final int SHIELD_Y_POSITION = 500;
    private final ShieldImage shieldImage;

    public LevelTwo(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);
        this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
        addImagesToRoot();

        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_1).toExternalForm());
        Image backgroundImage2 = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME_2).toExternalForm());

        this.initializeRollingBackground(backgroundImage, backgroundImage2);

    }

    private void addImagesToRoot() {
        super.getRoot().getChildren().addAll(shieldImage);
    }

    public void showShield() {
        shieldImage.showShield();
    }

    public void hideShield() {
        shieldImage.hideShield();
    }

}
