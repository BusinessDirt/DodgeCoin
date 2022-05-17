package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.gui.AssetPool;
import businessdirt.dodgecoin.gui.Draw;
import businessdirt.dodgecoin.gui.images.Coin;
import businessdirt.dodgecoin.gui.images.Image;
import businessdirt.dodgecoin.gui.Window;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GameClock extends Thread {

    private static GameClock instance;

    public static int BitcoinValue = 10;

    public static int DogecoinValue = 1;

    private boolean running;
    private int loopCounter;

    private BufferedImage bitcoin;
    private BufferedImage dogecoin;
    private BufferedImage imageBuffer;
    private Random random = new Random();

    //TODO set coin value based on stock market API

    private static int score = 0;

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
                    imageBuffer = tmp <= 3 ? bitcoin : dogecoin;

                    int tmp2 = random.nextInt(Window.getWidth()- Constants.X_OFFSET - imageBuffer.getWidth() * 4);
                    Window.getDraw().addCoin(new Coin(tmp2, 0, imageBuffer.getWidth() * 4, imageBuffer.getHeight() * 4, imageBuffer, tmp <= 3 ? Coin.CoinType.BITCOIN : Coin.CoinType.DOGECOIN));
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
                // TODO Gameover
                for (Coin coin : coins) {
                    if (coin.intersects(Window.getDraw().getPlayer())) {
                        coin.setDraw(false);
                        coin.setX(69420);
                        score += coin.type == Coin.CoinType.DOGECOIN ? DogecoinValue : BitcoinValue;
                    }
                }

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

    public static int getScore(){return score;}

    public static void setScore(int i){score = i;}
}
