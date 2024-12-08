package com.example.demo.factory;

import com.example.demo.enums.LevelType;
import com.example.demo.model.LevelFour;
import com.example.demo.model.LevelOne;
import com.example.demo.model.LevelThree;
import com.example.demo.model.LevelTwo;
import com.example.demo.model.base.LevelParent;

/**
 * Factory class for creating levels in the game.
 * <p>
 * The {@code LevelFactory} provides a method to create instances of {@link LevelParent}
 * based on the provided {@link LevelType}. This ensures that the creation logic
 * for levels is centralized and easily maintainable.
 * </p>
 */
public class LevelFactory {

    /**
     * Creates a new level instance based on the specified {@link LevelType}.
     *
     * @param levelType the type of level to create.
     * @return a new instance of a class extending {@link LevelParent}, representing the desired level.
     * @throws IllegalArgumentException if the provided {@code levelType} is null or not recognized.
     */
    public static LevelParent createLevel(LevelType levelType) {
        if (levelType == null) {
            throw new IllegalArgumentException("LevelType cannot be null.");
        }

        return switch (levelType) {
            case LEVEL_ONE -> new LevelOne();
            case LEVEL_TWO -> new LevelTwo();
            case LEVEL_THREE -> new LevelThree();
            case LEVEL_FOUR -> new LevelFour();
        };
    }
}
