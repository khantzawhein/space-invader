package com.se233.spaceinvader.models;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Score extends Text {
    private int score = 0;
    public Score() {
        super();
        this.setText("Score: " + String.format("%05d", score));
        this.setFill(Color.WHITE);
        this.setFont(Font.font("Space Invaders", 15));
        this.setX(20);
        this.setY(35);
    }

    public void increaseScore() {
        score++;
        this.setText("Score: " + score);
    }
}
