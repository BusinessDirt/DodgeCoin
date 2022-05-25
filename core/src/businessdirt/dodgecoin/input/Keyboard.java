package businessdirt.dodgecoin.input;

import com.badlogic.gdx.Gdx;

public class Keyboard {

    public static boolean keyDown(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }

    public static boolean keyUp(int keycode) {
        return !Gdx.input.isKeyPressed(keycode);
    }

    public static boolean keyTyped(int keycode) {
        return Gdx.input.isKeyJustPressed(keycode);
    }
}
