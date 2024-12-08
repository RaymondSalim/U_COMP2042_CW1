package com.example.demo.view.screens;

import javafx.scene.Scene;

/**
 * Represents a general screen in the application.
 * All specific screens must implement this interface.
 */
public interface Screen {

    /**
     * Creates and returns the JavaFX Scene for this screen.
     *
     * @return the JavaFX Scene object.
     */
    Scene createScene();
}
