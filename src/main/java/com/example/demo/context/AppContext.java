package com.example.demo.context;

public class AppContext {
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
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }
}
