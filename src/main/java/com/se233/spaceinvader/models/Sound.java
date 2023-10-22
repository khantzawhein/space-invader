package com.se233.spaceinvader.models;

import javafx.scene.media.Media;

import java.io.File;

public class Sound {
    private final Media media;
    public Sound(File file) {
        media = new Media(file.toURI().toString());
    }

    public Media getMedia() {
        return media;
    }
}
