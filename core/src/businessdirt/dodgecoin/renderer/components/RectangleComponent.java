package businessdirt.dodgecoin.renderer.components;

import com.badlogic.gdx.graphics.Color;

public class RectangleComponent {

    private int x, y;
    private int width, height;
    private Color color;
    private boolean fill;

    public RectangleComponent(int x, int y, int width, int height, boolean fill, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fill = fill;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFill() {
        return fill;
    }

    public Color getColor() {
        return color;
    }
}
