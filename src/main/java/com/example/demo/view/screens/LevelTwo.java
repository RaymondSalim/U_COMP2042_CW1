package com.example.demo.view.screens;

import com.example.demo.view.base.LevelView;
import com.example.demo.view.objects.ShieldImage;
import javafx.scene.Group;

public class LevelTwo extends LevelView {
    private static final int SHIELD_X_POSITION = 1150;
    private static final int SHIELD_Y_POSITION = 500;
    private final ShieldImage shieldImage;

    public LevelTwo(Group root, int heartsToDisplay) {
        super(root, heartsToDisplay);
        this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
        addImagesToRoot();
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
