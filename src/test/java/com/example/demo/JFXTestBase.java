package com.example.demo;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
//import org.testfx.framework.junit5.ApplicationTest;

//public abstract class JFXTestBase extends ApplicationTest {
//    @BeforeAll
//    static void setupJavaFX() {
//        new Thread(() -> Application.launch(DummyApp.class)).start();
//    }
//
//    public static class DummyApp extends Application {
//        @Override
//        public void start(Stage stage) {}
//    }
//}

public abstract class JFXTestBase {
    @BeforeAll
    public static void setupJavaFX() {
        // Initialize JavaFX runtime once for all tests
        if (!Platform.isFxApplicationThread()) {
            try {
                Platform.startup(() -> {
                });
            } catch (Exception e) {
            }
        }
    }
}