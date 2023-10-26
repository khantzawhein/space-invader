package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.views.GamePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class RocketDrop extends Pane {
    public final double SPEED = 3;
    public RocketDrop(double x, double y) {
        super();
        this.setTranslateY(y);
        this.setTranslateX(x);
        ImageView imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/rocket.png")));
        imageView.setFitWidth(30);
        imageView.setFitHeight(32);
        this.setRotate(30);
        this.getChildren().add(imageView);
    }

    public void update() {
        this.setTranslateY(this.getTranslateY() + SPEED);
    }

    public boolean isOutOfBound() {
        return this.getTranslateY() > GamePane.GROUND;
    }


}
