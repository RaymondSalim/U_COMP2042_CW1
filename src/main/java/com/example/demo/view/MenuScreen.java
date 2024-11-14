package com.example.demo.view;

import com.example.demo.view.base.NavigationHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuScreen {
    private final NavigationHandler navigationHandler;

    public MenuScreen(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public Scene createScene() {
        Button playButton = new Button("Play");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        playButton.setOnAction(e -> navigationHandler.showLevelSelectScreen());
        settingsButton.setOnAction(e -> navigationHandler.showSettingsScreen());
        exitButton.setOnAction(e -> navigationHandler.showMenuScreen());

        VBox layout = new VBox(20, playButton, settingsButton, exitButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 800, 600);
    }
}
