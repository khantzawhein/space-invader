package com.se233.spaceinvader.models;

import com.se233.spaceinvader.enums.EnemyLevel;
import com.se233.spaceinvader.views.GamePane;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;

public class EnemyShipManager {
    private ObservableList<Node> paneList;
    private boolean isMovingLeft, isMovingRight, shouldMoveLeft, shouldMoveRight;

    public final static int ORIGINAL_ENEMY_COUNT = 60;

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
                for (int j = 0; j < 10; j++) {
                    EnemyShip enemyShip = new EnemyShip(this, enemyLevel, currentX, currentY);
                    paneList.add(enemyShip);
                    currentX += enemyShip.getWidth() + margin;
                }
            }
        }
    }

    public void update() {
        if (shouldMoveLeft) {
            moveLeft();
        }
        if (shouldMoveRight) {
            moveRight();
        }
        getEnemyShips().stream().forEach(EnemyShip::update);

    }

    public ArrayList<EnemyShip> getEnemyShips() {
        Iterator<Node> paneListIterator  = new ArrayList<>(paneList).iterator();
        ArrayList<EnemyShip> enemyShips = new ArrayList<>();
        while (paneListIterator.hasNext())  {
            Node node;
            if ((node = paneListIterator.next()) instanceof EnemyShip) {
                enemyShips.add((EnemyShip) node);
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
        for (Iterator<EnemyShip> i = getEnemyShips().iterator(); i.hasNext();) {
            EnemyShip enemyShip = i.next();
            enemyShip.moveDown();
        }
    }

    public void addEnemyShip(EnemyShip enemyShip) {
        getEnemyShips().add(enemyShip);
    }

    public void removeEnemyShip(EnemyShip enemyShip) {
        getEnemyShips().remove(enemyShip);
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


}
