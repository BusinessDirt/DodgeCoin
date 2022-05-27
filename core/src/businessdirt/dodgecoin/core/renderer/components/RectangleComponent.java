package businessdirt.dodgecoin.core.renderer.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class RectangleComponent {

    private Vector2 position;
    private Vector2 size;
    private int thickness;
    private Color color;
    private boolean fill;

    public RectangleComponent(int x, int y, int width, int height, int thickness, boolean fill, Color color) {
        this.position = new Vector2(x, y);
        this.size = new Vector2(width, height);
        this.fill = fill;
        this.color = color;
        this.thickness = thickness;
    }

    public int getX() {
        return (int) position.x;
    }

    public int getY() {
        return (int) position.y;
    }

    public int getWidth() {
        return (int) size.x;
    }

    public int getHeight() {
        return (int) size.y;
    }

    public boolean isFill() {
        return fill;
    }

    public Color getColor() {
        return color;
    }

    public int getThickness() {
        return thickness;
    }
}
