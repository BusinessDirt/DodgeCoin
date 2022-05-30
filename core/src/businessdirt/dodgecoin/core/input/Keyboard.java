package businessdirt.dodgecoin.core.input;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

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

    public static void defaultKeys() {
        if (Keyboard.keyDown(Input.Keys.ALT_LEFT) && Keyboard.keyDown(Input.Keys.F4)) {
            Gdx.app.exit();
            System.exit(0);
        }
    }
}
