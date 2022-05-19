package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.gui.buttons.ImageButton;
import businessdirt.dodgecoin.gui.images.Coin;
import businessdirt.dodgecoin.gui.Window;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameClock extends Thread {

    private static GameClock instance;

    public static int bitcoinValue = 100;

    public static int dogecoinValue = 1;

    private boolean running;
    private int loopCounter;

    private BufferedImage bitcoin, dogecoin;
    private final Random random = new Random();

    //TODO set coin value based on stock market API

    private GameClock() {
        this.running = false;
        this.loopCounter = 0;
        this.loadCoins();
        Util.logEvent("GameClock initialized!");
    }

    public void run() {
        while (true) {
            try {
                sleep(Constants.COIN_DROP_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (running) {
                // Spawn coin
                if (loopCounter >= Constants.COIN_SPAWN_SPEED) {
                    // load Image
                    int tmp = random.nextInt(20);
                    BufferedImage imageBuffer = tmp <= 3 ? bitcoin : dogecoin;

                    int tmp2 = random.nextInt(Constants.GAME_WIDTH - Constants.X_OFFSET - imageBuffer.getWidth() * 4);
                    Window.getDraw().addCoin(new Coin((Window.getWidth() - Constants.GAME_WIDTH) / 2 + tmp2, -imageBuffer.getHeight() * 4, imageBuffer.getWidth() * 4, imageBuffer.getHeight() * 4, imageBuffer, tmp <= 3 ? Coin.CoinType.BITCOIN : Coin.CoinType.DOGECOIN));
                    loopCounter = 0;
                }

                // Move coins
                List<Coin> coins = Window.getDraw().getCoins();
                for (Coin coin : coins) {
                    if (coin.getY() == Window.getHeight() - coin.getHeight() - 100 && coin.isDraw()) {
                        coin.setDraw(false);
                    } else if (coin.isDraw()) {
                        coin.setY(coin.getY() + 1);
                    }
                }

                // Hit detection
                try { for (Coin coin : coins) {
                    if (coin.intersects(Window.getDraw().getPlayer())) {
                        coin.setDraw(false);
                        coin.setX(69420);

                        // Game-over mechanic
                        if (coin.type == Coin.CoinType.DOGECOIN) {
                            Window.setGameState(GameState.GAME_OVER);
                            this.running = false;
                            this.loopCounter = 0;
                            Window.getDraw().getCoins().clear();

                            for (ImageButton b : Window.buttons) {
                                if (Objects.equals(b.getName(), "cancel")) b.setEnabled(true);
                            }
                        }

                        Config.money += coin.type == Coin.CoinType.DOGECOIN ? dogecoinValue : bitcoinValue;
                        Config.getConfig().markDirty();
                        Config.getConfig().writeData();
                    }
                }} catch (ConcurrentModificationException ignore) {}

                // increase loopCounter
                this.loopCounter += Constants.COIN_DROP_SPEED;
            }
        }
    }

    private void loadCoins() {
        try {
            bitcoin = FileHandler.get().getImageFromResource("coins/bitcoin.png");
            dogecoin = FileHandler.get().getImageFromResource("coins/dogecoin.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameClock get() {
        if (GameClock.instance == null) GameClock.instance = new GameClock();
        return GameClock.instance;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
