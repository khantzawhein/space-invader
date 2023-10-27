package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.enums.BulletType;
import com.se233.spaceinvader.enums.EnemyLevel;
import com.se233.spaceinvader.enums.MediaIdentifier;
import com.se233.spaceinvader.models.BossShip;
import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.models.EnemyShip;
import com.se233.spaceinvader.views.elements.DisplayText;
import com.se233.spaceinvader.views.elements.Rocket;
import com.se233.spaceinvader.views.elements.RocketDrop;
import javafx.application.Platform;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;

public class DrawLoop implements Runnable {
    private final Logger logger = LogManager.getLogger(DrawLoop.class);
    private GamePane gamePane;
    private final int fps = 60;
    private boolean running = true;
    private boolean isResultTextSet = false;

    public DrawLoop(GamePane gamePane) {
        this.gamePane = gamePane;
    }

    private void update() {
        tickResultText();
        if (gamePane.isStarted()) {
            updatePlayer();
            updateMissiles();
            updateDrops();
            checkPowerUpHit();
            showExplosionAndRemoveDeadEnemyShips();
            setResultText();
        } else {
            tickStartPage();
        }
    }

    private void updatePlayer() {
        Platform.runLater(gamePane.getPlayer()::update);
    }

    private void updateDrops() {
        gamePane.getPowerUpManager().update();
    }

    private void tickStartPage() {
        gamePane.getStartPage().getChildren().stream().filter(node -> node instanceof DisplayText).forEach(node -> {
            DisplayText displayText = (DisplayText) node;
            if (!displayText.isTickDone()) {
                Platform.runLater(displayText::tick);
            }
        });
    }

    private void checkPowerUpHit() {
        for (RocketDrop rocketDrop : gamePane.getPowerUpManager().getRocketDrops().stream().filter(rocketDrop -> rocketDrop.getBoundsInParent().intersects(gamePane.getPlayer().getBoundsInParent())).toArray(RocketDrop[]::new)) {
            gamePane.getPowerUpManager().removeFromPane(rocketDrop);
            logger.info("Player hit by rocket power up, position: " + rocketDrop.getTranslateY());
            GamePane.MEDIA_MANAGER.play(MediaIdentifier.POWER_UP_SOUND);
            gamePane.getPowerUpManager().incrementPlayerRocketPowerUpCount();
        }
    }

    private void tickResultText() {
        DisplayText displayText = gamePane.getResultText();
        if (displayText != null && !displayText.isTickDone()) {
            Platform.runLater(displayText::tick);
        }
    }

    private void showExplosionAndRemoveDeadEnemyShips() {
        for (EnemyShip enemyShip : gamePane.getEnemyShipManager().getEnemyShips().stream().filter(EnemyShip::isDead).toArray(EnemyShip[]::new)) {
            // Show Explosion for 0.5 Secs
            if (enemyShip.countDownAndCheckShowingExplosion()) {
                continue;
            }
            // If Countdown is done remove from scene
            if (enemyShip.isDead()) {
                Platform.runLater(() -> {
                    gamePane.getChildren().remove(enemyShip);
                });
            }
        }
    }

    private void updateMissiles() {
        ArrayList<Node> nodes = gamePane.getChildrenConcurrentSafe();
        ArrayList<Bullet> bullets = nodes.stream().filter(node -> node instanceof Bullet).map(node -> (Bullet) node).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Rocket> rockets = nodes.stream().filter(node -> node instanceof Rocket).map(node -> (Rocket) node).collect(Collectors.toCollection(ArrayList::new));
        for (Bullet bullet : bullets) {
            Platform.runLater(bullet::update);
            if (bullet.isOutOfBound()) {
                Platform.runLater(() -> gamePane.getChildren().remove(bullet));
                logger.debug("Bullet removed, position: " + bullet.getTranslateY());
            }
            checkBulletHit(bullet);
        }

        for (Rocket rocket : rockets) {
            Platform.runLater(rocket::update);
            if (rocket.isOutOfBound()) {
                Platform.runLater(() -> gamePane.getChildren().remove(rocket));
                logger.info("Rocket removed, position: " + rocket.getTranslateY());
            }
            checkRocketHit(rocket);
        }
    }

    private void checkRocketHit(Rocket rocket) {
        for (EnemyShip enemyShip : gamePane.getEnemyShipManager().getEnemyShips()) {
            if (!enemyShip.isDead() && rocket.getBoundsInParent().intersects(enemyShip.getBoundsInParent())) {
                logger.info("Enemy hit by rocket, position: " + rocket.getTranslateY());
                GamePane.MEDIA_MANAGER.play(MediaIdentifier.INVADER_KILLED);
                ArrayList<EnemyShip> surroundingEnemies = gamePane.getEnemyShipManager().getSurroundingEnemies(enemyShip);
                int score = 0;
                for (EnemyShip surroundingEnemy : surroundingEnemies) {
                    EnemyLevel enemyLevel = surroundingEnemy.getEnemyLevel();
                    score += enemyLevel == EnemyLevel.FRONT ? 10 : enemyLevel == EnemyLevel.MIDDLE ? 20 : 30;

                }
                gamePane.getScore().incrementScoreBy(score);
                    Platform.runLater(() -> {
                        gamePane.getChildren().removeAll(surroundingEnemies);
                        gamePane.getChildren().remove(rocket);
                        gamePane.getScore().renderScore();
                    });

                break;
            }
        }
    }

    private void checkBulletHit(Bullet bullet) {
        if (bullet.getType() == BulletType.ENEMY) {
            if (bullet.getBoundsInParent().intersects(gamePane.getPlayer().getBoundsInParent())) {
                Platform.runLater(() -> gamePane.getChildren().remove(bullet));
                logger.info("Player hit by bullet, position: " + bullet.getTranslateY());
                GamePane.MEDIA_MANAGER.play(MediaIdentifier.EXPLOSION_SOUND);
                gamePane.getPlayer().hit();
            }
        } else {
            checkEnemyBulletHit(bullet);
        }
    }

    private void checkEnemyBulletHit(Bullet bullet) {
        if (gamePane.getEnemyShipManager().isBossMode() && gamePane.getEnemyShipManager().getBossShip().isStartAnimationDone()) {
            BossShip bossShip = gamePane.getEnemyShipManager().getBossShip();
            if (!bossShip.isDead() && bullet.getBoundsInParent().intersects(bossShip.getBoundsInParent())) {
                int score = 500;
                GamePane.MEDIA_MANAGER.play(MediaIdentifier.INVADER_KILLED);
                gamePane.getScore().incrementScoreBy(score);
                logger.info("Boss hit by bullet, position: " + bullet.getTranslateY());
                Platform.runLater(() -> {
                    gamePane.getChildren().remove(bullet);
                    gamePane.getEnemyShipManager().getBossShip().hit();
                    if (gamePane.getEnemyShipManager().getBossShip().isDead()) {
                        gamePane.getChildren().remove(bossShip);
                    }
                    gamePane.getScore().renderScore();
                });
            }
        } else {
            for (EnemyShip enemyShip : gamePane.getEnemyShipManager().getEnemyShips()) {
                if (!enemyShip.isDead() && bullet.getBoundsInParent().intersects(enemyShip.getBoundsInParent())) {
                    logger.info("Enemy hit by bullet, position: " + bullet.getTranslateY());
                    GamePane.MEDIA_MANAGER.play(MediaIdentifier.INVADER_KILLED);
                    EnemyLevel enemyLevel = enemyShip.getEnemyLevel();
                    int score = enemyLevel == EnemyLevel.FRONT ? 10 : enemyLevel == EnemyLevel.MIDDLE ? 20 : 30;
                    gamePane.getScore().incrementScoreBy(score);
                    Platform.runLater(() -> {
                        gamePane.getChildren().remove(bullet);
                        enemyShip.die();
                        gamePane.getScore().renderScore();
                    });
                    break;
                }
            }
        }
    }

    private void setResultText() {
        if (gamePane.isGameOver() && !isResultTextSet) {
            this.isResultTextSet = true;
            Platform.runLater(() -> {
                if (gamePane.getEnemyShipManager().getEnemyShips().isEmpty() && !gamePane.getPlayer().isDead()) {
                    gamePane.getChildren().add(DisplayText.win());
                } else if (gamePane.getPlayer().isDead()) {
                    gamePane.getChildren().add(DisplayText.gameOver());
                }
            });
        }
    }

    public void stop() {
        running = false;
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
                e.printStackTrace();
                logger.warn("ConcurrentModificationException: " + e.getMessage());
            }
        }
    }
}
