package com.example.demo.controller;

import com.example.demo.JFXTestBase;
import com.example.demo.enums.LevelType;
import com.example.demo.model.base.LevelParent;
import com.example.demo.view.levels.LevelView;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class LevelControllerTest extends JFXTestBase {

    @Mock
    private Stage mockStage;
    @Mock
    private UIController mockUIController;
    @Mock
    private LevelParent mockLevelParent;
    @Mock
    private LevelView mockLevelView;

    private LevelController levelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        levelController = new LevelController(mockStage, mockUIController) {
            @Override
            protected LevelParent createLevel(LevelType levelType) {
                return mockLevelParent;
            }
        };

        levelController.setCurrentLevelView(mockLevelView);
        when(mockLevelParent.getLevelView()).thenReturn(mockLevelView);
        when(mockLevelView.getRoot()).thenReturn(new Group());
    }

    @Test
    void testGoToLevel() {
        LevelType levelType = LevelType.LEVEL_ONE;
        levelController.goToLevel(levelType);

        verify(mockLevelParent).addGameStateObserver(levelController);
        verify(mockUIController).addOverlayToLayout(any());
        verify(mockStage).setScene(any());
    }

    @Test
    void testStartGame() {
        LevelParent mockLevelParent = mock(LevelParent.class);
        levelController.setCurrentLevel(mockLevelParent);

        levelController.startGame();
        verify(mockUIController).hideOverlays();
    }

    @Test
    void testPauseGame() {
        Timeline mockTimeline = mock(Timeline.class);
        LevelParent mockLevelParent = mock(LevelParent.class);

        levelController.setGameTimeline(mockTimeline);
        levelController.setCurrentLevel(mockLevelParent);
        levelController.pauseGame();

        verify(mockTimeline).pause();
        verify(mockLevelParent).pause();
    }

    @Test
    void testResumeGame() {
        Timeline mockTimeline = mock(Timeline.class);
        LevelParent mockLevelParent = mock(LevelParent.class);

        levelController.setGameTimeline(mockTimeline);
        levelController.setCurrentLevel(mockLevelParent);
        levelController.resumeGame();

        verify(mockTimeline).play();
        verify(mockLevelParent).resume();
    }
}