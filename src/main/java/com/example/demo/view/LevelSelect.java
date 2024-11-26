package com.example.demo.view;

import com.example.demo.enums.LevelType;
import com.example.demo.view.base.NavigationHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LevelSelect {
    private final NavigationHandler navigationHandler;

    public LevelSelect(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public Scene createScene() {
        Button level1Button = new Button("Level 1");
        Button level2Button = new Button("Level 2");
        Button backButton = new Button("Back to Menu");

        level1Button.setOnAction(e -> navigationHandler.startLevel(LevelType.LEVEL_ONE));
        level2Button.setOnAction(e -> navigationHandler.startLevel(LevelType.LEVEL_TWO));
        backButton.setOnAction(e -> navigationHandler.showMenuScreen());

        VBox layout = new VBox(20, level1Button, level2Button, backButton);
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout, 800, 600);
    }
}
