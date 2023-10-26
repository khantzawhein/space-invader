package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.Launcher;
import com.se233.spaceinvader.views.GamePane;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PowerUpRocketCount extends HBox {
    private final Text displayText;
    public PowerUpRocketCount() {
        super();
        this.setAlignment(Pos.CENTER);
        this.setTranslateY(GamePane.GROUND + 5);
        this.setTranslateX(20);
        ImageView imageView = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/sprites/rocket-icon.png")));
        imageView.setFitHeight(30);
        imageView.setFitWidth(15);
        displayText = new Text("  x  0");
        displayText.setTextAlignment(TextAlignment.CENTER);
        displayText.setFill(Color.WHITE);
        displayText.setFont(Font.font("Space Invaders", 15));
        this.getChildren().addAll(imageView, displayText);
    }

    public void setCount(int count) {
        displayText.setText("  x  " + count);
    }
}
