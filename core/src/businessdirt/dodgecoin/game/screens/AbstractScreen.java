package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.core.input.Keyboard;
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

    // stage contains all ui elements
    protected final Stage stage;

    // viewport to handle resizing
    protected final ScalingViewport viewport;

    // skin for button style etc
    protected final Skin skin;

    // background color of the window
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

    @Override
    public abstract void show();

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

    @Override
    public void resize(int width, int height) {
        // scale the viewport if the window is resized to not mess with ratios
        Vector2 size = Scaling.fit.apply(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, width, height);
        this.viewport.update((int) size.x, (int) size.y, true);
        this.stage.setViewport(this.viewport);
    }

    @Override
    public abstract void pause();

    @Override
    public abstract void resume();

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
