package com.se233.spaceinvader.views.elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DeadLine extends Rectangle {
    public DeadLine() {
        super();
        this.setWidth(600);
        this.setHeight(1);
        this.setX(0);
        this.setY(640);

        this.setFill(Color.RED);
    }
}
