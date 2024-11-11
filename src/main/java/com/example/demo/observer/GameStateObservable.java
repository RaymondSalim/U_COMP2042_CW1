package com.example.demo.observer;

public interface GameStateObservable {
    default void onResumeGame() {
    }

    ;

    default void onPauseGame() {
    }

    ;

    default void onLevelComplete() {
    }

    ;

    default void onLevelRestart() {
    }

    ;

    default void onGameWin() {
    }

    ;

    default void onGameOver() {
    }

    ;
}
