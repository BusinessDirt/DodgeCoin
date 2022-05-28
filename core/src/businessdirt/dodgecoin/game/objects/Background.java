package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.Constants;

public class Background extends GameObject {

    private static Background instance;

    private Background() {
        super(Config.backgroundSkin, Constants.GAME_X_START, 0, Constants.GAME_WIDTH, Constants.VIEWPORT_HEIGHT);
    }

    public void setTexture(String texturePath) {
        this.texturePath = texturePath;
    }

    public static Background get() {
        if (Background.instance == null) Background.instance = new Background();
        return Background.instance;
    }
}
