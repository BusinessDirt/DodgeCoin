package businessdirt.dodgecoin.core.game;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Button {

    private static final JLabel listener = new JLabel();

    private Rectangle bounds;
    private final String name;

    public Button(int x, int y, int width, int height, String name) {
        this.bounds = new Rectangle(x, y, width, height);
        this.name = name;
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

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Button button = (Button) o;
        return Objects.equals(name, button.name) && Objects.equals(bounds, button.bounds);
    }

    @Override
    public String toString() {
        return "Button[" + "x=" + this.bounds.x + ", y=" + this.bounds.y +
                ", width=" + this.bounds.width + ", height=" + this.bounds.height + ", hash=" + this.name + ']';
    }
}
