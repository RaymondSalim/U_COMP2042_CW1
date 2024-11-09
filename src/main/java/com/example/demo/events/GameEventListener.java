package com.example.demo.events;

public interface GameEventListener {
    void onGamePaused();
    void onGameResumed();
    void onGameOver();
}
