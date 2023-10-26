package com.se233.spaceinvader.views;

import com.se233.spaceinvader.Launcher;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Background extends ImageView {
    public Background() {
        Image backgroundImage = new Image(Launcher.class.getResourceAsStream("assets/background.png"));
        this.setImage(backgroundImage);
        TranslateTransition scrollTransition = new TranslateTransition(Duration.seconds(20), this);
        scrollTransition.setByY(-2048 + GamePane.HEIGHT);
        scrollTransition.setCycleCount(TranslateTransition.INDEFINITE);
        scrollTransition.play();
    }
}
