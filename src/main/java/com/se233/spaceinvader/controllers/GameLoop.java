package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.BulletType;
import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.models.Key;
import com.se233.spaceinvader.views.GamePane;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameLoop implements Runnable {
    private final Logger logManager = LogManager.getLogger(GameLoop.class);
    private GamePane gamePane;
    private final int fps = 60;
    private long lastShootTime;

    public GameLoop(GamePane gamePane) {
        this.gamePane = gamePane;
        this.lastShootTime = System.currentTimeMillis();
    }

    public void update() {
        this.detectKey();
    }

    private void detectKey() {
        Key key = Launcher.key;
        if (key.isPressed(KeyCode.LEFT) && !key.isPressed(KeyCode.RIGHT)) {
            gamePane.getPlayer().moveLeft();
            logManager.debug("Player move left, position: " + gamePane.getPlayer().getPosition());
        } else if (key.isPressed(KeyCode.RIGHT) && !key.isPressed(KeyCode.LEFT)) {
            gamePane.getPlayer().moveRight();
            logManager.debug("Player move right, position: " + gamePane.getPlayer().getPosition());
        }
        if (key.isPressed(KeyCode.SPACE)) {
            shootBullet();
        }
    }

    private void shootBullet() {
        // Throttle the shoot speed
        if (System.currentTimeMillis() - lastShootTime > 500) {
            this.lastShootTime = System.currentTimeMillis();
            Bullet bullet = new Bullet(gamePane.getPlayer().getPosition() + (GamePane.PLAYER_WIDTH / 2), BulletType.PLAYER);
            Platform.runLater(() -> gamePane.getChildren().add(bullet));
            logManager.debug("Player shoot, position: " + gamePane.getPlayer().getPosition());
        }
    }

    @Override
    public void run() {

        while (true) {
            this.update();
            try {
                float delay = 1000f / fps;
                Thread.sleep((long) delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
