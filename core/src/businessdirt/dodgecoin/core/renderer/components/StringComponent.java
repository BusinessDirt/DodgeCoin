package businessdirt.dodgecoin.core.renderer.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Holds values of strings that shall be drawn.
 */
public class StringComponent {

    private final Vector2 position;
    private final Vector2 size;
    private boolean centered;
    private final Color color;
    private final String string;

    public StringComponent(String string, int x, int y, Color color) {
        this.position = new Vector2(x, y);
        this.size = new Vector2();
        this.color = color;
        this.string = string;
        this.centered = false;
    }

    public int getX() {
        return (int) this.position.x;
    }

    public int getY() {
        return (int) this.position.y;
    }

    public String getString() {
        return string;
    }

    public Color getColor() {
        return color;
    }

    public void center(int width, int height) {
        this.centered = true;
        this.size.set(width, height);
    }

    public boolean isCentered() {
        return this.centered;
    }

    public Vector2 getSize() {
        return this.size;
    }

}
