package com.example.demo.view;

import com.example.demo.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Settings {
    private final Stage stage;
    private final GameController gameController;

    public Settings(Stage stage, GameController gameController) {
        this.stage = stage;
        this.gameController = gameController;
    }

    public void show() {
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> new MenuScreen(stage, gameController).show());

        VBox layout = new VBox(20, backButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
