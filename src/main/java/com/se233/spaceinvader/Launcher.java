package com.se233.spaceinvader;

import com.se233.spaceinvader.controllers.DrawLoop;
import com.se233.spaceinvader.controllers.EnemyShipDrawLoop;
import com.se233.spaceinvader.controllers.GameLoop;
import com.se233.spaceinvader.models.Key;
import com.se233.spaceinvader.views.GamePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher extends Application {
    public static final Key key = new Key();
    private GameLoop gameLoop;
    private DrawLoop drawLoop;
    private EnemyShipDrawLoop enemyShipDrawLoop;
    private static GamePane gamePane;

    @Override
    public void start(Stage stage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("assets/fonts/space_invaders.ttf"), 10);
        stage.setResizable(false);
        gamePane = new GamePane();
        gameLoop = new GameLoop(gamePane);
        drawLoop = new DrawLoop(gamePane);
        enemyShipDrawLoop = new EnemyShipDrawLoop(gamePane);

        Scene scene = new Scene(gamePane);
        scene.setOnKeyPressed(keyEvent -> key.add(keyEvent.getCode()));
        scene.setOnKeyReleased(keyEvent -> key.remove(keyEvent.getCode()));
        stage.setScene(scene);
        stage.setTitle("Space Invaders");
        stage.show();

        (new Thread(gameLoop)).start();
        (new Thread(drawLoop)).start();
        (new Thread(enemyShipDrawLoop)).start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        gameLoop.stop();
        drawLoop.stop();
        enemyShipDrawLoop.stop();
    }

    public static GamePane getGamePane() {
        return gamePane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
