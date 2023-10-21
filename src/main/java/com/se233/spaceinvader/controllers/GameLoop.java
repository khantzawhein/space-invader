package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.BulletType;
import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.models.Key;
import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.models.EnemyShip;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class GameLoop implements Runnable {
    private final Logger logger = LogManager.getLogger(GameLoop.class);
    private GamePane gamePane;
    private final int fps = 60;
    private long lastShootTime;
    private long lastEnemyShootTime;
    private boolean running = true;

    public GameLoop(GamePane gamePane) {
        this.gamePane = gamePane;
        this.lastShootTime = System.currentTimeMillis();
        this.lastEnemyShootTime = System.currentTimeMillis();
    }

    public void stop() {
        running = false;
    }

    public void update() {
        if (!gamePane.isGameOver() && !gamePane.getPlayer().isReviving()) {
            this.detectKey();
        }
    }

    private void detectKey() {
        Key key = Launcher.key;
        if (key.isPressed(KeyCode.LEFT) && !key.isPressed(KeyCode.RIGHT)) {
            gamePane.getPlayer().moveLeft();
            if (Math.random() < 0.15) {
                logger.debug("Player move left, position: " + gamePane.getPlayer().getPosition());
            }
        } else if (key.isPressed(KeyCode.RIGHT) && !key.isPressed(KeyCode.LEFT)) {
            gamePane.getPlayer().moveRight();
            if (Math.random() < 0.15) {
                logger.debug("Player move right, position: " + gamePane.getPlayer().getPosition());
            }
        }
        if (key.isPressed(KeyCode.SPACE)) {
            shootBullet();
        }
        if (System.currentTimeMillis() - lastEnemyShootTime > 1500) {
            this.shootEnemyBullet();
        }
    }

    private void shootEnemyBullet() {

        this.lastEnemyShootTime = System.currentTimeMillis();
        if (Math.random() <= 0.3) {
            gamePane.getEnemyShipManager().getEnemyShips().stream().max(EnemyShip::compareTo).ifPresent(enemyShip -> {
                double maxY = enemyShip.getTranslateY();
                List<EnemyShip> lowestShips = gamePane.getEnemyShipManager().getEnemyShips().stream().filter(ship -> ship.getTranslateY() >= maxY).toList();
                int enemyShipIndex = new Random().nextInt(lowestShips.size());
                EnemyShip randomShip = lowestShips.get(enemyShipIndex);

                Bullet bullet = new Bullet((int) (randomShip.getTranslateX() + (randomShip.getWidth() / 2)), (int) (randomShip.getTranslateY() + 50), BulletType.ENEMY);
                Platform.runLater(() -> gamePane.getChildren().add(bullet));
            });
        }
    }

    private void shootBullet() {
        // Throttle the shoot speed
        if (System.currentTimeMillis() - lastShootTime > 500) {
            this.lastShootTime = System.currentTimeMillis();
            Bullet bullet = new Bullet(gamePane.getPlayer().getPosition() + (GamePane.PLAYER_WIDTH / 2), BulletType.PLAYER);
            Platform.runLater(() -> gamePane.getChildren().add(bullet));
            logger.debug("Player shoot, position: " + gamePane.getPlayer().getPosition());
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                this.update();
                float delay = 1000f / fps;
                Thread.sleep((long) delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException e) {
                logger.warn("ConcurrentModificationException: " + e.getMessage());
            }
        }
    }
}
