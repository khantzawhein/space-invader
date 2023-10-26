package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.views.GamePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Rocket extends Pane {
    private final int SPEED = 5;
    public Rocket(double x) {
        super();
        ImageView imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/rocket-icon.png")));
        imageView.setFitHeight(30);
        imageView.setFitWidth(15);
        this.setTranslateY(GamePane.GROUND - GamePane.PLAYER_WIDTH - 10);
        this.setTranslateX(x);
        this.getChildren().add(imageView);
    }

    public void update() {
        this.setTranslateY(this.getTranslateY() - SPEED);
    }

    public boolean isOutOfBound() {
        return this.getTranslateY() < 0;
    }
}
