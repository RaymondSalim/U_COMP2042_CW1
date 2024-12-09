package com.example.demo.model;

import com.example.demo.JFXTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest extends JFXTestBase {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(3); // Initial health of 3
    }

    @Test
    void testInitialHealth() {
        assertEquals(3, player.getHealth(), "Initial health should be 3");
    }

    @Test
    void testTakeDamage() {
        player.takeDamage();
        assertEquals(2, player.getHealth(), "Health should decrease by 1 after taking damage");
    }

    @Test
    void testTakeDamageAtZeroHealth() {
        player.takeDamage();
        player.takeDamage();
        player.takeDamage();
        player.takeDamage();
        assertEquals(0, player.getHealth(), "Health should not go below 0");
    }

    @Test
    void testIsDestroyed() {
        player.takeDamage();
        assertFalse(player.isDestroyed(), "Player should not be destroyed with health > 0");

        player.takeDamage();
        player.takeDamage();
        assertTrue(player.isDestroyed(), "Player should be destroyed when health <= 0");
    }

    @Test
    void testIncrementKillCount() {
        player.incrementKillCount();
        player.incrementKillCount();
        assertEquals(2, player.getKillCount(), "Kill count should increment correctly");
    }

    @Test
    void testReset() {
        player.takeDamage();
        player.incrementKillCount();
        player.reset();
        assertEquals(3, player.getHealth(), "Health should reset to initial value");
        assertEquals(0, player.getKillCount(), "Kill count should reset to 0");
    }
}