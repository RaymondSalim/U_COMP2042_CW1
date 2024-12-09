package com.example.demo.controller;

import com.example.demo.JFXTestBase;
import com.example.demo.utils.NavigationHandler;
import com.example.demo.view.overlays.GameOverOverlay;
import com.example.demo.view.overlays.LevelCompleteOverlay;
import com.example.demo.view.overlays.PauseMenuOverlay;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;


class UIControllerTest extends JFXTestBase {

    @Mock
    private Stage mockStage;
    @Mock
    private NavigationHandler mockNavigationHandler;

    private UIController uiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uiController = new UIController(mockStage, mockNavigationHandler);
    }

    @Test
    void testShowMenuScreen() {
        uiController.showMenuScreen();
        verify(mockStage).setScene(any());
        verify(mockStage).show();
    }

    @Test
    void testShowLevelSelectScreen() {
        uiController.showLevelSelectScreen();
        verify(mockStage).setScene(any());
        verify(mockStage).show();
    }

    @Test
    void testShowSettingsScreen() {
        uiController.showSettingsScreen();
        verify(mockStage).setScene(any());
        verify(mockStage).show();
    }

    @Test
    void testShowCreditsScreen() {
        uiController.showCreditsScreen();
        verify(mockStage).setScene(any());
        verify(mockStage).show();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testAddOverlayToLayout() {
        ObservableList<Node> mockList = (ObservableList<Node>) mock(ObservableList.class);
        StackPane mockStackPane = mock(StackPane.class);
        when(mockStackPane.getChildren()).thenReturn(mockList);

        Pane mockPane = mock(Pane.class);
        PauseMenuOverlay mockPauseMenuOverlay = mock(PauseMenuOverlay.class);
        when(mockPauseMenuOverlay.getPane()).thenReturn(mockPane);

        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);
        when(mockGameOverOverlay.getPane()).thenReturn(mockPane);

        LevelCompleteOverlay mockLevelCompleteOverlay = mock(LevelCompleteOverlay.class);
        when(mockLevelCompleteOverlay.getPane()).thenReturn(mockPane);

        uiController.setPauseMenu(mockPauseMenuOverlay);
        uiController.setGameOver(mockGameOverOverlay);
        uiController.setLevelCompleteOverlay(mockLevelCompleteOverlay);

        uiController.addOverlayToLayout(mockStackPane);

        verify(mockStackPane).getChildren();
        verify(mockPauseMenuOverlay).getPane();
        verify(mockGameOverOverlay).getPane();
        verify(mockLevelCompleteOverlay).getPane();
        verify(mockList).addAll(mockPane, mockPane, mockPane);
    }

    @Test
    void testShowGameOverOverlay() {
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);
        uiController.setGameOver(mockGameOverOverlay);
        uiController.showGameOverOverlay();

        verify(mockGameOverOverlay).show();
    }

    @Test
    void testHideGameOverOverlay() {
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);
        uiController.setGameOver(mockGameOverOverlay);
        uiController.hideGameOverOverlay();

        verify(mockGameOverOverlay).hide();
    }

    @Test
    void testShowLevelCompleteOverlay() {
        int expected_score = 500;
        int expected_star = 3;

        LevelCompleteOverlay mockLevelCompleteOverlay = mock(LevelCompleteOverlay.class);
        uiController.setLevelCompleteOverlay(mockLevelCompleteOverlay);
        uiController.showLevelCompleteOverlay(expected_score, expected_star);

        verify(mockLevelCompleteOverlay).setStarCount(expected_star);
        verify(mockLevelCompleteOverlay).setScore(expected_score);
        verify(mockLevelCompleteOverlay).show();
    }

    @Test
    void testHideLevelCompleteOverlay() {
        LevelCompleteOverlay mockLevelCompleteOverlay = mock(LevelCompleteOverlay.class);
        uiController.setLevelCompleteOverlay(mockLevelCompleteOverlay);
        uiController.hideLevelCompleteOverlay();

        verify(mockLevelCompleteOverlay).hide();
    }

    @Test
    void testHideOverlays() {
        GameOverOverlay mockGameOverOverlay = mock(GameOverOverlay.class);
        uiController.setGameOver(mockGameOverOverlay);
        LevelCompleteOverlay mockLevelCompleteOverlay = mock(LevelCompleteOverlay.class);
        uiController.setLevelCompleteOverlay(mockLevelCompleteOverlay);
        PauseMenuOverlay mockPauseMenuOverlay = mock(PauseMenuOverlay.class);
        uiController.setPauseMenu(mockPauseMenuOverlay);
        uiController.hideOverlays();

        verify(mockPauseMenuOverlay).hide();
        verify(mockLevelCompleteOverlay).hide();
        verify(mockLevelCompleteOverlay).hide();
    }
}