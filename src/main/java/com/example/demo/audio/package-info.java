/**
 * Provides classes and enumerations for managing audio resources in the application.
 *
 * <p>
 * This package includes the following components:
 * </p>
 * <ul>
 *     <li>{@link com.example.demo.audio.AudioEnum} - Enumerates the different audio types used in the application.</li>
 *     <li>{@link com.example.demo.audio.AudioFiles} - Maps {@link com.example.demo.audio.AudioEnum} keys to their corresponding file paths.</li>
 *     <li>{@link com.example.demo.audio.AudioManager} - A singleton class responsible for handling audio playback, including sound effects and background music.</li>
 * </ul>
 *
 * <p>
 * The package is designed to provide a simple and efficient way to manage audio resources,
 * leveraging JavaFX's {@link javafx.scene.media.MediaPlayer} for playback.
 * It supports features such as sound effect pooling, background music looping, and volume control through
 * integration with the application's {@link com.example.demo.context.AppContext}.
 * </p>
 */
package com.example.demo.audio;