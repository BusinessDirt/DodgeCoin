package businessdirt.dodgecoin.input;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.renderer.Renderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import java.awt.*;

public class ImageButton {

    private Vector2 position;
    private Vector2 size;
    private Texture texture;
    private Button button;

    public ImageButton(Texture texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.size = new Vector2(width, height);

        this.button = new Button();
        this.button.setPosition(position.x, position.y);
        this.button.setSize(size.x, size.y);
    }

    public ImageButton(String path, int x, int y, int width, int height) {
        this(DodgeCoin.assets.get(path, Texture.class), x, y, width, height);
    }

    public void draw() {
        Renderer.get().fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y, Color.BLACK);
        Renderer.get().drawImage(texture, (int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
    }

    public boolean isClicked() {
        return Mouse.clicked(new Rectangle((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y));
    }
}
