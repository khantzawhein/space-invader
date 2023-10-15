package com.se233.spaceinvader.models;

import com.se233.spaceinvader.enums.EnemyLevel;
import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.views.elements.SpriteSheetAnimator;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnemyShip extends Pane implements Comparable<EnemyShip> {

    Logger logger = LogManager.getLogger(EnemyShip.class);
    private final SpriteSheetAnimator spriteSheetAnimator;
    private final EnemyShipManager enemyShipManager;
    private final EnemyLevel enemyLevel;
    private static final int SPEED = 5;

    public EnemyShip(EnemyShipManager enemyShipManager, EnemyLevel enemyLevel, double x, double y) {
        super();
        this.setTranslateX(x);
        this.setTranslateY(y);
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

    public void update() {
        this.spriteSheetAnimator.tick();

        if (enemyShipManager.isMovingLeft()) {
            this.setTranslateX(this.getTranslateX() - SPEED);
        } else if (enemyShipManager.isMovingRight()) {
            this.setTranslateX(this.getTranslateX() + SPEED);
        }

        if (this.getTranslateX() < 30) {
            this.enemyShipManager.boundaryHitLeft();
        } else if (this.getTranslateX() > GamePane.WIDTH - 50) {
            this.enemyShipManager.boundaryHitRight();
        }
    }

    public EnemyLevel getEnemyLevel() {
        return enemyLevel;
    }

    public void moveDown() {
        this.setTranslateY(this.getTranslateY() + 15);
    }

    @Override
    public int compareTo(EnemyShip o) {
        return Double.compare(this.getTranslateY(), o.getTranslateY());
    }
}
