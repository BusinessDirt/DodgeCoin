package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.input.Keyboard;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class SettingsScreen extends ScreenAdapter {

    private final Renderer renderer;

    public SettingsScreen() {
        renderer = Renderer.newInstance();
    }

    @Override
    public void render(float delta) {
        // input
        if (Keyboard.keyTyped(Input.Keys.ESCAPE)) DodgeCoin.get().setScreen(new MenuScreen());
        if (Keyboard.keyDown(Input.Keys.ALT_LEFT) && Keyboard.keyDown(Input.Keys.F4)) {
            Gdx.app.exit();
            System.exit(0);
        }

        // clear the screen from the previous frame
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // IMPORTANT: if this is removed, nothing will be drawn to the screen
        renderer.render();
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
