package com.se233.spaceinvader.models;

import com.se233.spaceinvader.views.elements.SpriteSheetAnimator;
import javafx.scene.layout.Pane;

public class BossShip extends Pane {
    private final SpriteSheetAnimator spriteSheetAnimator;
    public BossShip() {
        super();
        this.spriteSheetAnimator = new SpriteSheetAnimator(1, 1, 3, 30);
    }
}
