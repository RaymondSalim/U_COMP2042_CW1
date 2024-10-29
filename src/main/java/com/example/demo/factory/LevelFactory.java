package com.example.demo.factory;

import com.example.demo.enums.LevelType;
import com.example.demo.model.LevelTwo;
import com.example.demo.model.base.LevelParent;
import com.example.demo.model.LevelOne;

public class LevelFactory {
    public static LevelParent createLevel(LevelType levelType, double height, double width) {
        switch (levelType) {
            case LEVEL_ONE:
                return new LevelOne(height, width);
            case LEVEL_TWO:
                return new LevelTwo(height, width);
            default:
                throw new IllegalArgumentException("Unknown level: " + levelType);
        }
    }
}
