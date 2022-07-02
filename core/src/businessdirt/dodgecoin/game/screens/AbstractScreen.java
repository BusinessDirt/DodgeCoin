package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.core.input.Keyboard;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import java.awt.*;

public abstract class AbstractScreen implements Screen {

    /**
     * The stage contains all ui elements.
     */
    protected final Stage stage;

    /**
     * The viewport handles resizing.
     */
    protected final ScalingViewport viewport;

    /**
     * All needed styles for ui elements are defined in a skin.
     */
    protected final Skin skin;

    /**
     * The background color of the window.
     */
    protected final Color backgroundColor;

    protected AbstractScreen(Skin skin, Color backgroundColor) {
        // initialize everything
        this.stage = new Stage();
        this.viewport = new ScalingViewport(Scaling.fill, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.skin = skin;
        this.backgroundColor = backgroundColor;

        // Set the Input Processor to the stage for button click etc.
        Gdx.input.setInputProcessor(this.stage);
    }

    /** Called when this screen becomes the current screen for a {@link Game}. */
    @Override
    public abstract void show();

    /** Called when the screen should render itself.
     * @param delta The time in seconds since the last render. */
    @Override
    public void render(float delta) {
        // input
        Keyboard.defaultKeys();

        // clear the screen from the previous frame
        Gdx.gl.glClearColor(this.backgroundColor.r, this.backgroundColor.g, this.backgroundColor.b, this.backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // act() updates all ui elements (e.g. if clicked)
        // draw() draws all ui elements
        stage.act(delta);
        stage.draw();
    }

    /** @see ApplicationListener#resize(int, int) */
    @Override
    public void resize(int width, int height) {
        // scale the viewport if the window is resized to not mess with ratios
        Vector2 size = Scaling.fit.apply(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, width, height);
        this.viewport.update((int) size.x, (int) size.y, true);
        this.stage.setViewport(this.viewport);
    }

    /** @see ApplicationListener#pause() */
    @Override
    public abstract void pause();

    /** @see ApplicationListener#resume() */
    @Override
    public abstract void resume();

    /** Called when this screen is no longer the current screen for a {@link Game}. */
    @Override
    public void hide() {
        this.dispose();
    }

    /** Called when this screen should release all resources. */
    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
