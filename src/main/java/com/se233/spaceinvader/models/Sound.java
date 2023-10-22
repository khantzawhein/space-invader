package com.se233.spaceinvader.models;

import javafx.scene.media.Media;

import java.io.File;

public class Sound {
    private Media media;
    public Sound(File file) {
        media = new Media(file.toPath().toString());
    }

    public Media getMedia() {
        return media;
    }
}
