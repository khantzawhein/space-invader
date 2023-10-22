package com.se233.spaceinvader.views;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.MediaIdentifier;
import com.se233.spaceinvader.models.*;
import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.views.elements.DeadLine;
import com.se233.spaceinvader.views.elements.DisplayText;
import com.se233.spaceinvader.views.elements.StartPage;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
    private boolean started = false;
    private StartPage startPage;
    public static final MediaManager MEDIA_MANAGER = new MediaManager();

    public GamePane() {
        super();
        this.setPrefSize(WIDTH, HEIGHT);
        this.setBackground(Background.fill(Paint.valueOf("#000000")));
        this.player = new PlayerShip((WIDTH / 2) - PLAYER_WIDTH);
        this.score = new Score();
        this.lives = new Lives();
        this.deadLine = new DeadLine();
        startPage = new StartPage();
        this.getChildren().addAll(player, score, lives, deadLine, startPage);
        enemyShipManager = new EnemyShipManager(this);
        MEDIA_MANAGER.play(MediaIdentifier.BG_SOUND);
    }

    public EnemyShipManager getEnemyShipManager() {
        return enemyShipManager;
    }

    public StartPage getStartPage() {
        return startPage;
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
        return this.getChildrenConcurrentSafe().stream().filter(node -> node instanceof Bullet)
                .map(node -> (Bullet) node).collect(Collectors.toCollection(ArrayList::new));
    }

    public DisplayText getResultText() {
        return this.getChildrenConcurrentSafe().stream().filter(node -> node instanceof DisplayText)
                .map(node -> (DisplayText) node).findFirst().orElse(null);
    }

    public ArrayList<Node> getChildrenConcurrentSafe() {
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < this.getChildren().size(); i++) {
            try {
                Node node = this.getChildren().get(i);
                nodes.add(node);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
        return nodes;
    }

    public boolean isGameOver() {
        return player.isDead() || enemyShipManager.isBossDead();
    }

    public PlayerShip getPlayer() {
        return player;
    }

    public void start() {
        if (!started) {
            this.getStartPage().setVisible(false);
            this.getEnemyShipManager().generateEnemyShips();
            MEDIA_MANAGER.stop(MediaIdentifier.BG_SOUND);
            this.started = true;
        }
    }

    public boolean isStarted() {
        return started;
    }
}
