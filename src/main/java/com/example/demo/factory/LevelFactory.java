package com.example.demo.factory;

import com.example.demo.enums.LevelType;
import com.example.demo.model.LevelFour;
import com.example.demo.model.LevelOne;
import com.example.demo.model.base.LevelParent;

public class LevelFactory {
    public static LevelParent createLevel(LevelType levelType) {
        return switch (levelType) {
            case LEVEL_ONE -> new LevelOne();
            case LEVEL_TWO -> new LevelFour(); // TODO!
            case LEVEL_THREE -> new LevelOne(); // TODO!
            case LEVEL_FOUR -> new LevelFour();
        };
    }
}
