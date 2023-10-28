package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.enums.BulletType;
import com.se233.spaceinvader.enums.MediaIdentifier;
import com.se233.spaceinvader.models.BossShip;
import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.models.Key;
import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.models.EnemyShip;
import com.se233.spaceinvader.views.elements.DisplayText;
import com.se233.spaceinvader.views.elements.Rocket;
import com.se233.spaceinvader.views.elements.RocketDrop;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

public class GameLoop implements Runnable {
    private static final Logger logger = LogManager.getLogger(GameLoop.class);
    private GamePane gamePane;
    private final int fps = 60;
    private long lastShootTime;
    private long lastEnemyShootTime;
    private boolean running = true;
    private long pauseTimeBeforeBossCome = 5000;
    private long generateBossShipDelay;

    public GameLoop(GamePane gamePane) {
        this.gamePane = gamePane;
        this.lastShootTime = System.currentTimeMillis();
        this.lastEnemyShootTime = System.currentTimeMillis();
    }

    public void stop() {
        running = false;
    }

    private void update() {
        if (gamePane.isStarted() && !gamePane.isGameOver() && !gamePane.getPlayer().isReviving()) {
            this.detectKey();
            this.shootBullets();
            this.dropPowerUps();
            this.checkAndGenerateBossShip();
        }
        if (!gamePane.isStarted()) {
            this.detectStartKey();
        }
    }

    private void dropPowerUps() {
        gamePane.getPowerUpManager().generatePowerUp();
    }

    private void detectStartKey() {
        if (!gamePane.isStarted() && Launcher.key.isPressed(KeyCode.SPACE)) {
            Platform.runLater(gamePane::start);
        }
    }

    private void checkAndGenerateBossShip() {
        if (this.generateBossShipDelay == 0 && gamePane.getEnemyShipManager().getEnemyShips().isEmpty() && !gamePane.getEnemyShipManager().isBossMode()) {
            this.generateBossShipDelay = System.currentTimeMillis();
            Platform.runLater(() -> {
                gamePane.getChildren().add(DisplayText.bossComing());
            });
        }

        if (this.generateBossShipDelay != 0 && System.currentTimeMillis() - this.generateBossShipDelay >= pauseTimeBeforeBossCome) {
            this.generateBossShipDelay = 0;
            Platform.runLater(() -> {
                gamePane.getChildren().removeIf(node -> node instanceof DisplayText);
                gamePane.getEnemyShipManager().generateBossShip();
            });
        }
    }

    private void shootBullets() {
        if (gamePane.getEnemyShipManager().isBossMode()) {
            this.shootBossBullet();
        } else {
            this.shootEnemyBullet();
        }
    }

    private void detectKey() {
        Key key = Launcher.key;
        if (key.isPressed(KeyCode.LEFT) && !key.isPressed(KeyCode.RIGHT)) {
            Platform.runLater(() -> gamePane.getPlayer().moveLeft());
            if (Math.random() < 0.15) {
                logger.debug("Player move left, position: " + gamePane.getPlayer().getPosition());
            }
        } else if (key.isPressed(KeyCode.RIGHT) && !key.isPressed(KeyCode.LEFT)) {
            Platform.runLater(() -> gamePane.getPlayer().moveRight());
            if (Math.random() < 0.15) {
                logger.debug("Player move right, position: " + gamePane.getPlayer().getPosition());
            }
        }
        if (key.isPressed(KeyCode.SPACE)) {
            if (gamePane.getPowerUpManager().getPlayerRocketPowerUpCount() > 0 && !gamePane.getEnemyShipManager().isBossMode()) {
                shootRocket();
            } else {
                shootBullet();
            }

        }
    }

    private void shootRocket() {
        if (System.currentTimeMillis() - lastShootTime > 700 && gamePane.getPowerUpManager().getPlayerRocketPowerUpCount() > 0) {
            this.lastShootTime = System.currentTimeMillis();
            gamePane.getPowerUpManager().decrementPlayerRocketPowerUpCount();
            Rocket rocket = new Rocket(gamePane.getPlayer().getPosition() + (GamePane.PLAYER_WIDTH / 2.0) - 7.5);
            GamePane.MEDIA_MANAGER.play(MediaIdentifier.SHOOT_SOUND);
            Platform.runLater(() -> {
                gamePane.getChildren().add(rocket);
            });
        }
    }

    private void shootEnemyBullet() {
        if (System.currentTimeMillis() - lastEnemyShootTime > 1500) {
            this.lastEnemyShootTime = System.currentTimeMillis();
            if (Math.random() <= 0.4) {
                gamePane.getEnemyShipManager().getEnemyShips().stream().max(EnemyShip::compareTo).ifPresent(enemyShip -> {
                    GamePane.MEDIA_MANAGER.play(MediaIdentifier.SHOOT_SOUND);
                    double maxY = enemyShip.getLayoutY();
                    List<EnemyShip> lowestShips = gamePane.getEnemyShipManager().getEnemyShips().stream().filter(ship -> ship.getLayoutY() >= maxY).toList();
                    int enemyShipIndex = new Random().nextInt(lowestShips.size());
                    EnemyShip randomShip = lowestShips.get(enemyShipIndex);

                    Bullet bullet = new Bullet((int) (randomShip.getLayoutX() + (randomShip.getWidth() / 2)), (int) (randomShip.getLayoutY() + 50), BulletType.ENEMY);
                    Platform.runLater(() -> gamePane.getChildren().add(bullet));
                });
            }
        }
    }

    private void shootBossBullet() {
        if (System.currentTimeMillis() - this.lastEnemyShootTime > 700
                && gamePane.getEnemyShipManager().getBossShip().isStartAnimationDone()) {
            this.lastEnemyShootTime = System.currentTimeMillis();

            if (Math.random() < 0.7) {
                GamePane.MEDIA_MANAGER.play(MediaIdentifier.SHOOT_SOUND);
                BossShip bossShip = gamePane.getEnemyShipManager().getBossShip();
                Bullet bullet = new Bullet((int) (bossShip.getTranslateX() + (bossShip.getWidth() / 2)), (int) (bossShip.getTranslateY() + 50), BulletType.ENEMY);
                Platform.runLater(() -> gamePane.getChildren().add(bullet));
            }
        }
    }

    private void shootBullet() {
        // Throttle the shoot speed
        if (System.currentTimeMillis() - lastShootTime > 700) {
            GamePane.MEDIA_MANAGER.play(MediaIdentifier.SHOOT_SOUND);
            this.lastShootTime = System.currentTimeMillis();
            Bullet bullet = new Bullet(gamePane.getPlayer().getPosition() + (GamePane.PLAYER_WIDTH / 2), BulletType.PLAYER);
            Platform.runLater(() -> gamePane.getChildren().add(bullet));
            logger.info("Player shoot, position: " + gamePane.getPlayer().getPosition());
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
                logger.error("InterruptedException: " + e.getMessage());
            } catch (ConcurrentModificationException e) {
                logger.warn("ConcurrentModificationException: " + e.getMessage());
            } catch (Throwable e) {
                logger.error("Error: {}, Message: {}", e.getClass().getName(), e.getMessage());
            }
        }
    }
}
