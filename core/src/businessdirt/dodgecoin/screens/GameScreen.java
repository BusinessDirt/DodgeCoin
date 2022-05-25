package businessdirt.dodgecoin.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.game.objects.Background;
import businessdirt.dodgecoin.game.objects.Coin;
import businessdirt.dodgecoin.game.objects.Player;
import businessdirt.dodgecoin.input.Keyboard;
import businessdirt.dodgecoin.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter {

    private final Renderer renderer;
    private Player player;
    private Background background;

    public GameScreen() {
        renderer = Renderer.newInstance();
        player = Player.get();
        background = Background.get();
    }

    @Override
    public void render(float delta) {
        // input detection
        player.move(delta);
        Coin.move(delta);

        // TODO pause
        if (Keyboard.keyTyped(Input.Keys.ESCAPE)) DodgeCoin.get().setScreen(new MenuScreen());

        // spawn new coin
        Coin.spawn();

        // clear the screen from the previous frame
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // renderer calls (before the actual renderer call)
        // if image is parsed via string it is disposed automatically when the renderer is disposed
        background.draw();
        player.draw();
        Coin.drawAll();

        // IMPORTANT: if this is removed, nothing will be drawn to the screen
        renderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        renderer.dispose();
    }
}
