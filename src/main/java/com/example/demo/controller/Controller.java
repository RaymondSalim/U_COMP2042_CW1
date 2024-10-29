package com.example.demo.controller;

import com.example.demo.enums.LevelType;
import com.example.demo.factory.LevelFactory;
import com.example.demo.model.base.LevelParent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private final Stage stage;

    public Controller(Stage stage) {
        this.stage = stage;
    }

    public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        stage.show();
        goToLevel(LevelType.LEVEL_ONE);
    }

    private void goToLevel(LevelType levelType) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            LevelParent myLevel = LevelFactory.createLevel(levelType, stage.getHeight(), stage.getWidth());
            myLevel.addObserver(this);
            Scene scene = myLevel.initializeScene();
            stage.setScene(scene);
            myLevel.startGame();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        try {
            goToLevel((LevelType) arg1);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(e.getClass().toString());
            alert.show();
        }
    }

}
