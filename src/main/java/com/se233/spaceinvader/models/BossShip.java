package com.se233.spaceinvader.models;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.views.elements.Explosion;
import com.se233.spaceinvader.views.elements.SpriteSheetAnimator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class BossShip extends Pane {
    private final EnemyShipManager enemyShipManager;
    private final long EXPLOSION_DURATION = 200;
    private final int SPEED = 12;
    private boolean isDead = false;
    private int hp = 3;
    private long lastExplosionTime;
    private ImageView explosion;

    public BossShip(EnemyShipManager enemyShipManager, double currentX, double currentY) {
        super();
        this.enemyShipManager = enemyShipManager;
        ImageView imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/boss-ship.png")));
        imageView.setFitHeight(30);
        imageView.setFitWidth(60);
        this.setTranslateX(currentX);
        this.setTranslateY(currentY);
        this.getChildren().addAll(imageView);
    }

    public void update() {
        if (enemyShipManager.isMovingLeft()) {
            this.setTranslateX(this.getTranslateX() - SPEED);
        } else if (enemyShipManager.isMovingRight()) {
            this.setTranslateX(this.getTranslateX() + SPEED);
        }
        if (this.getTranslateX() < 10) {
            this.enemyShipManager.boundaryHitLeft();
        } else if (this.getTranslateX() > GamePane.WIDTH - 70) {
            this.enemyShipManager.boundaryHitRight();
        }

        removeExplosionAfterDuration();
    }

    private void removeExplosionAfterDuration() {
        if (System.currentTimeMillis() - lastExplosionTime > EXPLOSION_DURATION) {
            if (this.getChildren().remove(explosion)) {
                if (this.hp == 0) {
                    this.die();
                    return;
                }
                ImageView imageView;
                if (this.hp == 2) {
                    imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/boss-ship-2.png")));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(40);
                } else {
                    imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/boss-ship-1.png")));
                    imageView.setFitHeight(30);
                    imageView.setFitWidth(20);
                }
                this.getChildren().add(imageView);
            }
        }
    }

    public void teleport() {
        this.setTranslateX(10 + Math.random() * (GamePane.WIDTH - 80));
    }


    public boolean isDead() {
        return isDead;
    }

    public void hit() {
        this.lastExplosionTime = System.currentTimeMillis();
        this.hp--;
        this.getChildren().remove(0);
        if (this.hp == 2) {
            this.explosion = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/boss-ship-explosion-2.png")));
            explosion.setFitHeight(30);
            explosion.setFitWidth(60);
            this.getChildren().add(this.explosion);
        } else if (this.hp == 1) {
            this.explosion = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/boss-ship-explosion-1.png")));
            this.explosion.setFitHeight(30);
            this.explosion.setFitWidth(60);
            this.getChildren().add(this.explosion);
        } else {
            this.explosion = new Explosion(40);
            this.getChildren().add(this.explosion);
        }
    }

    public void die() {
        this.isDead = true;
    }
}
