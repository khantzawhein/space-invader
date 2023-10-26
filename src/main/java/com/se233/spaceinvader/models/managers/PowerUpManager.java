package com.se233.spaceinvader.models.managers;

import com.se233.spaceinvader.views.GamePane;
import com.se233.spaceinvader.views.elements.RocketDrop;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Iterator;

public class PowerUpManager {
    private GamePane gamePane;
    private long lastGenerateTime;
    private ArrayList<RocketDrop> rocketDrops;
    private int playerRocketPowerUpCount = 0;

    public PowerUpManager(GamePane gamePane) {
        this.rocketDrops = new ArrayList<>();
        this.gamePane = gamePane;
        this.lastGenerateTime = System.currentTimeMillis();
    }

    public void generatePowerUp() {
        if (Math.random() < 0.003 && playerRocketPowerUpCount < 3
                && !gamePane.getEnemyShipManager().isBossMode()
                && System.currentTimeMillis() - lastGenerateTime > 5000) {
            lastGenerateTime = System.currentTimeMillis();
            double randomX = Math.random() * (GamePane.WIDTH - 50);
            RocketDrop rocketDrop = new RocketDrop(randomX, 0);
            rocketDrops.add(rocketDrop);
            Platform.runLater(() -> gamePane.getChildren().add(rocketDrop));
        }
    }

    public void update() {
        ArrayList<RocketDrop> toRemove = new ArrayList<>();
        for (RocketDrop rocketDrop : rocketDrops) {
            Platform.runLater(rocketDrop::update);
            if (rocketDrop.isOutOfBound()) {
                Platform.runLater(() -> gamePane.getChildren().remove(rocketDrop));
                toRemove.add(rocketDrop);
            }
        }
        rocketDrops.removeAll(toRemove);
    }

    public ArrayList<RocketDrop> getRocketDrops() {
        return rocketDrops;
    }

    public int getPlayerRocketPowerUpCount() {
        return playerRocketPowerUpCount;
    }

    public void incrementPlayerRocketPowerUpCount() {
        this.playerRocketPowerUpCount++;
        Platform.runLater(() -> gamePane.getPowerUpRocketCount().setCount(playerRocketPowerUpCount));
    }

    public void decrementPlayerRocketPowerUpCount() {
        this.playerRocketPowerUpCount--;
        Platform.runLater(() -> gamePane.getPowerUpRocketCount().setCount(playerRocketPowerUpCount));
    }
    public void removeFromPane(RocketDrop rocketDrop) {
        Platform.runLater(() -> {
            this.gamePane.getChildren().remove(rocketDrop);
        });
        this.rocketDrops.remove(rocketDrop);
    }
}
