package businessdirt.dodgecoin.core.input.buttons;

import businessdirt.dodgecoin.core.input.Mouse;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.awt.*;
import java.util.Objects;

public class ImageButton {

    private final Vector2 position;
    private final Vector2 size;
    private String texture;

    private Sound clickSound;
    private BorderDescriptor border;
    private Color backgroundColor;
    private boolean enabled;

    public ImageButton(String texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.size = new Vector2(width, height);

        Button button = new Button();
        button.setPosition(position.x, position.y);
        button.setSize(size.x, size.y);

        this.clickSound = null;
        this.border = new BorderDescriptor(3, Color.WHITE);
        this.backgroundColor = Color.BLACK;
        this.enabled = true;
    }

    public void draw() {
        if (this.enabled) {
            Renderer.get().fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y, this.backgroundColor);
            Renderer.get().drawRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y, this.border.getThickness(), this.border.getColor());
            if (!Objects.equals("", texture))
                Renderer.get().drawImage(texture, (int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
        }
    }

    public boolean isClicked() {
        boolean tmp = Mouse.clicked(new Rectangle((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y));
        if (clickSound != null && tmp && enabled) clickSound.play();
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

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getTexture() {
        return this.texture;
    }

    public void setEnabled(boolean state) {
        this.enabled = state;
    }
}
