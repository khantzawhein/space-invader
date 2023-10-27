package com.se233.spaceinvader.models;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.views.GamePane;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class PlayerShip extends Pane {
    private int position;
    public static final int SPEED = 3;
    private boolean isDead = false;
    private boolean isReviving = false;
    private final ImageView imageView;

    public PlayerShip(int position) {
        super();
        this.position = position;
        imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/player-ship.png")));
        imageView.setFitHeight(GamePane.PLAYER_WIDTH);
        imageView.setFitWidth(GamePane.PLAYER_WIDTH);

        this.setTranslateX(position);
        this.setTranslateY(GamePane.GROUND);

        this.getChildren().add(imageView);
    }

    public void update() {
        this.setTranslateX(position);
    }

    public void hit() {
        Launcher.getGamePane().getLives().decrementLive();
        Platform.runLater(() -> Launcher.getGamePane().getLives().renderLive());
        if (Launcher.getGamePane().getLives().count() == 0) {
            Platform.runLater(this::setAsDead);
        }
        else {
            this.isReviving = true;
            // Blink the player three times
            for (int i = 0; i < 3; i++) {
                try {
                    Platform.runLater(() -> this.setVisible(false));
                    Thread.sleep(200);
                    Platform.runLater(() -> this.setVisible(true));
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.isReviving = false;
        }

    }

    public void setAsDead() {
        imageView.setImage(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("assets/sprites/player-died.png"))));
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isReviving() {
        return isReviving;
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

}
