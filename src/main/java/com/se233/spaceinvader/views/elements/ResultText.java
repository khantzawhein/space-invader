package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.views.GamePane;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ResultText extends StackPane {
    public ResultText(String s) {
        super();
        this.setPrefSize(GamePane.WIDTH, 100);
        this.setTranslateY(50);
        Text text = new Text(s);

        text.setText(s);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Space Invaders", 15));

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(text);

    }
    public static ResultText gameOver() {
        return new ResultText("Game Over!");
    }

    public static ResultText win() {
        return new ResultText("You Win!");
    }
}
