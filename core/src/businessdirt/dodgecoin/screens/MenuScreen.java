package businessdirt.dodgecoin.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.input.ImageButton;
import businessdirt.dodgecoin.input.Keyboard;
import businessdirt.dodgecoin.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class MenuScreen extends ScreenAdapter {

    private Renderer renderer;
    private ImageButton shopButton;
    private ImageButton settingsButton;

    public MenuScreen() {
        renderer = Renderer.newInstance();
        shopButton = new ImageButton("textures/gui/shop.png", 50, 50, 100, 100);
        settingsButton = new ImageButton("textures/gui/settings.png", (int) (renderer.getWidth() - 150), 50, 100, 100);
    }

    @Override
    public void render(float delta) {
        // input
        if (Keyboard.keyTyped(Input.Keys.ENTER)) DodgeCoin.get().setScreen(new GameScreen());

        // buttons
        if (shopButton.isClicked()) System.out.println("shop");
        if (settingsButton.isClicked()) System.out.println("settings");

        // clear the screen from the previous frame
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shopButton.draw();
        settingsButton.draw();

        // IMPORTANT: if this is removed, nothing will be drawn to the screen
        renderer.render(delta);
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
