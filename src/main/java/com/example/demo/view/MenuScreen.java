package com.example.demo.view;

import com.example.demo.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuScreen {
    private final Stage stage;
    private final GameController gameController;

    public MenuScreen(Stage stage, GameController gameController) {
        this.stage = stage;
        this.gameController = gameController;
    }

    public void show() {
        Button playButton = new Button("Play");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        playButton.setOnAction(e -> showLevelSelect());
        settingsButton.setOnAction(e -> showSettingsScreen());
        exitButton.setOnAction(e -> stage.close());

        VBox layout = new VBox(20, playButton, settingsButton, exitButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showLevelSelect() {
        LevelSelect levelSelect = new LevelSelect(stage, gameController);
        levelSelect.show();
    }

    private void showSettingsScreen() {
        Settings settings = new Settings(stage, gameController);
        settings.show();
    }
}
