package com.se233.spaceinvader;

import com.se233.spaceinvader.controllers.DrawLoop;
import com.se233.spaceinvader.controllers.GameLoop;
import com.se233.spaceinvader.models.PlayerShip;
import com.se233.spaceinvader.views.GamePane;
import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterTesting {
    private GamePane gamePane;
    private GameLoop gameLoop;
    private DrawLoop drawLoop;
    private Method gameLoopUpdate, drawLoopUpdate;

    @BeforeEach
    public void setup() throws NoSuchMethodException, InterruptedException {
        JFXPanel jfxPanel = new JFXPanel();
        gamePane = new GamePane();
        gameLoop = new GameLoop(gamePane);
        drawLoop = new DrawLoop(gamePane);
        Launcher.key.removeAll();
        gamePane.start();
        try {
            gameLoopUpdate = GameLoop.class.getDeclaredMethod("update");
            gameLoopUpdate.setAccessible(true);
            drawLoopUpdate = DrawLoop.class.getDeclaredMethod("update");
            drawLoopUpdate.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            drawLoopUpdate = gameLoopUpdate = null;
        }
    }

    @Test
    public void characterShouldMoveToLeftWhenLeftKeyIsPressed() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        double currentX = gamePane.getPlayer().getTranslateX();
        Launcher.key.add(KeyCode.LEFT);
        gameLoopUpdate.invoke(gameLoop);
        drawLoopUpdate.invoke(drawLoop);
        Thread.sleep(500);
        assertEquals(currentX - PlayerShip.SPEED, gamePane.getPlayer().getTranslateX());
    }

    @Test
    public void characterShouldMoveToRightWhenRightKeyIsPressed() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        double currentX = gamePane.getPlayer().getTranslateX();
        System.out.println(gamePane.getPlayer().getTranslateX());
        Launcher.key.add(KeyCode.RIGHT);
        gameLoopUpdate.invoke(gameLoop);
        drawLoopUpdate.invoke(drawLoop);
        Thread.sleep(500);
        assertEquals(currentX + PlayerShip.SPEED, gamePane.getPlayer().getTranslateX());
    }

    @Test
    public void characterShouldNotMoveToRightWhenRightKeyIsNotPressed() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        double currentX = gamePane.getPlayer().getTranslateX();
        Launcher.key.remove(KeyCode.RIGHT);
        gameLoopUpdate.invoke(gameLoop);
        drawLoopUpdate.invoke(drawLoop);
        Thread.sleep(500);
        assertEquals(currentX, gamePane.getPlayer().getTranslateX());
    }

    @Test
    public void characterShouldNotMoveToLeftWhenLeftKeyIsNotPressed() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        double currentX = gamePane.getPlayer().getTranslateX();
        Launcher.key.remove(KeyCode.LEFT);
        gameLoopUpdate.invoke(gameLoop);
        drawLoopUpdate.invoke(drawLoop);
        Thread.sleep(500);
        assertEquals(currentX, gamePane.getPlayer().getTranslateX());
    }
}
