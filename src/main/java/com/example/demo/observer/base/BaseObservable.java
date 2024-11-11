package com.example.demo.observer.base;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseObservable<T> {
    protected final List<T> observers = new ArrayList<>();

    public void addGameStateObserver(T observer) {
        observers.add(observer);
    }

    public void removeGameStateObserver(T observer) {
        observers.remove(observer);
    }
}
