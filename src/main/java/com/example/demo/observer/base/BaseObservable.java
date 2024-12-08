package com.example.demo.observer.base;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic base class for implementing the observer pattern.
 *
 * @param <T> the type of observer.
 */
public abstract class BaseObservable<T> {

    /**
     * A list of registered observers.
     */
    protected final List<T> observers = new ArrayList<>();

    /**
     * Adds an observer to the list.
     *
     * @param observer the observer to add.
     */
    public void addGameStateObserver(T observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list.
     *
     * @param observer the observer to remove.
     */
    public void removeGameStateObserver(T observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of a change.
     * <p>
     * This method should be overridden in subclasses to provide specific notification logic.
     * </p>
     */
    public void notifyObservers() {
    }
}
