package businessdirt.dodgecoin.game;

public class Constants {

    // Window Constants
    public static final int VIEWPORT_WIDTH = 1920;
    public static final int VIEWPORT_HEIGHT = 1080;

    // Rendering Constants
    public static final int ICON_SIZE_MULTIPLIER = VIEWPORT_WIDTH / 480;
    public static final int PLAYER_SIZE_MULTIPLIER = VIEWPORT_WIDTH / 320;
    public static final int GAME_WIDTH = VIEWPORT_WIDTH / 3;
    public static final int GAME_X_START = VIEWPORT_WIDTH / 2 - GAME_WIDTH / 2;
    public static final int PLAYER_Y_START = 100;
    public static final int CENTER_X = VIEWPORT_WIDTH / 2;
    public static final int CENTER_Y = VIEWPORT_HEIGHT / 2;

    // Game Constants
    public static final int MOVEMENT_SPEED = GAME_WIDTH;
    public static int COIN_DROP_SPEED = 350;
    public static long COIN_SPAWN_DELAY = 2000000000L;
    public static final int BITCOIN_CHANCE = 15;
}
