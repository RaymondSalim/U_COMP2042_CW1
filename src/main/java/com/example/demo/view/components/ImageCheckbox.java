package com.example.demo.view.components;

import com.example.demo.audio.AudioEnum;
import com.example.demo.audio.AudioManager;
import javafx.scene.image.Image;

/**
 * A checkbox implemented as a toggleable button with visual feedback.
 */
public class ImageCheckbox extends ImageButton {
    private static final String CHECKED_IMAGE_NAME = "/com/example/demo/images/settings/checkedBtn.png";
    private static final String UNCHECKED_IMAGE_NAME = "/com/example/demo/images/settings/uncheckedBtn.png";
    private final Image checkedImage;
    private final Image uncheckedImage;
    private boolean isChecked;

    /**
     * Constructs an {@code ImageCheckbox} with the specified initial state.
     *
     * @param initialState {@code true} if the checkbox should start checked, {@code false} otherwise.
     */
    public ImageCheckbox(boolean initialState) {
        super(initialState ? CHECKED_IMAGE_NAME : UNCHECKED_IMAGE_NAME);

        this.checkedImage = new Image(getClass().getResource(CHECKED_IMAGE_NAME).toExternalForm());
        this.uncheckedImage = new Image(getClass().getResource(UNCHECKED_IMAGE_NAME).toExternalForm());
        this.isChecked = initialState;

        this.setOnMouseClicked(event -> toggle());
    }

    /**
     * Plays the toggle sound effect.
     */
    @Override
    protected void playAudio() {
        AudioManager manager = AudioManager.getInstance();
        manager.playSoundEffect(AudioEnum.CHECKBOX_TOGGLE, false);
    }

    /**
     * Toggles the checkbox state and updates its visual appearance.
     */
    public void toggle() {
        isChecked = !isChecked;
        updateImage();
    }

    /**
     * Checks whether the checkbox is currently checked.
     *
     * @return {@code true} if the checkbox is checked, {@code false} otherwise.
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Sets the checkbox state.
     *
     * @param checked {@code true} to set the checkbox as checked, {@code false} otherwise.
     */
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        updateImage();
    }

    /**
     * Updates the image displayed by the checkbox based on its state.
     */
    private void updateImage() {
        this.setImage(isChecked ? checkedImage : uncheckedImage);
    }
}