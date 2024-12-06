package com.example.demo.audio;

import java.util.HashMap;
import java.util.Map;

public class AudioFiles {
    private static final Map<AudioEnum, String> audioFilePaths = new HashMap<>();

    static {
        audioFilePaths.put(AudioEnum.BG_MUSIC, "/com/example/demo/audio/bgMusic.mp3");

        audioFilePaths.put(AudioEnum.BUTTON_CLICK, "/com/example/demo/audio/buttonClick.wav");
        audioFilePaths.put(AudioEnum.CHECKBOX_TOGGLE, "/com/example/demo/audio/checkbox.wav");
        audioFilePaths.put(AudioEnum.PLAYER_SHOOT, "/com/example/demo/audio/shoot.wav");

        audioFilePaths.put(AudioEnum.STAR_1, "/com/example/demo/audio/star1.wav");
        audioFilePaths.put(AudioEnum.STAR_2, "/com/example/demo/audio/star2.wav");
        audioFilePaths.put(AudioEnum.STAR_3, "/com/example/demo/audio/star3.wav");
    }

    public static String getAudioFilePath(AudioEnum key) {
        return audioFilePaths.get(key);
    }

    public static Map<AudioEnum, String> getAudioFilePaths() {
        return audioFilePaths;
    }
}