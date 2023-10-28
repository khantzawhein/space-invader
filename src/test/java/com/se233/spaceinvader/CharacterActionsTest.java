package com.se233.spaceinvader;

import com.se233.spaceinvader.controllers.DrawLoop;
import com.se233.spaceinvader.controllers.EnemyShipDrawLoop;
import com.se233.spaceinvader.controllers.GameLoop;
import com.se233.spaceinvader.views.GamePane;
import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterActionsTest {

    private GamePane gamePane;
    private GameLoop gameLoop;
    private DrawLoop drawLoop;
    private Method gameLoopUpdate, drawLoopUpdate;
    private EnemyShipDrawLoop enemyShipDrawLoop;

    @BeforeEach
    public void setup() throws NoSuchMethodException {
        JFXPanel jfxPanel = new JFXPanel();
        gamePane = new GamePane();
        gameLoop = new GameLoop(gamePane);
        drawLoop = new DrawLoop(gamePane);
        enemyShipDrawLoop = new EnemyShipDrawLoop(gamePane);
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
    public void characterCanShootBullet() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Thread.sleep(1000);
        Launcher.key.add(KeyCode.SPACE);
        gameLoopUpdate.invoke(gameLoop);
        drawLoopUpdate.invoke(drawLoop);
        assertEquals(1, gamePane.getBullets().size());
    }

    @Test
    public void characterCanShootAndHitEnemyShip() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        Thread.sleep(1000);

        Launcher.key.add(KeyCode.SPACE);
        enemyShipDrawLoop.update();
        gameLoopUpdate.invoke(gameLoop);
        drawLoopUpdate.invoke(drawLoop);

        long currentTime = System.currentTimeMillis();
        int previousEnemyCount = gamePane.getEnemyShipManager().getEnemyShips().size();

        while (System.currentTimeMillis() - currentTime < 5000) {
            enemyShipDrawLoop.update();
            gameLoopUpdate.invoke(gameLoop);
            drawLoopUpdate.invoke(drawLoop);
            if (gamePane.getEnemyShipManager().getEnemyShips().size() < previousEnemyCount) {
                assertTrue(true);
                return;
            }
        }
        fail();
    }

    @Test
    public void playerScoreShouldIncreaseWhenEnemyHasBeenKilled() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        int score = gamePane.getScore().getScore();
        this.characterCanShootAndHitEnemyShip();

        assertTrue(gamePane.getScore().getScore() > score);
    }
}
