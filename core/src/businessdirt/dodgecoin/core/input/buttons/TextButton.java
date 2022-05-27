package businessdirt.dodgecoin.core.input.buttons;

import businessdirt.dodgecoin.core.input.Mouse;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.awt.*;

import static businessdirt.dodgecoin.core.renderer.Renderer.USE_DEFAULT_FONT;
import static businessdirt.dodgecoin.core.renderer.Renderer.USE_DEFAULT_FONT_SIZE;

public class TextButton {

    private Vector2 position;
    private Vector2 size;
    private String text;
    private Button button;

    private String fontPath;
    private int fontSize;
    private Sound clickSound;
    private BorderDescriptor border;
    private Color backgroundColor;

    public TextButton(String text, int x, int y, int width, int height) {
        this.text = text;
        this.position = new Vector2(x, y);
        this.size = new Vector2(width, height);

        this.button = new Button();
        this.button.setPosition(position.x, position.y);
        this.button.setSize(size.x, size.y);

        this.fontPath = USE_DEFAULT_FONT;
        this.fontSize = USE_DEFAULT_FONT_SIZE;
        this.clickSound = null;
        this.border = new BorderDescriptor(3, Color.WHITE);
        this.backgroundColor = Color.BLACK;
    }

    public void draw() {
        Renderer.get().fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y, this.backgroundColor);
        Renderer.get().drawRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y, this.border.getThickness(), this.border.getColor());
        Renderer.get().drawCenteredString(text, (int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y, Color.WHITE);
    }

    public boolean isClicked() {
        boolean tmp = Mouse.clicked(new Rectangle((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y));
        if (clickSound != null && tmp) clickSound.play();
        return tmp;
    }

    public void setSound(Sound clickSound) {
        this.clickSound = clickSound;
    }

    public void setBorder(BorderDescriptor border) {
        this.border = border;
    }

    public void setBorderColor(Color color) {
        this.border.getColor().set(color);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setFont(String fontPath, int fontSize) {
        this.fontPath = fontPath;
        this.fontSize = fontSize;
    }
}
