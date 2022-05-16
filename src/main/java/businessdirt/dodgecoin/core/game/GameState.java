package businessdirt.dodgecoin.core.game;

public enum GameState {

    MAIN_MENU, GAME, PAUSE, GAME_OVER, SHOP, SETTINGS;

    public int getID() {
        return this.ordinal();
    }

    public static GameState getState(int id) {
        return values()[id];
    }
}
