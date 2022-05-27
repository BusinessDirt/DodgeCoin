package businessdirt.dodgecoin.core.input.buttons;

import com.badlogic.gdx.graphics.Color;

public class BorderDescriptor {

    private final int thickness;
    private final Color color;


    public BorderDescriptor(int thickness, Color color) {
        this.thickness = thickness;
        this.color = color;
    }

    public int getThickness() {
        return thickness;
    }

    public Color getColor() {
        return color;
    }
}
