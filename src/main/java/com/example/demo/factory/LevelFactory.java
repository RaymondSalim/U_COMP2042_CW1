package com.example.demo.factory;

import com.example.demo.enums.LevelType;
import com.example.demo.model.LevelOne;
import com.example.demo.model.LevelTwo;
import com.example.demo.model.base.LevelParent;

public class LevelFactory {
    public static LevelParent createLevel(LevelType levelType) {
        return switch (levelType) {
            case LEVEL_ONE -> new LevelOne();
            case LEVEL_TWO -> new LevelTwo();
        };
    }
}
