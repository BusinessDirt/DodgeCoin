package businessdirt.dodgecoin.renderer;

import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.renderer.components.RectangleComponent;
import businessdirt.dodgecoin.renderer.components.TextureComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Renderer {

    // instantiation
    private static Renderer instance;
    private OrthographicCamera camera;
    private Viewport viewport;

    // qol constants
    public static int USE_ORIGINAL_SIZE = -1;

    // sub renderer objects
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;

    // Arrays to store the objects that shall be rendered
    private final Array<TextureComponent> textures;
    private final Array<RectangleComponent> rectangles;

    private Renderer() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        viewport = new FillViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        textures = new Array<>();
        rectangles = new Array<>();
    }

    public void render(float dt) {

        // update the camera
        camera.update();

        // draw Shapes
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin();

        for (RectangleComponent r : rectangles) {
            if (r.isFill()) shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight(), r.getColor(), r.getColor(), r.getColor(), r.getColor());
            shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        }

        shapeRenderer.end();

        // draw Images
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (TextureComponent t : textures) {
            spriteBatch.draw(t.getTextureRegion(), t.getPosition().x, t.getPosition().y, t.getWidth(), t.getHeight());
        }

        spriteBatch.end();

        textures.clear();
        rectangles.clear();
    }

    public void resize (int width, int height) {
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
        rectangles.add(new RectangleComponent(x, y, width, height, false, Color.BLACK));
    }

    public void drawRect(int x, int y, int width, int height, Color color) {
        rectangles.add(new RectangleComponent(x, y, width, height, false, color));
    }

    public void fillRect(int x, int y, int width, int height) {
        rectangles.add(new RectangleComponent(x, y, width, height, true, Color.BLACK));
    }

    public void fillRect(int x, int y, int width, int height, Color color) {
        rectangles.add(new RectangleComponent(x, y, width, height, true, color));
    }

    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();

        for (TextureComponent t : textures) t.dispose();
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
