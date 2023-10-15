package com.se233.spaceinvader.models;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Lives extends Text {
    private int lives = 3;
    public Lives() {
        super();
        this.setText("Lives: " + String.format("%d", lives));
        this.setFill(Color.WHITE);
        this.setFont(Font.font("Space Invaders", 15));
        this.setX(500);
        this.setY(35);
    }

    public void decrementLive() {
        lives--;
        this.setText("Lives: " + lives);
    }

    public int count() {
        return lives;
    }
}
