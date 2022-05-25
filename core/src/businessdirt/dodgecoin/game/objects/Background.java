package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.game.Constants;

public class Background extends GameObject {

    private static Background instance;

    private Background() {
        super("textures/backgrounds/default.png", Constants.GAME_X_START, 0, Constants.GAME_WIDTH, Constants.VIEWPORT_HEIGHT);
    }

    public static Background get() {
        if (Background.instance == null) Background.instance = new Background();
        return Background.instance;
    }
}
