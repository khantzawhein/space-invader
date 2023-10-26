package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.DisplayTextAnimation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class StartPage extends StackPane {
    public StartPage() {
        this.setAlignment(Pos.CENTER);
        ImageView imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/logo.png")));
        imageView.setTranslateY(100);
        DisplayText authorText = new DisplayText("SE233 Project II\n\nBy Potatoes and French Fry Team", 110, DisplayTextAnimation.TICKING);
        authorText.setTranslateY(250);
        DisplayText startText = DisplayText.startText();
        startText.setTranslateY(400);
        this.getChildren().addAll(imageView, authorText, startText);
    }
}
