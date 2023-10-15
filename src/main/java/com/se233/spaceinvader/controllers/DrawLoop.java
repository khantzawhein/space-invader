package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.enums.BulletType;
import com.se233.spaceinvader.enums.EnemyLevel;
import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.models.EnemyShip;
import com.se233.spaceinvader.views.elements.ResultText;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DrawLoop implements Runnable {
    private final Logger logger = LogManager.getLogger(DrawLoop.class);
    private GamePane gamePane;
    private final int fps = 60;

    private boolean running = true;

    public DrawLoop(GamePane gamePane) {
        this.gamePane = gamePane;
    }

    private void update() {
        gamePane.getPlayer().update();
        updateBullets();
        checkCollisions();
        if (gamePane.isGameOver()) {
            setResultText();
        }

    }

    private void checkCollisions() {
        gamePane.getEnemyShipManager().getEnemyShips().stream().forEach(enemyShip -> {
            if (enemyShip.getBoundsInParent().intersects(gamePane.getDeadLine().getBoundsInParent())) {
                gamePane.getPlayer().setAsDead();
            }
        });
    }


    private void updateBullets() {
        for (Bullet bullet : gamePane.getBullets()) {
            Platform.runLater(bullet::update);

            if (bullet.isOutOfBound()) {
                Platform.runLater(() -> gamePane.getChildren().remove(bullet));
                logger.debug("Bullet removed, position: " + bullet.getTranslateY());
            }
            checkBulletHit(bullet);
        }
    }

    private void checkBulletHit(Bullet bullet) {
        if (bullet.getType() == BulletType.ENEMY) {
            if (bullet.getBoundsInParent().intersects(gamePane.getPlayer().getBoundsInParent())) {
                Platform.runLater(() -> gamePane.getChildren().remove(bullet));
                logger.info("Player hit by bullet, position: " + bullet.getTranslateY());
                gamePane.getPlayer().hit();
            }
        } else {
            for (EnemyShip enemyShip : gamePane.getEnemyShipManager().getEnemyShips()) {
                if (bullet.getBoundsInParent().intersects(enemyShip.getBoundsInParent())) {
                    logger.info("Enemy hit by bullet, position: " + bullet.getTranslateY());
                    EnemyLevel enemyLevel = enemyShip.getEnemyLevel();
                    int score = enemyLevel == EnemyLevel.FRONT ? 10 : enemyLevel == EnemyLevel.MIDDLE ? 20 : 30;
                    Platform.runLater(() -> {
                        gamePane.getChildren().remove(bullet);
                        gamePane.getChildren().remove(enemyShip);
                        gamePane.getScore().increaseScoreBy(score);
                    });
                }
            }
        }
    }

    private void setResultText() {
        Platform.runLater(() -> {
            if (gamePane.getEnemyShipManager().getEnemyShips().isEmpty() && !gamePane.getPlayer().isDead()) {
                gamePane.getChildren().add(ResultText.win());
            } else if (gamePane.getPlayer().isDead()) {
                gamePane.getChildren().add(ResultText.gameOver());
            }
        });
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
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
