package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.APIHandler;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.game.screens.GameScreen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Random;

public class Coin extends GameObject {

    private static final Array<Coin> coins = new Array<>();
    private static long lastSpawn = 0;
    private static boolean noSpawn = true;

    private static int combo = 1;

    private Coin(String type) {
        super(type, Constants.GAME_X_START + new Random().nextInt(Constants.GAME_WIDTH - 16 * Constants.ICON_SIZE_MULTIPLIER),
                Constants.VIEWPORT_HEIGHT, 16 * Constants.ICON_SIZE_MULTIPLIER, 16 * Constants.ICON_SIZE_MULTIPLIER);
    }

    public static void spawn() {
        if (TimeUtils.nanoTime() - lastSpawn > Constants.COIN_SPAWN_DELAY) {
            if (!noSpawn) {
                int rand = MathUtils.random(1, 100);
                coins.add(new Coin(rand <= Constants.BITCOIN_CHANCE ? "textures/coins/bitcoin.png" : "textures/coins/dogecoin.png"));
            }
            lastSpawn = TimeUtils.nanoTime();
            noSpawn = false;
        }
    }

    public static void move(float delta) {
        for (Iterator<Coin> iter = coins.iterator(); iter.hasNext(); ) {
            Coin coin = iter.next();
            coin.y -= Constants.COIN_DROP_SPEED * delta;
            if (coin.y < Constants.PLAYER_Y_START) iter.remove();
            if (coin.intersects(Player.get())) {
                iter.remove();

                if (coin.texturePath.contains("dogecoin.png")) {
                    Config.money += APIHandler.dogecoin;
                    GameScreen.setState(GameScreen.GameState.OVER);
                    iter.forEachRemaining(c -> iter.remove());
                    noSpawn = true;
                    combo = 1;
                } else {
                    combo ++;
                    Config.money += APIHandler.bitcoin * (1 + combo / 10);
                    Constants.COIN_SPAWN_DELAY -= combo * 1000;
                    if (combo % 3 == 0 && combo<50) {
                        Constants.COIN_DROP_SPEED = Constants.COIN_DROP_SPEED + combo * 3;
                    }
                }

                DodgeCoin.config.markDirty();
                DodgeCoin.config.writeData();
            }
        }
    }

    public static void drawAll() {
        for (Coin coin : coins) coin.draw();
    }
    public static int getCombo()
    {
        return combo;
    }
}
