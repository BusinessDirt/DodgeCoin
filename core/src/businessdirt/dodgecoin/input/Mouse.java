package businessdirt.dodgecoin.input;

import businessdirt.dodgecoin.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.awt.*;

public class Mouse {

    public static boolean clicked(Rectangle rectangle) {
        Renderer renderer = Renderer.get();
        float xMultiplier = renderer.getWidth() / Gdx.graphics.getWidth();
        float yMultiplier = renderer.getHeight() / Gdx.graphics.getHeight();

        int mouseX = Math.round(Gdx.input.getX() * xMultiplier);
        int mouseY = Math.round(Gdx.input.getY() * yMultiplier) + 1;

        return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && rectangle.contains(mouseX, renderer.getHeight() - mouseY);
    }
}
