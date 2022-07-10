package businessdirt.dodgecoin.core.renderer;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.core.renderer.components.RectangleComponent;
import businessdirt.dodgecoin.core.renderer.components.StringComponent;
import businessdirt.dodgecoin.core.renderer.components.TextureComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

@SuppressWarnings("GDXJavaFlushInsideLoop")
public class Renderer {

    /**
     * Single instance only.
     */
    private static Renderer instance;

    /**
     * Camera.
     */
    private final OrthographicCamera camera;

    /**
     * Tells the Renderer to use the original size of a Texture to render it.
     */
    public static int USE_ORIGINAL_SIZE = -1;

    /**
     * Render sprites.
     */
    private final SpriteBatch spriteBatch;

    /**
     * Render shapes.
     */
    private final ShapeRenderer shapeRenderer;

    /**
     * Render strings.
     */
    private final BitmapFont font;

    /**
     * Contains all information about textures that should be rendered in a frame.
     */
    private final Array<TextureComponent> textures;

    /**
     * Contains all information about rectangles that should be rendered in a frame.
     */
    private final Array<RectangleComponent> rectangles;

    /**
     * Contains all information about strings that should be rendered in a frame.
     */
    private final Array<StringComponent> strings;

    private Renderer() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        new FillViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);

        font = DodgeCoin.assets.get("fonts/8-bit.fnt", BitmapFont.class);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        textures = new Array<>();
        rectangles = new Array<>();
        strings = new Array<>();
    }

    public void render() {
        // sort the rectangle list to optimize speed
        rectangles.sort(new RectangleComponent.RectangleComparator());

        // update the camera
        camera.update();

        // draw Shapes
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin();
        for (RectangleComponent r : new Array.ArrayIterator<>(rectangles)) {
            if (r.isFill()) {
                // draw the rect filled
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight(), r.getColor(), r.getColor(), r.getColor(), r.getColor());
            } else {
                // draw the rect with lines
                shapeRenderer.set(ShapeRenderer.ShapeType.Line);
                shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight(), r.getColor(), r.getColor(), r.getColor(), r.getColor());
            }
        }
        shapeRenderer.end();

        // draw textures
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (TextureComponent t : new Array.ArrayIterator<>(textures)) {
            spriteBatch.draw(t.getTextureRegion(), t.getPosition().x, t.getPosition().y, t.getWidth(), t.getHeight());
        }

        // draw strings
        for (StringComponent s : new Array.ArrayIterator<>(strings)) {
            if (!s.isCentered()) {
                // not centered
                font.draw(spriteBatch, s.getString(), s.getX(), s.getY());
            } else {
                // centered
                GlyphLayout layout = new GlyphLayout(font, s.getString());
                font.draw(spriteBatch, layout, s.getX() + (s.getSize().x - layout.width) / 2, s.getY() + (s.getSize().y + layout.height) / 2);
            }
        }
        spriteBatch.end();

        // remove all information from the arrays
        // to not render the current frame in the next frame
        textures.clear();
        rectangles.clear();
        strings.clear();
    }


    public void resize(int width, int height) {
        Vector2 size = Scaling.fit.apply(1920, 1080, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int) size.x;
        int viewportHeight = (int) size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
    }

    public void drawImage(Texture image, int x, int y) {
        textures.add(new TextureComponent(image, x, y, image.getWidth(), image.getHeight()));
    }

    public void drawImage(Texture image, int x, int y, int width, int height) {
        textures.add(new TextureComponent(image, x, y, width, height));
    }

    public void drawImage(TextureRegion region, int x, int y) {
        textures.add(new TextureComponent(region, x, y, region.getRegionWidth(), region.getRegionHeight()));
    }

    public void drawImage(TextureRegion region, int x, int y, int width, int height) {
        textures.add(new TextureComponent(region, x, y, width, height));
    }

    public void drawImage(String path, int x, int y) {
        textures.add(new TextureComponent(path, x, y, -1, -1));
    }

    public void drawImage(String path, int x, int y, int width, int height) {
        textures.add(new TextureComponent(path, x, y, width, height));
    }

    public void drawRect(int x, int y, int width, int height) {
        rectangles.add(new RectangleComponent(x, y, width, height, 1, false, Color.BLACK));
    }

    public void drawRect(int x, int y, int width, int height, Color color) {
        rectangles.add(new RectangleComponent(x, y, width, height, 1, false, color));
    }

    public void drawRect(int x, int y, int width, int height, int thickness) {
        rectangles.add(new RectangleComponent(x, y, width, height, thickness, false, Color.BLACK));
    }

    public void drawRect(int x, int y, int width, int height, int thickness, Color color) {
        rectangles.add(new RectangleComponent(x, y, width, height, thickness, false, color));
    }

    public void fillRect(int x, int y, int width, int height) {
        rectangles.add(new RectangleComponent(x, y, width, height, 1, true, Color.BLACK));
    }

    public void fillRect(int x, int y, int width, int height, Color color) {
        rectangles.add(new RectangleComponent(x, y, width, height, 1, true, color));
    }

    public void drawString(String string, int x, int y) {
        strings.add(new StringComponent(string, x, y, Color.BLACK));
    }

    public void drawString(String string, int x, int y, Color color) {
        strings.add(new StringComponent(string, x, y, color));
    }

    public void drawCenteredString(String string, int x, int y, int width, int height, Color color) {
        StringComponent component = new StringComponent(string, x, y, color);
        component.center(width, height);
        strings.add(component);
    }

    public void dispose() {
        font.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();

        for (TextureComponent t : new Array.ArrayIterator<>(textures)) t.dispose();
    }

    public float getHeight() {
        return camera.viewportHeight;
    }

    public float getWidth() {
        return camera.viewportWidth;
    }

    public static Renderer get() {
        if (Renderer.instance == null) Renderer.instance = new Renderer();
        return Renderer.instance;
    }

    public static Renderer newInstance() {
        Renderer.instance = new Renderer();
        return Renderer.instance;
    }
}
