package businessdirt.dodgecoin.core.renderer.components;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Holds values of textures that shall be drawn.
 */
public class TextureComponent {

    private final Vector2 position;
    private final Vector2 size;
    private final Texture texture;
    private final TextureRegion region;

    public TextureComponent(Texture texture, int x, int y, int width, int height) {
        this.texture = texture;
        this.region = new TextureRegion(this.texture);
        this.position = new Vector2(x, y);
        this.size = new Vector2(width == Renderer.USE_ORIGINAL_SIZE ? this.texture.getWidth() : width,
                height == Renderer.USE_ORIGINAL_SIZE ? this.texture.getHeight() : height);
    }

    public TextureComponent(String path, int x, int y, int width, int height) {
        this.texture = DodgeCoin.assets.get(path, Texture.class);
        this.region = new TextureRegion(this.texture);
        this.position = new Vector2(x, y);
        this.size = new Vector2(width == Renderer.USE_ORIGINAL_SIZE ? this.texture.getWidth() : width,
                height == Renderer.USE_ORIGINAL_SIZE ? this.texture.getHeight() : height);
    }

    public TextureComponent(TextureRegion region, int x, int y, int width, int height) {
        this.region = region;
        this.texture = region.getTexture();
        this.position = new Vector2(x, y);
        this.size = new Vector2(width == Renderer.USE_ORIGINAL_SIZE ? this.texture.getWidth() : width,
                height == Renderer.USE_ORIGINAL_SIZE ? this.texture.getHeight() : height);
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getWidth() {
        return (int) size.x;
    }

    public int getHeight() {
        return (int) size.y;
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
