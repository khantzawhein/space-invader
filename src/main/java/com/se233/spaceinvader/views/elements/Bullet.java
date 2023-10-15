package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.enums.BulletType;
import com.se233.spaceinvader.views.GamePane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {
    private final BulletType type;
    private final int SPEED = 5;

    public Bullet(int x, BulletType type) {
        super(2, 20, type == BulletType.PLAYER ? Color.WHITE : Color.RED);
        this.setTranslateX(x);
        this.setTranslateY(GamePane.GROUND - GamePane.PLAYER_WIDTH - 10);
        this.type = type;
    }

    public Bullet(int x, int y, BulletType type) {
        super(2, 20, type == BulletType.PLAYER ? Color.WHITE : Color.RED);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.type = type;
    }

    public void update() {
        if (type == BulletType.PLAYER) {
            this.setTranslateY(this.getTranslateY() - SPEED);
        } else {
            this.setTranslateY(this.getTranslateY() + SPEED);
        }
    }

    public boolean isOutOfBound() {
        return (this.getType() == BulletType.PLAYER && this.getTranslateY() < 0) ||
                (this.getType() == BulletType.ENEMY && this.getTranslateY() > GamePane.GROUND);
    }

    public BulletType getType() {
        return type;
    }
}
