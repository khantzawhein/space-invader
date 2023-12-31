package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.enums.MediaIdentifier;
import com.se233.spaceinvader.models.managers.EnemyShipManager;
import com.se233.spaceinvader.views.GamePane;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;

public class EnemyShipDrawLoop implements Runnable {
    private static final Logger logger = LogManager.getLogger(EnemyShipDrawLoop.class);
    private final GamePane gamePane;
    private int fps = 8;
    private final int baseFps = 11;
    private boolean running = true;

    public EnemyShipDrawLoop(GamePane gamePane) {
        this.gamePane = gamePane;
    }

    public void update() {
        if (gamePane.isStarted() && !gamePane.getPlayer().isReviving() && !gamePane.isGameOver()) {
            updateEnemyShips();
            checkCollisions();
        }
    }

    private void checkCollisions() {
        gamePane.getEnemyShipManager().getEnemyShips().forEach(enemyShip -> {
            if (enemyShip.getLayoutY() + enemyShip.getHeight() >= gamePane.getDeadLine().getY()) {
                GamePane.MEDIA_MANAGER.play(MediaIdentifier.GAME_OVER_SOUND);
                Platform.runLater(() -> gamePane.getPlayer().setAsDead());
            }
        });
    }

    private void updateEnemyShips() {
        int enemyCount = gamePane.getEnemyShipManager().getEnemyShips().size();
        int enemyDefeated = -(enemyCount - EnemyShipManager.ORIGINAL_ENEMY_COUNT);

        if (gamePane.getEnemyShipManager().isBossMode()) {
            this.fps = 15;
        } else {
            this.fps = enemyCount > 0 ? (enemyDefeated / 5) + this.baseFps : this.baseFps;
        }

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
        while (running) {
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
                logger.warn("InterruptedException: " + e.getMessage());
            } catch (ConcurrentModificationException e) {
                logger.warn(e.getMessage());
            } catch (Throwable e) {
                logger.error("Error: {}, Message: {}", e.getClass().getName(), e.getMessage());
            }
        }
    }
}
