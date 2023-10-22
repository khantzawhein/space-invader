package com.se233.spaceinvader.models;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.MediaIdentifier;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class MediaManager {
    private static MediaManager instance = null;
    private HashMap<MediaIdentifier, Sound> library;
    private HashMap<MediaIdentifier, MediaPlayer> playing;
    public MediaManager() {
        library = new HashMap<>();
        playing = new HashMap<>();
        Sound bgSound = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/space-invader.mp3")).toString()));
        library.put(MediaIdentifier.BG_SOUND, bgSound);

        Sound shootSound = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/shoot.wav")).toString()));
        library.put(MediaIdentifier.SHOOT_SOUND, shootSound);

        Sound invaderKilled = new Sound(new File(Objects.requireNonNull(Launcher.class.getResource("assets/sounds/invaderkilled.wav")).toString()));
        library.put(MediaIdentifier.INVADER_KILLED, invaderKilled);
    }

    public void play(MediaIdentifier name) {
        MediaPlayer player = playing.get(name);
        if (player != null) {
            player.stop();
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
