package com.se233.spaceinvader.controllers;

import com.se233.spaceinvader.views.elements.Bullet;
import com.se233.spaceinvader.views.GamePane;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DrawLoop implements Runnable {
    private final Logger logManager = LogManager.getLogger(DrawLoop.class);
    private GamePane gamePane;
    private final int fps = 60;

    public DrawLoop(GamePane gamePane) {
        this.gamePane = gamePane;
    }
    private void update() {
        gamePane.getPlayer().update();
        ArrayList<Bullet> bullets = gamePane.getChildren().stream().filter(node -> node instanceof Bullet).map(n -> (Bullet) n).collect(Collectors.toCollection(ArrayList::new));
        for (Bullet bullet: bullets) {
            bullet.update();

            if (bullet.isOutOfBound()) {
                Platform.runLater(() -> gamePane.getChildren().remove(bullet));
                logManager.debug("Bullet removed, position: " + bullet.getTranslateY());
            }
        }
    }
    @Override
    public void run() {
        while (true) {
            this.update();
            try {
                float delay = 1000f / fps;
                Thread.sleep((long) delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
