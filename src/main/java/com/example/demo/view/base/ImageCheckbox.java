package com.example.demo.view.base;

import javafx.scene.image.Image;

public class ImageCheckbox extends ImageButton {
    private static final String CHECKED_IMAGE_NAME = "/com/example/demo/images/settings/checkedBtn.png";
    private static final String UNCHECKED_IMAGE_NAME = "/com/example/demo/images/settings/uncheckedBtn.png";
    private final Image checkedImage;
    private final Image uncheckedImage;
    private boolean isChecked;

    public ImageCheckbox(boolean initialState) {
        super(initialState ? CHECKED_IMAGE_NAME : UNCHECKED_IMAGE_NAME);

        this.checkedImage = new Image(getClass().getResource(CHECKED_IMAGE_NAME).toExternalForm());
        this.uncheckedImage = new Image(getClass().getResource(UNCHECKED_IMAGE_NAME).toExternalForm());
        this.isChecked = initialState;

        this.setOnMouseClicked(event -> toggle());
    }

    public void toggle() {
        isChecked = !isChecked;
        updateImage();
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
        updateImage();
    }

    private void updateImage() {
        this.setImage(isChecked ? checkedImage : uncheckedImage);
    }
}