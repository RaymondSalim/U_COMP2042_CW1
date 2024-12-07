package com.example.demo.context;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class AppContext {
    private static AppContext instance;

    private final SimpleIntegerProperty screenWidthProperty;
    private final SimpleIntegerProperty screenHeightProperty;
    private final SimpleIntegerProperty targetFPS;
    private final SimpleDoubleProperty volume;

    private AppContext() {
        this.targetFPS = new SimpleIntegerProperty(60);
        this.volume = new SimpleDoubleProperty(50);

        this.screenWidthProperty = new SimpleIntegerProperty();
        this.screenHeightProperty = new SimpleIntegerProperty();
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public SimpleIntegerProperty getScreenWidthPropertyProperty() {
        return screenWidthProperty;
    }

    public SimpleIntegerProperty getScreenHeightPropertyProperty() {
        return screenHeightProperty;
    }

    public SimpleIntegerProperty getTargetFPS() {
        return targetFPS;
    }

    public void setTargetFPS(int fps) {
        this.targetFPS.set(fps);
    }

    public SimpleDoubleProperty volumeProperty() {
        return volume;
    }

    public double getVolume() {
        return volume.get();
    }

    public void setVolume(double volume) {
        this.volume.set(volume);
    }
}
