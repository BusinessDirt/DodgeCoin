package businessdirt.dodgecoin.gui.images;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends Sprite {

    public final CoinType type;

    public Coin(int x, int y, int width, int height, BufferedImage image, CoinType type) {
        super(x, y, width, height, image);
        this.type = type;
    }

    public Coin(int x, int y, BufferedImage image, CoinType type) {
        super(x, y, image);
        this.type = type;
    }

    public Coin(Point position, Point size, BufferedImage image, CoinType type) {
        super(position, size, image);
        this.type = type;
    }

    public static enum CoinType {
        BITCOIN, DOGECOIN
    }
}
