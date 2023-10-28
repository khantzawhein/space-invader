package com.se233.spaceinvader.models;

import com.se233.spaceinvader.enums.EnemyLevel;
import com.se233.spaceinvader.models.managers.EnemyShipManager;
import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.views.elements.Explosion;
import com.se233.spaceinvader.views.elements.SpriteSheetAnimator;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnemyShip extends Pane implements Comparable<EnemyShip> {
    private final SpriteSheetAnimator spriteSheetAnimator;
    private final EnemyShipManager enemyShipManager;
    private final EnemyLevel enemyLevel;
    private static final int SPEED = 5;
    private boolean isDead = false;
    private int deadAnimationFrames = 8;

    public EnemyShip(EnemyShipManager enemyShipManager, EnemyLevel enemyLevel, double x, double y) {
        super();
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.enemyShipManager = enemyShipManager;
        this.enemyLevel = enemyLevel;
        if (enemyLevel == EnemyLevel.FRONT) {
            spriteSheetAnimator = new SpriteSheetAnimator(1, 1, 3, 30);
        } else if (enemyLevel == EnemyLevel.MIDDLE) {
            spriteSheetAnimator = new SpriteSheetAnimator(1, 2, 3, 30);
        } else {
            spriteSheetAnimator = new SpriteSheetAnimator(1, 3, 3, 30);
        }
        this.getChildren().add(spriteSheetAnimator);
    }

    public SpriteSheetAnimator getSpriteSheetAnimator() {
        return spriteSheetAnimator;
    }

    public void die() {
        this.isDead = true;
        this.getChildren().remove(spriteSheetAnimator);
        this.getChildren().add(new Explosion());
    }

    public boolean countDownAndCheckShowingExplosion() {
        if (this.deadAnimationFrames > 0 && this.isDead) {
            this.deadAnimationFrames--;
        }
        return this.deadAnimationFrames > 0 && this.isDead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void update() {
        this.spriteSheetAnimator.tick();

        if (enemyShipManager.isMovingLeft()) {
            this.setLayoutX(this.getLayoutX() - SPEED);
        } else if (enemyShipManager.isMovingRight()) {
            this.setLayoutX(this.getLayoutX() + SPEED);
        }

        if (this.getLayoutX() < 20) {
            this.enemyShipManager.boundaryHitLeft();
        } else if (this.getLayoutX() > GamePane.WIDTH - 50) {
            this.enemyShipManager.boundaryHitRight();
        }
    }

    public EnemyLevel getEnemyLevel() {
        return enemyLevel;
    }

    public void moveDown() {
        this.setLayoutY(this.getLayoutY() + 15);
    }

    @Override
    public int compareTo(EnemyShip o) {
        return Double.compare(this.getLayoutY(), o.getLayoutY());
    }
}
