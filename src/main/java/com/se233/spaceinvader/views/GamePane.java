package com.se233.spaceinvader.views;

import com.se233.spaceinvader.models.EnemyShipManager;
import com.se233.spaceinvader.models.Lives;
import com.se233.spaceinvader.models.Score;
import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.views.elements.DeadLine;
import com.se233.spaceinvader.models.PlayerShip;
import com.se233.spaceinvader.views.elements.ResultText;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

public class GamePane extends Pane {
    private final Score score;
    public static final int GROUND = 650;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 700;
    public static final int PLAYER_WIDTH = 30;
    private final PlayerShip player;
    private final Lives lives;
    private final EnemyShipManager enemyShipManager;
    private final Rectangle deadLine;

    public GamePane() {
        super();
        this.setPrefSize(WIDTH, HEIGHT);
        this.setBackground(Background.fill(Paint.valueOf("#000000")));
        this.player = new PlayerShip((WIDTH / 2) - PLAYER_WIDTH);
        this.score = new Score();
        this.lives = new Lives();
        this.deadLine = new DeadLine();
        this.getChildren().addAll(player, score, lives, deadLine);
        enemyShipManager = new EnemyShipManager(this);
        enemyShipManager.generateEnemyShips();
    }

    public EnemyShipManager getEnemyShipManager() {
        return enemyShipManager;
    }

    public Score getScore() {
        return score;
    }

    public Rectangle getDeadLine() {
        return deadLine;
    }

    public Lives getLives() {
        return lives;
    }

    public ArrayList<Bullet> getBullets() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        for (int i = 0; i < this.getChildren().size(); i++) {
            try {
                Node node = this.getChildren().get(i);
                if (node instanceof Bullet) {
                    bullets.add((Bullet) node);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
        return bullets;
    }

    public boolean isGameOver() {
        return player.isDead() || enemyShipManager.getEnemyShips().isEmpty();
    }

    public PlayerShip getPlayer() {
        return player;
    }
}
