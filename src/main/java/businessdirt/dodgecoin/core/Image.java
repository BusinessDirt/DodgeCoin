package businessdirt.dodgecoin.core;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Image {

    private int x, y;
    private int width, height;
    private BufferedImage image;
    private boolean draw;

    public Image(int x, int y, int width, int height, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.draw = true;
    }

    public Image(int x, int y, BufferedImage image) {
        this(x, y, image.getWidth(), image.getHeight(), image);
    }

    public Image(Point position, Point size, BufferedImage image) {
        this(position.x, position.y, size.x, size.y, image);
    }

    public boolean intersects(Image image) {
        Rectangle r1 = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle r2 = new Rectangle(image.x, image.y, image.width, image.height);
        return r1.intersects(r2);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}