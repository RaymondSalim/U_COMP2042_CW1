package com.example.demo.context;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Singleton class that represents the global application context.
 * <p>
 * The {@code AppContext} class manages global settings and properties such as screen dimensions,
 * target frames per second (FPS), and audio volume. These settings are accessible throughout
 * the application and are implemented using JavaFX's observable properties for seamless
 * integration with the UI or other components.
 * </p>
 */
public class AppContext {
    /**
     * The single instance of {@code AppContext}.
     */
    private static AppContext instance;

    /**
     * Observable property for the screen width.
     */
    private final SimpleIntegerProperty screenWidthProperty;

    /**
     * Observable property for the screen height.
     */
    private final SimpleIntegerProperty screenHeightProperty;

    /**
     * Observable property for the target frames per second (FPS).
     */
    private final SimpleIntegerProperty targetFPS;

    /**
     * Observable property for the application's volume level.
     */
    private final SimpleDoubleProperty volume;

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private AppContext() {
        this.targetFPS = new SimpleIntegerProperty(60);
        this.volume = new SimpleDoubleProperty(50);
        this.screenWidthProperty = new SimpleIntegerProperty();
        this.screenHeightProperty = new SimpleIntegerProperty();
    }

    /**
     * Retrieves the singleton instance of {@code AppContext}.
     *
     * @return the singleton instance of {@code AppContext}.
     */
    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    /**
     * Retrieves the observable property for screen width.
     *
     * @return the {@link SimpleIntegerProperty} representing screen width.
     */
    public SimpleIntegerProperty getScreenWidthPropertyProperty() {
        return screenWidthProperty;
    }

    /**
     * Retrieves the observable property for screen height.
     *
     * @return the {@link SimpleIntegerProperty} representing screen height.
     */
    public SimpleIntegerProperty getScreenHeightPropertyProperty() {
        return screenHeightProperty;
    }

    /**
     * Retrieves the observable property for target FPS.
     *
     * @return the {@link SimpleIntegerProperty} representing target FPS.
     */
    public SimpleIntegerProperty getTargetFPS() {
        return targetFPS;
    }

    /**
     * Sets the target frames per second (FPS).
     *
     * @param fps the desired FPS value.
     */
    public void setTargetFPS(int fps) {
        this.targetFPS.set(fps);
    }

    /**
     * Retrieves the observable property for volume.
     *
     * @return the {@link SimpleDoubleProperty} representing volume.
     */
    public SimpleDoubleProperty volumeProperty() {
        return volume;
    }

    /**
     * Retrieves the current volume level.
     *
     * @return the current volume level as a {@code double}.
     */
    public double getVolume() {
        return volume.get();
    }
}
