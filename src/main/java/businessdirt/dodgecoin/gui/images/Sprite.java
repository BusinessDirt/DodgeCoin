package businessdirt.dodgecoin.gui.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Sprite {

    private int x, y;
    private int width, height;
    private BufferedImage image;
    private boolean draw;

    public Sprite(int x, int y, int width, int height, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
        this.draw = true;
    }

    public Sprite(int x, int y, BufferedImage image) {
        this(x, y, image.getWidth(), image.getHeight(), image);
    }

    public Sprite(Point position, Point size, BufferedImage image) {
        this(position.x, position.y, size.x, size.y, image);
    }

    public boolean intersects(Sprite sprite) {
        Rectangle r1 = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle r2 = new Rectangle(sprite.x, sprite.y, sprite.width, sprite.height);
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

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    @Override
    public String toString() {
        return "Image[" + "x=" + x + ", y=" + y + ", width=" + width + ", height=" + height +
                ", image=" + image + ", draw=" + draw + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprite sprite1 = (Sprite) o;
        return width == sprite1.width && height == sprite1.height && draw == sprite1.draw && image.equals(sprite1.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height, image, draw);
    }
}
