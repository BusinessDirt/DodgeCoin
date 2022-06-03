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

public abstract class AbstractScreen implements Screen {
    protected final Stage stage;
    protected final ScalingViewport viewport;
    protected final Skin skin;
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

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        Vector2 size = Scaling.fit.apply(1920, 1080, width, height);
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
