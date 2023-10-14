package com.se233.spaceinvader.views.elements;

import com.se233.spaceinvader.Launcher;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.Logger;

public class SpriteSheetAnimator extends ImageView {
    private final int count;
    private int x;
    private int y;
    private final int initialX;
    private int currentIndex;
    private final int WIDTH = 15, HEIGHT = 15;

    Logger logger = org.apache.logging.log4j.LogManager.getLogger(SpriteSheetAnimator.class);

    public SpriteSheetAnimator(int startColumn, int startRow, int numColumns, int imgSize) {
        this.setImage(new Image(Launcher.class.getResourceAsStream("assets/sprites/invaders-sheet.png")));
        this.count = numColumns;
        this.x = this.initialX = WIDTH * (startColumn - 1);
        logger.debug("x:" + x);
        this.y = HEIGHT * (startRow - 1);
        this.currentIndex = 0;
        this.setFitHeight(imgSize);
        this.setFitWidth(imgSize);

        this.setViewport(new Rectangle2D(this.x, this.y, WIDTH, HEIGHT));
    }

    public SpriteSheetAnimator(Image image, int startColumn, int startRow, int numColumns) {
        this.setImage(image);
        this.count = numColumns;
        this.x = this.initialX = WIDTH * startColumn;
        this.y = HEIGHT * startRow;
        this.currentIndex = 0;

        this.setViewport(new Rectangle2D(this.x, this.y, WIDTH, HEIGHT));
    }

    public void tick() {
        logger.debug("x:" + x + " y:" + y);
        if (this.currentIndex == this.count - 1) {
            this.currentIndex = 0;
            this.x = this.initialX;
        } else {
            this.x += WIDTH;
            this.currentIndex++;
        }
        this.setViewport(new Rectangle2D(x, y, WIDTH, HEIGHT));
    }
}
