package businessdirt.dodgecoin.renderer.components;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static businessdirt.dodgecoin.renderer.Renderer.USE_ORIGINAL_SIZE;

public class TextureComponent {

    private Vector2 position;
    private Vector2 previousPosition;
    private int width, height;
    private Texture texture;
    private TextureRegion region;

    public TextureComponent(Texture texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.region = new TextureRegion(this.texture);
        this.position = new Vector2(x, y);
        this.previousPosition = new Vector2();
        this.width = width == USE_ORIGINAL_SIZE ? this.texture.getWidth() : width;
        this.height = height == USE_ORIGINAL_SIZE ? this.texture.getHeight() : height;
    }

    public TextureComponent(String path, int x, int y, int width, int height) {
        this.texture = DodgeCoin.assets.get(path, Texture.class);
        this.region = new TextureRegion(this.texture);
        this.position = new Vector2(x, y);
        this.width = width == USE_ORIGINAL_SIZE ? this.texture.getWidth() : width;
        this.height = height == USE_ORIGINAL_SIZE ? this.texture.getHeight() : height;
    }

    public TextureComponent(TextureRegion region, int x, int y, int width, int height) {
        this.region = region;
        this.texture = region.getTexture();
        this.position = new Vector2(x, y);
        this.width = width == USE_ORIGINAL_SIZE ? this.texture.getWidth() : width;
        this.height = height == USE_ORIGINAL_SIZE ? this.texture.getHeight() : height;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Texture getTexture() {
        return texture;
    }

    public TextureRegion getTextureRegion() {
        return this.region;
    }

    public void dispose() {
        this.texture.dispose();
    }
}
