package com.example.demo.observer;

import com.example.demo.observer.base.BaseObservable;

public class ScreenSizeObservable extends BaseObservable<ScreenSizeObserver> {
    public void notifyObservers(int height, int width) {
        for (ScreenSizeObserver observer : observers) {
            observer.onScreenSizeChanged(height, width);
        }
    }
}
