package com.example.demo.audio;

import com.example.demo.context.AppContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AudioManager {
    private static AudioManager instance;
    private final Map<AudioEnum, Queue<MediaPlayer>> soundEffectPools = new HashMap<>();
    private MediaPlayer backgroundMusicPlayer;

    private AudioManager() {
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    // Load a sound effect into the pool
    public void loadSoundEffect(AudioEnum audioKey) {
        String filePath = AudioFiles.getAudioFilePath(audioKey);
        if (filePath == null) {
            System.err.println("Audio file not found for key: " + audioKey);
            return;
        }
        Media sound = new Media(getClass().getResource(filePath).toExternalForm());
        Queue<MediaPlayer> pool = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            pool.add(createMediaPlayer(sound));
        }
        soundEffectPools.put(audioKey, pool);
    }

    // Play a sound effect
    public void playSoundEffect(AudioEnum audioKey, boolean loop) {
        Queue<MediaPlayer> pool = soundEffectPools.get(audioKey);
        if (pool == null) {
            loadSoundEffect(audioKey);
            pool = soundEffectPools.get(audioKey);
        }

        if (pool != null && !pool.isEmpty()) {
            MediaPlayer player = pool.poll();
            if (player != null) {
                player.stop();
                if (loop) {
                    player.setCycleCount(MediaPlayer.INDEFINITE);
                } else {
                    player.setCycleCount(1);
                }
                Queue<MediaPlayer> finalPool = pool;
                player.setOnEndOfMedia(() -> {
                    player.stop();
                    finalPool.offer(player);
                });
                player.play();
            }
        }
    }

    public void stopSoundEffect(AudioEnum audioKey) {
        Queue<MediaPlayer> pool = soundEffectPools.get(audioKey);
        if (pool != null) {
            for (MediaPlayer player : pool) {
                player.stop();
            }
        }
    }

    public void setBackgroundMusic(AudioEnum audioKey) {
        String filePath = AudioFiles.getAudioFilePath(audioKey);
        if (filePath == null) {
            System.err.println("Audio file not found for key: " + audioKey);
            return;
        }
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        Media music = new Media(getClass().getResource(filePath).toExternalForm());
        backgroundMusicPlayer = new MediaPlayer(music);
        backgroundMusicPlayer.volumeProperty().bind(AppContext.getInstance().volumeProperty().divide(100.0).multiply(0.5));
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void playBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    public void preloadSounds() {
        new Thread(() -> {
            for (AudioEnum key : AudioFiles.getAudioFilePaths().keySet()) {
                loadSoundEffect(key);
            }
        }).start();
    }

    private MediaPlayer createMediaPlayer(Media sound) {
        AppContext context = AppContext.getInstance();
        MediaPlayer player = new MediaPlayer(sound);
        player.volumeProperty().bind(context.volumeProperty().divide(100.0));
        player.setCycleCount(1);
        return player;
    }
}