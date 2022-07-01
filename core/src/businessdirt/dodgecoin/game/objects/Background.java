package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.Constants;

public class Background extends GameObject {

    // instantiation because the shall only one background
    private static Background instance;

    private Background() {
        super(Config.backgroundSkin, Constants.GAME_X_START, 0, Constants.GAME_WIDTH, Constants.VIEWPORT_HEIGHT);
    }

    // setter for the texture
    public void setTexture(String texturePath) {
        this.texturePath = texturePath;
    }

    // returns the instance / creates the instance
    public static Background get() {
        if (Background.instance == null) Background.instance = new Background();
        return Background.instance;
    }
}
