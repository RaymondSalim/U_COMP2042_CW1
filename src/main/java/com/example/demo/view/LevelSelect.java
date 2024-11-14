package com.example.demo.view;

import com.example.demo.controller.GameController;
import com.example.demo.enums.LevelType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelSelect {
    private final Stage stage;
    private final GameController gameController;

    public LevelSelect(Stage stage, GameController gameController) {
        this.stage = stage;
        this.gameController = gameController;
    }

    public void show() {
        Button level1Button = new Button("Level 1");
        Button level2Button = new Button("Level 2");
        Button backButton = new Button("Back to Menu");

        level1Button.setOnAction(e -> startLevel(LevelType.LEVEL_ONE));
        level2Button.setOnAction(e -> startLevel(LevelType.LEVEL_TWO));
        backButton.setOnAction(e -> new MenuScreen(stage, gameController).show());

        VBox layout = new VBox(20, level1Button, level2Button, backButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void startLevel(LevelType levelType) {
        gameController.goToLevel(levelType);
    }
}
