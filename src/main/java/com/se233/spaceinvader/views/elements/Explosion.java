package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.Launcher;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Explosion extends ImageView {
    private final int WIDTH = 30, HEIGHT = 30;
    public Explosion() {
        super();
        Image image = new Image(Launcher.class.getResourceAsStream("assets/sprites/explosion.png"));
        this.setImage(image);

        this.setFitHeight(HEIGHT);
        this.setFitWidth(WIDTH);
    }

    public Explosion(int size) {
        super();
        Image image = new Image(Launcher.class.getResourceAsStream("assets/sprites/explosion.png"));
        this.setImage(image);

        this.setFitHeight(size);
        this.setFitWidth(size);
    }
}
