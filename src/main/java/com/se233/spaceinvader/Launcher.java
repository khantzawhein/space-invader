package com.se233.spaceinvader;

import com.se233.spaceinvader.controllers.DrawLoop;
import com.se233.spaceinvader.controllers.GameLoop;
import com.se233.spaceinvader.models.Key;
import com.se233.spaceinvader.views.GamePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static final Key key = new Key();
    @Override
    public void start(Stage stage) throws Exception {
        Font font = Font.loadFont(getClass().getResourceAsStream("assets/fonts/space_invaders.ttf"), 10);
        stage.setResizable(false);
        GamePane gamePane = new GamePane();
        GameLoop gameLoop = new GameLoop(gamePane);
        DrawLoop drawLoop = new DrawLoop(gamePane);
        Scene scene = new Scene(gamePane);
        scene.setOnKeyPressed(keyEvent -> key.add(keyEvent.getCode()));
        scene.setOnKeyReleased(keyEvent -> key.remove(keyEvent.getCode()));
        stage.setScene(scene);
        stage.setTitle("Space Invaders");
        stage.show();

        (new Thread(gameLoop)).start();
        (new Thread(drawLoop)).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
