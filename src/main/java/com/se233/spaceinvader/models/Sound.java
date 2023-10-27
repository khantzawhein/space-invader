package com.se233.spaceinvader.models;

import javafx.scene.media.Media;

import java.io.File;

public class Sound {
    private final Media media;
    public Sound(String sound) {
        media = new Media(sound);
    }

    public Media getMedia() {
        return media;
    }
}
