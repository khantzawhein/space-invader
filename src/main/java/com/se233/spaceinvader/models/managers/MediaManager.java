package com.se233.spaceinvader.models.managers;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.MediaIdentifier;
import com.se233.spaceinvader.models.Sound;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
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
        Sound bgSound = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/space-invader.mp3")).getFile()));
        library.put(MediaIdentifier.BG_SOUND, bgSound);

        Sound shootSound = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/shoot.wav")).getFile()));
        library.put(MediaIdentifier.SHOOT_SOUND, shootSound);

        Sound invaderKilled = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/invaderkilled.wav")).getFile()));
        library.put(MediaIdentifier.INVADER_KILLED, invaderKilled);

        Sound explosion = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/explosion.wav")).getFile()));
        library.put(MediaIdentifier.EXPLOSION_SOUND, explosion);

        Sound bossSound = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/ufo_highpitch.wav")).getFile()));
        library.put(MediaIdentifier.BOSS_SOUND, bossSound);

        Sound powerUpSound = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/power-up.mp3")).getFile()));
        library.put(MediaIdentifier.POWER_UP_SOUND, powerUpSound);
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
