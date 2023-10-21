package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.models.EnemyShipManager;
import com.se233.spaceinvader.views.GamePane;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;

public class EnemyShipDrawLoop implements Runnable {
    private final Logger logger = LogManager.getLogger(EnemyShipDrawLoop.class);
    private final GamePane gamePane;
    private int fps = 8;
    private boolean running = true;

    public EnemyShipDrawLoop(GamePane gamePane) {
        this.gamePane = gamePane;
    }

    public void update() {
        if (!gamePane.getPlayer().isReviving()) {
            updateEnemyShips();
            checkCollisions();
        }
    }

    private void checkCollisions() {
        gamePane.getEnemyShipManager().getEnemyShips().forEach(enemyShip -> {
            if (enemyShip.getBoundsInParent().intersects(gamePane.getDeadLine().getBoundsInParent())) {
                gamePane.getPlayer().setAsDead();
            }
        });
    }

    private void updateEnemyShips() {
        int enemyCount = gamePane.getEnemyShipManager().getEnemyShips().size();
        int enemyDefeated = -(enemyCount - EnemyShipManager.ORIGINAL_ENEMY_COUNT);

        this.fps = (enemyDefeated / 5) + 8;

        Platform.runLater(() -> {
            gamePane.getEnemyShipManager().update();
        });
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        while (running && !gamePane.isGameOver()) {
            try {
                this.update();
                float delay = 1000f / fps;
                time = System.currentTimeMillis() - time;
                if (time < delay) {
                    Thread.sleep((long) (delay - time));
                } else {
                    Thread.sleep((long) delay);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException e) {
                logger.warn(e.getMessage());
            }
        }
    }
}
