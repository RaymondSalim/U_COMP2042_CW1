package com.example.demo.context;

import com.example.demo.observer.ScreenSizeObservable;

public class AppContext extends ScreenSizeObservable {
    private static AppContext instance;

    private int screenHeight;
    private int screenWidth;

    private AppContext() {

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
}
