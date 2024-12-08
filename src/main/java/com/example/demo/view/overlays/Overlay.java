package com.example.demo.view.overlays;

import javafx.scene.layout.Pane;

/**
 * Interface for all overlay components in the game.
 * <p>
 * An overlay is a UI component that can appear over the game's main view, such as menus or status screens.
 */
public interface Overlay {

    /**
     * Returns the pane that contains the overlay.
     *
     * @return the {@link Pane} representing the overlay.
     */
    Pane getPane();

    /**
     * Shows the overlay, making it visible.
     */
    void show();

    /**
     * Hides the overlay, making it invisible.
     */
    void hide();
}
