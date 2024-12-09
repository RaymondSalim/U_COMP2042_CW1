package com.example.demo.factory;

import com.example.demo.JFXTestBase;
import com.example.demo.enums.LevelType;
import com.example.demo.model.LevelFour;
import com.example.demo.model.LevelOne;
import com.example.demo.model.LevelThree;
import com.example.demo.model.LevelTwo;
import com.example.demo.model.base.LevelParent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelFactoryTest extends JFXTestBase {

    @Test
    void createLevel_levelOne() {
        LevelParent level = LevelFactory.createLevel(LevelType.LEVEL_ONE);
        assertNotNull(level, "LevelOne instance should not be null.");
        assertTrue(level instanceof LevelOne, "Instance should be of type LevelOne.");
    }

    @Test
    void createLevel_levelTwo() {
        LevelParent level = LevelFactory.createLevel(LevelType.LEVEL_TWO);
        assertNotNull(level, "LevelTwo instance should not be null.");
        assertTrue(level instanceof LevelTwo, "Instance should be of type LevelTwo.");
    }

    @Test
    void createLevel_levelThree() {
        LevelParent level = LevelFactory.createLevel(LevelType.LEVEL_THREE);
        assertNotNull(level, "LevelThree instance should not be null.");
        assertTrue(level instanceof LevelThree, "Instance should be of type LevelThree.");
    }

    @Test
    void createLevel_levelFour() {
        LevelParent level = LevelFactory.createLevel(LevelType.LEVEL_FOUR);
        assertNotNull(level, "LevelFour instance should not be null.");
        assertTrue(level instanceof LevelFour, "Instance should be of type LevelFour.");
    }

    @Test
    void createLevel_nullLevelType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                LevelFactory.createLevel(null));
        assertEquals("LevelType cannot be null.", exception.getMessage(),
                "Exception message should match.");
    }
}