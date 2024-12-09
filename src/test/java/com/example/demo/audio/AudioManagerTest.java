package com.example.demo.audio;

import com.example.demo.JFXTestBase;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class AudioManagerTest extends JFXTestBase {
    private AudioManager audioManager;

    @BeforeEach
    void setUp() {
        audioManager = AudioManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        audioManager.stopBackgroundMusic();
        audioManager.clearBackgroundMediaPlayer();
        audioManager.clearSoundEffectPools();
    }

    @Test
    void testGetInstance() {
        AudioManager instance1 = AudioManager.getInstance();
        AudioManager instance2 = AudioManager.getInstance();
        assertNotNull(instance1, "AudioManager instance should not be null.");
        assertSame(instance1, instance2, "AudioManager should be a singleton.");
    }

    @Test
    void testLoadSoundEffect() {
        audioManager.loadSoundEffect(AudioEnum.BUTTON_CLICK);
        Map<AudioEnum, Queue<MediaPlayer>> sep = audioManager.getSoundEffectPools();
        assertTrue(sep.containsKey(AudioEnum.BUTTON_CLICK), "Sound effect should be successfully loaded.");
    }

    @Test
    void testPlaySoundEffect() {
        audioManager.loadSoundEffect(AudioEnum.BUTTON_CLICK);
        assertDoesNotThrow(() -> audioManager.playSoundEffect(AudioEnum.BUTTON_CLICK, false),
                "Playing a sound effect should not throw an exception.");
    }

    @Test
    void testStopSoundEffect() {
        audioManager.loadSoundEffect(AudioEnum.BUTTON_CLICK);
        audioManager.playSoundEffect(AudioEnum.BUTTON_CLICK, false);
        assertDoesNotThrow(() -> audioManager.stopSoundEffect(AudioEnum.BUTTON_CLICK),
                "Stopping a sound effect should not throw an exception.");
    }

    @Test
    void testSetBackgroundMusic() {
        audioManager.setBackgroundMusic(AudioEnum.BG_MUSIC);
        assertNotNull(audioManager.getBackgroundMusicPlayer(), "Background music should be successfully set.");
        assertNotNull(audioManager.getBackgroundMusicPlayer().getMedia(), "Background music should be successfully set.");
    }

    // These tests have been disabled due to intermittently failing, causing error in CI (see README)
//    @Test
//    void testPlayBackgroundMusic() {
//        audioManager.setBackgroundMusic(AudioEnum.STAR_1); // Use diff audio file to prevent waiting for media to load
//        audioManager.playBackgroundMusic();
//        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
//            while (true) {
//                if (audioManager.getBackgroundMusicPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
//                    break;
//                }
//            }
//        }, "Media Player Status should be playing");
//    }
//
//    @Test
//    void testStopBackgroundMusic() {
//        audioManager.setBackgroundMusic(AudioEnum.STAR_1); // Use diff audio file to prevent waiting for media to load
//        audioManager.playBackgroundMusic();
//        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
//            while (true) {
//                if (audioManager.getBackgroundMusicPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
//                    break;
//                }
//            }
//        }, "Media Player Status should be playing");
//        audioManager.stopBackgroundMusic();
//        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
//            while (true) {
//                if (audioManager.getBackgroundMusicPlayer().getStatus() == MediaPlayer.Status.STOPPED) {
//                    break;
//                }
//            }
//        }, "Media Player Status should be STOPPED");
//    }

    @Test
    void testPreloadSounds() throws InterruptedException {
        audioManager.preloadSounds(); // execution is async, so we wait for a bit
        Thread.sleep(1000);
        assertNotEquals(0, audioManager.getSoundEffectPools().size(), "Sound effect pools should not be empty.");
    }
}