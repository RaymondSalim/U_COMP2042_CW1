/**
 * Provides the application-wide context for managing global settings and properties.
 *
 * <p>
 * This package contains the {@link com.example.demo.context.AppContext} class, which serves as a singleton
 * for managing global application state. It provides observable properties for screen dimensions,
 * target frames per second (FPS), and audio volume, enabling dynamic binding and updates across the application.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Global settings management, including screen dimensions and target FPS.</li>
 *     <li>Audio volume property for integration with the application's audio system.</li>
 *     <li>Singleton pattern ensures a single shared instance.</li>
 *     <li>Uses JavaFX properties for dynamic binding and observability.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 *     AppContext context = AppContext.getInstance();
 *     context.setTargetFPS(60);
 *     double volume = context.getVolume();
 * </pre>
 */
package com.example.demo.context;