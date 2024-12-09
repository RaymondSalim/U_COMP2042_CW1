package com.example.demo.controller;

import com.example.demo.JFXTestBase;
import com.example.demo.enums.LevelType;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class GameControllerTest extends JFXTestBase {
    @Mock
    private Stage mockStage;
    @Mock
    private UIController mockUIController;
    @Mock
    private LevelController mockLevelController;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(mockStage) {
            @Override
            protected UIController createUIController() {
                return mockUIController;
            }

            @Override
            protected LevelController createLevelController() {
                return mockLevelController;
            }
        };
    }

    @Test
    void testShowMenuScreen() {
        gameController.showMenuScreen();
        verify(mockUIController).showMenuScreen();
    }

    @Test
    void testExitGame() {
        gameController.exitGame();
        verify(mockStage).close();
    }

    @Test
    void testShowLevelSelectScreen() {
        gameController.showLevelSelectScreen();
        verify(mockUIController).showLevelSelectScreen();
    }

    @Test
    void testShowSettingsScreen() {
        gameController.showSettingsScreen();
        verify(mockUIController).showSettingsScreen();
    }

    @Test
    void testShowCreditsScreen() {
        gameController.showCreditsScreen();
        verify(mockUIController).showCreditsScreen();
    }

    @Test
    void testStartLevel() {
        LevelType levelType = LevelType.LEVEL_ONE;
        gameController.startLevel(levelType);
        verify(mockLevelController).goToLevel(levelType);
    }
}