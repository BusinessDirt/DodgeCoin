package businessdirt.dodgecoin.core.game;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Button {

    private static final JLabel listener = new JLabel();

    private Rectangle bounds;
    private final String imageName;

    public Button(int x, int y, int width, int height, String image) {
        this.bounds = new Rectangle(x, y, width, height);
        this.imageName = image;
    }

    public boolean contains(Point point) {
        return this.bounds.contains(point);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public String getImageName() {
        return this.imageName;
    }

    @Override
    public int hashCode() {
        return this.imageName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Button button = (Button) o;
        return Objects.equals(imageName, button.imageName) && Objects.equals(bounds, button.bounds);
    }

    @Override
    public String toString() {
        return "Button[" + "x=" + this.bounds.x + ", y=" + this.bounds.y +
                ", width=" + this.bounds.width + ", height=" + this.bounds.height + ", hash=" + this.imageName + ']';
    }
}
