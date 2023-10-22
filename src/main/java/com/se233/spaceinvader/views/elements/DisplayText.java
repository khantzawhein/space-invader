package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.enums.DisplayTextAnimation;
import com.se233.spaceinvader.views.GamePane;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DisplayText extends StackPane {
    private String text;
    private long lastTickTime;
    private int tickCount = 0;
    private Text displayText;
    private int tickDuration;
    private DisplayTextAnimation displayTextAnimation;

    public DisplayText(String s, int tickDuration, DisplayTextAnimation displayTextAnimation) {
        super();
        this.tickDuration = tickDuration;
        this.displayTextAnimation = displayTextAnimation;
        this.setPrefSize(GamePane.WIDTH, 100);
        this.setTranslateY(50);
        this.text = s;
        if (displayTextAnimation == DisplayTextAnimation.TICKING) {
            displayText = new Text();
        } else {
            displayText = new Text(s);
        }
        lastTickTime = System.currentTimeMillis();
        displayText.setTextAlignment(TextAlignment.CENTER);
        displayText.setFill(Color.WHITE);
        displayText.setFont(Font.font("Space Invaders", 15));

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(displayText);

    }

    public static DisplayText gameOver() {
        return new DisplayText("You Died!\n\nGame Over!", 100, DisplayTextAnimation.TICKING);
    }

    public static DisplayText win() {
        return new DisplayText("All Enemies Cleared\n\nYou Win!", 100, DisplayTextAnimation.TICKING);
    }

    public static DisplayText bossComing() {
        return new DisplayText("Warning:\n\nBoss is coming...", 100, DisplayTextAnimation.TICKING);
    }

    public static DisplayText startText() {
        return new DisplayText("Press SPACE to start the game!", 800, DisplayTextAnimation.BLINKING);
    }

    public boolean isTickDone() {
        return tickCount >= text.length();
    }

    public void tick() {
        if (System.currentTimeMillis() - lastTickTime >= this.tickDuration) {
            lastTickTime = System.currentTimeMillis();
            if (this.displayTextAnimation == DisplayTextAnimation.TICKING) {
                tickText();
            } else if (this.displayTextAnimation == DisplayTextAnimation.BLINKING) {
                displayText.setVisible(!displayText.isVisible());
            }
        }

    }

    private void tickText() {
        tickCount++;
        if (tickCount <= text.length()) {
            displayText.setText(this.text.substring(0, tickCount));
        }
    }
}
