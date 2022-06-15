package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.game.objects.Background;
import businessdirt.dodgecoin.game.objects.Coin;
import businessdirt.dodgecoin.game.objects.Player;
import businessdirt.dodgecoin.core.input.Keyboard;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen extends ScreenAdapter {

    private final Renderer renderer;
    private final Player player;
    private final Background background;
    private static GameState state;
    private long startTime;

    public GameScreen() {
        renderer = Renderer.newInstance();
        player = Player.get();
        background = Background.get();
        state = GameState.GAME;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // input detection
        Keyboard.defaultKeys();

        if (state == GameState.GAME) {
            if (Keyboard.keyTyped(Input.Keys.ESCAPE)) state = GameState.PAUSE;

            player.move(delta);
            Coin.move(delta);

            // spawn new coin
            Coin.spawn();
        } else {
            if (state == GameState.OVER) {
                startTime = TimeUtils.nanoTime();
            }
            if (Keyboard.keyTyped(Input.Keys.ESCAPE)) DodgeCoin.get().setScreen(new MenuScreen());
            if (Keyboard.keyTyped(Input.Keys.ENTER)) state = GameState.GAME;
        }

        // clear the screen from the previous frame
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // renderer calls (before the actual renderer call)
        // if image is parsed via string it is disposed automatically when the renderer is disposed
        if (state == GameState.GAME) {
            // draw game
            background.draw();
            player.draw();
            Coin.drawAll();

            // draw hud
            Renderer.get().drawString("Money:", 50, Constants.VIEWPORT_HEIGHT - 50, Color.WHITE);
            Renderer.get().drawString(String.valueOf(Math.round(Config.money)), 50, Constants.VIEWPORT_HEIGHT - 100, Color.WHITE);

            Renderer.get().drawString("Combo:", 50, Constants.VIEWPORT_HEIGHT - 160, Color.WHITE);
            Renderer.get().drawString(String.valueOf(Coin.getCombo()), 50, Constants.VIEWPORT_HEIGHT - 210, Color.WHITE);

            Renderer.get().drawString("Timer:", 50, Constants.VIEWPORT_HEIGHT - 270, Color.WHITE);
            Renderer.get().drawString((startTime - TimeUtils.nanoTime()) / -1000000000 + "s", 50, Constants.VIEWPORT_HEIGHT - 320, Color.WHITE);
        }

        // pause overlay
        if (state == GameState.PAUSE) {
            Gdx.gl.glClearColor(39f / 255, 174f / 255, 96f / 255, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Renderer.get().drawCenteredString("Paused!", Constants.CENTER_X - 250, Constants.CENTER_Y - 150, 500, 300, Color.WHITE);
        }

        // game-over overlay
        if (state == GameState.OVER) {
            Gdx.gl.glClearColor(250f / 255, 104f / 255, 0f, 0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Renderer.get().drawCenteredString("Game Over!", Constants.CENTER_X - 250, Constants.CENTER_Y - 150, 500, 300, Color.WHITE);
            startTime = 0L;
        }

        // IMPORTANT: if this is removed, nothing will be drawn to the screen
        renderer.render();
    }

    @Override
    public void hide() {
        this.dispose();
    }

    public enum GameState {
        GAME, PAUSE, OVER
    }

    public static void setState(GameState state) {
        GameScreen.state = state;
    }
}
