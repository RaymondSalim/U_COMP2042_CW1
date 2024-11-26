package com.example.demo.view.screens;

import com.example.demo.view.base.NavigationHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Settings {
    private final NavigationHandler navigationHandler;

    public Settings(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public Scene createScene() {
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> navigationHandler.showMenuScreen());

        VBox layout = new VBox(20, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 800, 600);
    }
}
