package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.game.Constants;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Random;

public class Coin extends GameObject {

    private static Array<Coin> coins = new Array<>();
    private static long lastSpawn = 0;

    public Coin() {
        super("textures/coins/dogecoin.png", Constants.GAME_X_START + new Random().nextInt(Constants.GAME_WIDTH - 16 * Constants.ICON_SIZE_MULTIPLIER),
                Constants.VIEWPORT_HEIGHT, 16 * Constants.ICON_SIZE_MULTIPLIER, 16 * Constants.ICON_SIZE_MULTIPLIER);
    }

    public static void spawn() {
        if (TimeUtils.nanoTime() - lastSpawn > Constants.COIN_SPAWN_DELAY) {
            coins.add(new Coin());
            lastSpawn = TimeUtils.nanoTime();
        }
    }

    public static void move(float delta) {
        for (Iterator<Coin> iter = coins.iterator(); iter.hasNext(); ) {
            Coin coin = iter.next();
            coin.y -= Constants.COIN_DROP_SPEED * delta;
            if (coin.y < Constants.PLAYER_Y_START) iter.remove();
            if (coin.intersects(Player.get())) {
                iter.remove();
                // TODO score
            }
        }
    }

    public static void drawAll() {
        for (Coin coin : coins) coin.draw();
    }
}
