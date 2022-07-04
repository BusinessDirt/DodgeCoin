//erstellt von Marcus Howell
package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.util.APIHandler;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.game.screens.GameScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.Random;

public class Coin extends GameObject {

    // list for all the coins on the screen
    private static final Array<Coin> coins = new Array<>();

    // last time a coin was spawned
    private static long lastSpawn = 0;

    // if coins should spawn
    // no coins spawn if the game is paused / game over
    private static boolean noSpawn = true;

    // combo for game difficulty scale
    private static int combo = 1;

    private Coin(String type) {
        super(type, Constants.GAME_X_START + new Random().nextInt(Constants.GAME_WIDTH - 16 * Constants.ICON_SIZE_MULTIPLIER),
                Constants.VIEWPORT_HEIGHT, 16 * Constants.ICON_SIZE_MULTIPLIER, 16 * Constants.ICON_SIZE_MULTIPLIER);
    }

    /**
     * Spawns a new coin at the top of the screen.
     * The x coordinate is randomly generated.
     * The chance of the coin being a bitcoin ist defined in {@link Constants}
     */
    public static void spawn() {
        // spawn the coin if the spawn delay time is passed and noSpawn is false
        if (TimeUtils.nanoTime() - lastSpawn > Constants.COIN_SPAWN_DELAY) {
            if (!noSpawn) {
                // random spawn (bitcoin or dogecoin)
                int rand = MathUtils.random(1, 100);
                coins.add(new Coin(rand <= Constants.BITCOIN_CHANCE ? "textures/coins/bitcoin.png" : "textures/coins/dogecoin.png"));
            }
            lastSpawn = TimeUtils.nanoTime();
            noSpawn = false;
        }
    }

    /**
     * Moves all coins down and handles hit detection.
     * @param delta time between frames
     */
    public static void move(float delta) {
        for (Iterator<Coin> iter = coins.iterator(); iter.hasNext(); ) {
            Coin coin = iter.next();

            // move the coin down
            coin.y -= Constants.COIN_DROP_SPEED * delta;

            // remove the coin below a certain y coordinate
            if (coin.y < Constants.PLAYER_Y_START) iter.remove();

            // hit reg
            if (coin.intersects(Player.get())) {
                // remove the coin
                iter.remove();

                // game over if it is a dogecoin
                if (coin.texturePath.contains("dogecoin.png")) {
                    // set GameState to OVER play death sound and add the value to the player money
                    Config.money += APIHandler.dogecoin;
                    GameScreen.setState(GameScreen.GameState.OVER);
                    DodgeCoin.assets.get("sounds/death.mp3", Sound.class).play(Config.sfxVolume / 100f);

                    // reset vars
                    Constants.COIN_DROP_SPEED = Constants.F_COIN_DROP_SPEED;
                    Constants.COIN_SPAWN_DELAY = Constants.F_COIN_SPAWN_DELAY;
                    noSpawn = true;
                    combo = 1;

                    // clear the coin list
                    iter.forEachRemaining(c -> iter.remove());
                } else { // i.e. the coin is a bitcoin
                    // increase difficulty, add the money to the player wallet
                    combo ++;
                    Config.money += APIHandler.bitcoin * (1 + combo / 10f);
                    Constants.COIN_SPAWN_DELAY -= combo * 1000L;

                    // player coin collect sound and further increase difficulty
                    DodgeCoin.assets.get("sounds/bitcoin.mp3", Sound.class).play(Config.sfxVolume / 100f);
                    if (combo % 3 == 0 && combo < 50) {
                        Constants.COIN_DROP_SPEED = Constants.COIN_DROP_SPEED + combo * 3;
                        Constants.COIN_SPAWN_DELAY = Constants.COIN_SPAWN_DELAY - 100;
                    }
                }

                // update the config values and save them
                DodgeCoin.config.markDirty();
                DodgeCoin.config.writeData();
            }
        }
    }

    /**
     * Draws all coins to the screen at their respective position.
     */
    public static void drawAll() {
        for (Coin coin : coins) coin.draw();
    }

    public static int getCombo() {
        return combo;
    }
}
