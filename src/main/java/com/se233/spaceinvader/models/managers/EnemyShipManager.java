package com.se233.spaceinvader.models.managers;

import com.se233.spaceinvader.enums.EnemyLevel;
import com.se233.spaceinvader.models.BossShip;
import com.se233.spaceinvader.models.EnemyShip;
import com.se233.spaceinvader.views.GamePane;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public class EnemyShipManager {
    private ObservableList<Node> paneList;
    private boolean isMovingLeft, isMovingRight, shouldMoveLeft, shouldMoveRight;
    private Logger logger = LogManager.getLogger(EnemyShipManager.class);
    private boolean isBossMode = false;
    public final static int ORIGINAL_ENEMY_COUNT = 60;

    private ArrayList<EnemyShip> originalEnemyShips = new ArrayList<>();

    private BossShip bossShip = null;

    public EnemyShipManager(GamePane gamePane) {
        paneList = gamePane.getChildren();
        this.isMovingLeft = false;
        this.isMovingRight = true;
    }

    public void generateEnemyShips() {
        double currentX = 130;
        double currentY = 330;

        double initialX = currentX;
        int margin = 40;
        EnemyLevel[] enemyLevels = new EnemyLevel[]{EnemyLevel.FRONT, EnemyLevel.MIDDLE, EnemyLevel.BEHIND};

        for (EnemyLevel enemyLevel : enemyLevels) {
            for (int i = 0; i < 2; i++) {
                currentY -= 40;
                currentX = initialX;
                for (int j = 0; j < 8; j++) {
                    EnemyShip enemyShip = new EnemyShip(this, enemyLevel, currentX, currentY);
                    paneList.add(enemyShip);
                    originalEnemyShips.add(enemyShip);
                    currentX += enemyShip.getWidth() + margin;
                }
            }
        }
    }

    public void generateBossShip() {
        if (isBossMode) {
            return;
        }
        isBossMode = true;
        double currentX = (GamePane.WIDTH / 2.0) - 30;
        double currentY = 80;
        this.bossShip = new BossShip(this, currentX, currentY);
        paneList.add(bossShip);
    }

    public void update() {
        if (shouldMoveLeft) {
            moveLeft();
        }
        if (shouldMoveRight) {
            moveRight();
        }
        if (this.isBossMode()) {
            this.bossShip.update();
        } else {
            getEnemyShips().forEach(EnemyShip::update);
        }
    }

    public ArrayList<EnemyShip> getSurroundingEnemies(EnemyShip enemyShip) {
        ArrayList<EnemyShip> surroundingEnemies = new ArrayList<>();
        surroundingEnemies.add(enemyShip);
        int currentIndex = originalEnemyShips.indexOf(enemyShip);
        int rightIndex = currentIndex + 1;
        int topIndex = currentIndex + 8;
        int topRightIndex = topIndex + 1;

        if (rightIndex < originalEnemyShips.size()) {
            surroundingEnemies.add(originalEnemyShips.get(rightIndex));
        }
        if (topIndex >= 0 && topIndex < originalEnemyShips.size()) {
            surroundingEnemies.add(originalEnemyShips.get(topIndex));
        }
        if (topRightIndex >= 0 && topRightIndex < originalEnemyShips.size()) {
            surroundingEnemies.add(originalEnemyShips.get(topRightIndex));
        }
        return surroundingEnemies;
    }
    public BossShip getBossShip() {
        return this.bossShip;
    }

    public ArrayList<EnemyShip> getEnemyShips() {
        ArrayList<EnemyShip> enemyShips = new ArrayList<>();
        for (int i = 0; i < paneList.size(); i++) {
            try {
                Node node = paneList.get(i);
                if (node instanceof EnemyShip) {
                    enemyShips.add((EnemyShip) node);
                }
            } catch (IndexOutOfBoundsException e) {
                logger.warn(e.getMessage());
                break;
            }
        }
        return enemyShips;
    }

    private void moveLeft() {
        this.isMovingRight = false;
        this.isMovingLeft = true;
        this.shouldMoveLeft = false;
        moveAllShipDown();
    }

    private void moveRight() {
        this.isMovingRight = true;
        this.isMovingLeft = false;
        this.shouldMoveRight = false;
        moveAllShipDown();
    }

    private void moveAllShipDown() {
        for (Iterator<EnemyShip> i = getEnemyShips().iterator(); i.hasNext(); ) {
            EnemyShip enemyShip = i.next();
            enemyShip.moveDown();
        }
    }

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public void boundaryHitRight() {
        if (this.isMovingRight) {
            this.shouldMoveLeft = true;
        }
    }

    public void boundaryHitLeft() {
        if (this.isMovingLeft) {
            // Should move Right but wait for update to finish
            this.shouldMoveRight = true;
        }
    }

    public boolean isBossMode() {
        return isBossMode && this.bossShip != null;
    }

    public boolean isBossDead() {
        return isBossMode && this.bossShip != null && this.bossShip.isDead();
    }
}
