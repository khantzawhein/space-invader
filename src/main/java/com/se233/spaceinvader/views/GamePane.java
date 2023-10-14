package com.se233.spaceinvader.views;

import com.se233.spaceinvader.models.Lives;
import com.se233.spaceinvader.models.Score;
import com.se233.spaceinvader.views.elements.PlayerShip;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class GamePane extends Pane {
    private final Score score;
    public static final int GROUND = 650;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 700;
    public static final int PLAYER_WIDTH = 30;
    private final PlayerShip player;
    private final Lives lives;
    public GamePane() {
        super();
        this.setPrefSize(WIDTH, HEIGHT);
        this.setBackground(Background.fill(Paint.valueOf("#000000")));
        this.player = new PlayerShip((WIDTH / 2) - PLAYER_WIDTH);
        this.score = new Score();
        this.lives = new Lives();

        this.getChildren().addAll(player, score, lives);
    }
    public Score getScore() {
        return score;
    }

    public Lives getLives() {
        return lives;
    }

    public PlayerShip getPlayer() {
        return player;
    }
}
