package com.example.demo.model;

public class Player {
    private final int initialHealth;
    private int health;
    private int killCount;

    public Player(int initialHealth) {
        this.health = initialHealth;
        this.initialHealth = initialHealth;
        this.killCount = 0;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage() {
        if (health > 0) {
            health--;
        }
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public int getKillCount() {
        return killCount;
    }

    public void incrementKillCount() {
        killCount++;
    }

    public void reset() {
        this.health = initialHealth;
        this.killCount = 0;
    }
}
