package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.renderer.Renderer;
import com.badlogic.gdx.graphics.Texture;

public class GameObject {

    protected String texturePath;
    protected int x, y;
    protected int width, height;

    public GameObject(String texturePath, int x, int y, int width, int height) {
        this.texturePath = texturePath;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw() {
        Renderer.get().drawImage(DodgeCoin.assets.get(this.texturePath, Texture.class), (int) this.x, (int) this.y, this.width, this.height);
    }

    public boolean intersects(GameObject gameObject) {
        return x < gameObject.x + gameObject.width && x + width > gameObject.x
                && y < gameObject.y + gameObject.height && y + height > gameObject.y;
    }
}
