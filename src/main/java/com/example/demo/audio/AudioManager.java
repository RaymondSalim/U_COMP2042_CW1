package com.example.demo.audio;

import com.example.demo.context.AppContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Singleton class for managing audio playback in the application.
 * Handles sound effects and background music using a pool of {@link MediaPlayer} instances.
 */
public class AudioManager {
    /**
     * Singleton instance of {@link AudioManager}.
     */
    private static AudioManager instance;

    /**
     * Map holding sound effect pools for each {@link AudioEnum}.
     */
    private final Map<AudioEnum, Queue<MediaPlayer>> soundEffectPools = new HashMap<>();

    /**
     * {@link MediaPlayer} for background music.
     */
    private MediaPlayer backgroundMusicPlayer;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private AudioManager() {
    }

    /**
     * Retrieves the singleton instance of {@link AudioManager}.
     *
     * @return the singleton instance.
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Getter for soundEffectPools
     *
     * @return map of {@link AudioEnum} to {@link Queue<MediaPlayer>}
     */
    protected Map<AudioEnum, Queue<MediaPlayer>> getSoundEffectPools() {
        return soundEffectPools;
    }

    /**
     * Getter for backgroundMusicPlayer
     *
     * @return {@link MediaPlayer} for background music
     */
    protected MediaPlayer getBackgroundMusicPlayer() {
        return backgroundMusicPlayer;
    }

    /**
     * Loads a sound effect into a reusable pool of {@link MediaPlayer} instances.
     *
     * @param audioKey the {@link AudioEnum} key representing the audio.
     */
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

    /**
     * Plays a sound effect.
     *
     * @param audioKey the {@link AudioEnum} key representing the audio.
     * @param loop     whether the sound should loop indefinitely.
     */
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
                player.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
                Queue<MediaPlayer> finalPool = pool;
                player.setOnEndOfMedia(() -> {
                    player.stop();
                    finalPool.offer(player);
                });
                player.play();
            }
        }
    }

    /**
     * Stops all instances of a given sound effect.
     *
     * @param audioKey the {@link AudioEnum} key representing the audio.
     */
    public void stopSoundEffect(AudioEnum audioKey) {
        Queue<MediaPlayer> pool = soundEffectPools.get(audioKey);
        if (pool != null) {
            for (MediaPlayer player : pool) {
                player.stop();
            }
        }
    }

    /**
     * Sets the background music track.
     *
     * @param audioKey the {@link AudioEnum} key representing the audio.
     */
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

    /**
     * Starts playing the background music.
     */
    public void playBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    /**
     * Stops the background music.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    /**
     * Preloads all sound effects asynchronously to reduce playback latency.
     */
    public void preloadSounds() {
        new Thread(() -> {
            for (AudioEnum key : AudioFiles.getAudioFilePaths().keySet()) {
                loadSoundEffect(key);
            }
        }).start();
    }

    /**
     * Creates a new {@link MediaPlayer} instance for a given sound {@link Media}.
     *
     * @param sound the {@link Media} object representing the audio file.
     * @return a new {@link MediaPlayer} instance.
     */
    private MediaPlayer createMediaPlayer(Media sound) {
        AppContext context = AppContext.getInstance();
        MediaPlayer player = new MediaPlayer(sound);
        player.volumeProperty().bind(context.volumeProperty().divide(100.0));
        player.setCycleCount(1);
        return player;
    }

    /**
     * Clears the soundEffectPool
     */
    protected void clearSoundEffectPools() {
        soundEffectPools.clear();
    }

    /**
     * Clears the background media player
     */
    protected void clearBackgroundMediaPlayer() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer = null;
        }
    }
}