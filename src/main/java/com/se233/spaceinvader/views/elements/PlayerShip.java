package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.views.GamePane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayerShip extends Pane {
    private int position;
    public static final int SPEED = 3;

    private int lives = 3;

    public PlayerShip(int position) {
        super();
        this.position = position;
        ImageView imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/player.png")));
        imageView.setFitHeight(GamePane.PLAYER_WIDTH);
        imageView.setFitWidth(GamePane.PLAYER_WIDTH);

        this.setTranslateX(position);
        this.setTranslateY(GamePane.GROUND);

        this.getChildren().add(imageView);
    }

    public void update() {
        this.setTranslateX(position);
    }

    public void moveLeft() {
        if (position > 10) {
            position -= SPEED;
        }
    }

    public void moveRight() {
        if (position < GamePane.WIDTH - GamePane.PLAYER_WIDTH - 10) {
            position += SPEED;
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLives() {
        return lives;
    }

    public void died() {
        lives--;
    }
}
