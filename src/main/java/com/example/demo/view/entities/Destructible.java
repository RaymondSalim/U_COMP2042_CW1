package com.example.demo.view.entities;

/**
 * Interface defining destructible behavior.
 *
 * <p>Provides methods for handling damage and determining the destroyed state.</p>
 */
public interface Destructible {
    /**
     * Applies damage to the object.
     */
    void takeDamage();

    /**
     * Destroys the current object.
     */
    void destroy();

}
