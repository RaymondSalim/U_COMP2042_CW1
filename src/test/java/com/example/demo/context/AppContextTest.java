package com.example.demo.context;

import com.example.demo.JFXTestBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppContextTest extends JFXTestBase {

    private AppContext appContext;

    @BeforeEach
    void setUp() {
        appContext = AppContext.getInstance();
    }

    @Test
    void testSingletonInstance() {
        AppContext instance1 = AppContext.getInstance();
        AppContext instance2 = AppContext.getInstance();
        assertNotNull(instance1, "AppContext instance should not be null.");
        assertSame(instance1, instance2, "AppContext should be a singleton.");
    }

    @Test
    @Order(1)
    void testDefaultProperties() {
        assertEquals(60, appContext.getTargetFPS().get(), "Default FPS should be 60.");

        assertEquals(50.0, appContext.getVolume(), 0.01, "Default volume should be 50.");

        assertNotNull(appContext.getScreenWidthPropertyProperty(), "Screen width property should not be null.");
        assertNotNull(appContext.getScreenHeightPropertyProperty(), "Screen height property should not be null.");
    }

    @Test
    void testSetTargetFPS() {
        appContext.setTargetFPS(120);
        assertEquals(120, appContext.getTargetFPS().get(), "Target FPS should be updated to 120.");
    }

    @Test
    void testVolumeProperty() {
        SimpleDoubleProperty volumeProperty = appContext.volumeProperty();
        assertNotNull(volumeProperty, "Volume property should not be null.");
        assertEquals(50.0, volumeProperty.get(), 0.01, "Volume property default value should be 50.");

        // Update volume and verify
        volumeProperty.set(75.0);
        assertEquals(75.0, volumeProperty.get(), 0.01, "Volume property should be updated to 75.");
    }

    @Test
    void testScreenWidthProperty() {
        SimpleIntegerProperty screenWidthProperty = appContext.getScreenWidthPropertyProperty();
        assertNotNull(screenWidthProperty, "Screen width property should not be null.");

        // Update screen width and verify
        screenWidthProperty.set(1920);
        assertEquals(1920, screenWidthProperty.get(), "Screen width should be updated to 1920.");
    }

    @Test
    void testScreenHeightProperty() {
        SimpleIntegerProperty screenHeightProperty = appContext.getScreenHeightPropertyProperty();
        assertNotNull(screenHeightProperty, "Screen height property should not be null.");

        // Update screen height and verify
        screenHeightProperty.set(1080);
        assertEquals(1080, screenHeightProperty.get(), "Screen height should be updated to 1080.");
    }
}