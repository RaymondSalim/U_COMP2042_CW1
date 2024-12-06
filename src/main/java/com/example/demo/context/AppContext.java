package com.example.demo.context;

import com.example.demo.observer.ScreenSizeObservable;
import javafx.beans.property.SimpleIntegerProperty;

public class AppContext extends ScreenSizeObservable {
    private static AppContext instance;

    private int screenHeight;
    private int screenWidth;
    private SimpleIntegerProperty targetFPS;

    private AppContext() {
        this.targetFPS = new SimpleIntegerProperty(60);
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public void setScreenSize(int height, int width) {
        this.screenHeight = height;
        this.screenWidth = width;
        this.notifyObservers(height, width);
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public SimpleIntegerProperty getTargetFPS() {
        return targetFPS;
    }

    public void setTargetFPS(int fps) {
        this.targetFPS.set(fps);
    }
}
