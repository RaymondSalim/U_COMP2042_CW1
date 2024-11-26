package com.example.demo.view.base;

import com.example.demo.view.RollingBackground;
import com.example.demo.view.objects.HeartDisplay;
import javafx.scene.Group;

public abstract class LevelView {
    private static final double HEART_DISPLAY_X_POSITION = 25;
    private static final double HEART_DISPLAY_Y_POSITION = 25;

    private final Group root;
    private final HeartDisplay heartDisplay;
    private RollingBackground rollingBackground;
    // TODO! Implement rolling background

    public LevelView(Group root, int heartsToDisplay) {
        this.root = root;
        this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
    }

    public void reset() {
        this.heartDisplay.resetHearts();
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
}
