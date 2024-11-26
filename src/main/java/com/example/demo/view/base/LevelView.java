package com.example.demo.view.base;

import com.example.demo.view.RollingBackground;
import com.example.demo.view.objects.HeartDisplay;
import javafx.scene.Group;
import javafx.scene.image.Image;

public abstract class LevelView {
    private static final double HEART_DISPLAY_X_POSITION = 25;
    private static final double HEART_DISPLAY_Y_POSITION = 25;

    private final Group root;
    private final HeartDisplay heartDisplay;
    private RollingBackground rollingBackground;

    public LevelView(Group root, int heartsToDisplay) {
        this.root = root;
        this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
    }

    protected void initializeRollingBackground(Image... backgroundImages) {
        this.rollingBackground = new RollingBackground(backgroundImages);
        root.getChildren().add(rollingBackground);
        rollingBackground.start();
    }

    public void reset() {
        this.heartDisplay.resetHearts();
        this.rollingBackground.restart();
    }

    public void showHeartDisplay() {
        this.root.getChildren().add(heartDisplay.getContainer());
    }

    public Group getRoot() {
        return root;
    }

    public void removeHearts(int heartsRemaining) {
        int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
        for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
            heartDisplay.removeHeart();
        }
    }

    public void pause() {
        this.rollingBackground.stop();
    }

    public void resume() {
        this.rollingBackground.start();
    }
}
