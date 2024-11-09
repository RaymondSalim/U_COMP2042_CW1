package com.example.demo.view;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.animation.Timeline;

public class PauseMenu {

    private final VBox menu;
    private final Button resumeButton;
    private boolean isPaused = false;

    public PauseMenu(Group root, Timeline timeline) {
        menu = new VBox();
        menu.setPrefWidth(400);
        menu.setPrefHeight(400);

        resumeButton = new Button("Resume");
        resumeButton.setOnAction(e -> togglePause(timeline));
        menu.getChildren().add(resumeButton);

        menu.setVisible(false);  // Start as hidden
        root.getChildren().add(menu);
    }

    public void togglePause(Timeline timeline) {
        isPaused = !isPaused;
        if (isPaused) {
            timeline.pause(); // Pause the game logic
        } else {
            timeline.play();  // Resume the game
        }
    }

    public VBox getMenu() {
        return menu;  // To allow access to the menu container in the view
    }

    public boolean isPaused() {
        return isPaused;
    }
}
