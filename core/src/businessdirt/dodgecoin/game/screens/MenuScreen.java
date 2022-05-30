package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.core.input.buttons.ImageButton;
import businessdirt.dodgecoin.core.input.Keyboard;
import businessdirt.dodgecoin.core.input.buttons.TextButton;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class MenuScreen extends ScreenAdapter {

    private final Renderer renderer;
    private final ImageButton shopButton, settingsButton;
    private final TextButton playButton;

    public MenuScreen() {
        renderer = Renderer.newInstance();
        shopButton = new ImageButton("textures/gui/shop.png", 75, 75, 100, 100);
        settingsButton = new ImageButton("textures/gui/settings.png", (int) (Renderer.get().getWidth() - 225), 75, 100, 100);
        playButton = new TextButton("PLAY", Constants.CENTER_X - 300, 50, 600, 150);
        playButton.setBackgroundColor(Color.RED);
    }

    @Override
    public void render(float delta) {
        // input
        if (Keyboard.keyTyped(Input.Keys.ENTER)) DodgeCoin.get().setScreen(new GameScreen());
        Keyboard.defaultKeys();

        // buttons
        if (shopButton.isClicked()) DodgeCoin.get().setScreen(new ShopScreen());
        if (settingsButton.isClicked()) DodgeCoin.get().setScreen(new SettingsScreen());
        if (playButton.isClicked()) DodgeCoin.get().setScreen(new GameScreen());

        // clear the screen from the previous frame
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw the buttons
        shopButton.draw();
        settingsButton.draw();
        playButton.draw();

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
