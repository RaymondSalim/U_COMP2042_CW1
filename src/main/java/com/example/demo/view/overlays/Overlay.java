package com.example.demo.view.overlays;

import javafx.scene.layout.Pane;

public interface Overlay {
    Pane getPane();

    void show();

    void hide();
}
