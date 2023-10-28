package com.se233.spaceinvader.models.managers;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.MediaIdentifier;
import com.se233.spaceinvader.models.Sound;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;

public class MediaManager {
    private static MediaManager instance = null;
    private HashMap<MediaIdentifier, Sound> library;
    private HashMap<MediaIdentifier, MediaPlayer> playing;
    private HashMap<MediaIdentifier, Long> lastSoundPlayedTimes;
    private long soundThrottleDelay = 150;

    public MediaManager() {
        library = new HashMap<>();
        playing = new HashMap<>();
        lastSoundPlayedTimes = new HashMap<>();
        try {
            loadMedia();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }


    private void loadMedia() throws URISyntaxException {
        Sound bgSound = new Sound(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/space-invader.mp3")).toURI().toString());
        library.put(MediaIdentifier.BG_SOUND, bgSound);

        Sound shootSound = new Sound(Objects.requireNonNull(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/shoot.wav")).toURI().toString()));
        library.put(MediaIdentifier.SHOOT_SOUND, shootSound);

        Sound invaderKilled = new Sound(Objects.requireNonNull(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/invaderkilled.wav")).toURI().toString()));
        library.put(MediaIdentifier.INVADER_KILLED, invaderKilled);

        Sound explosion = new Sound(Objects.requireNonNull(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/explosion.wav")).toURI().toString()));
        library.put(MediaIdentifier.EXPLOSION_SOUND, explosion);

        Sound bossSound = new Sound(Objects.requireNonNull(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/ufo_highpitch.wav")).toURI().toString()));
        library.put(MediaIdentifier.BOSS_SOUND, bossSound);

        Sound powerUpSound = new Sound(Objects.requireNonNull(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/power-up.wav")).toURI().toString()));
        library.put(MediaIdentifier.POWER_UP_SOUND, powerUpSound);

        Sound winSound = new Sound(Objects.requireNonNull(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/win-sound.mp3")).toURI().toString()));
        library.put(MediaIdentifier.WIN_SOUND, winSound);

        Sound gameOverSound = new Sound(Objects.requireNonNull(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/game-over.wav")).toURI().toString()));
        library.put(MediaIdentifier.GAME_OVER_SOUND, gameOverSound);
    }

    public void play(MediaIdentifier name) {
        lastSoundPlayedTimes.putIfAbsent(name, (long) 0);
        if (System.currentTimeMillis() - lastSoundPlayedTimes.get(name) < soundThrottleDelay) {
            return;
        }
        lastSoundPlayedTimes.put(name, System.currentTimeMillis());
        MediaPlayer player = playing.get(name);
        if (player != null) {
            player.seek(Duration.millis(0));
            player.play();
        } else {
            player = new MediaPlayer(library.get(name).getMedia());
            player.play();
            playing.put(name, player);
        }
    }

    public void stop(MediaIdentifier name) {
        playing.get(name).stop();
    }

    public static MediaManager getInstance() {
        if (instance == null) {
            instance = new MediaManager();
        }
        return instance;
    }
}
