package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.gui.Image;
import businessdirt.dodgecoin.gui.Window;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GameClock extends Thread {

    private static GameClock instance;

    public final int COIN_SPAWN_SPEED;
    public final int COIN_DROP_SPEED;
    public final int MOVEMENT_SPEED = 10;

    private boolean running;
    private int loopCounter;

    private BufferedImage bitcoin;
    private BufferedImage dogecoin;
    private BufferedImage imageBuffer;
    private Random random = new Random();

    //TODO set coin value based on stock market API
    private static int BitcoinValue = 10;

    private static int DogecoinValue = 1;

    private static int score = 0;

    private GameClock() {
        this.COIN_DROP_SPEED = 7;
        this.COIN_SPAWN_SPEED = 2000;
        this.running = false;
        this.loopCounter = 0;
        this.loadCoins();
        Util.logEvent("GameClock initialized!");
    }

    public void run() {

        while (true) {
            try {
                sleep(GameClock.get().COIN_DROP_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (running) {
                // Spawn coin
                if (loopCounter / COIN_DROP_SPEED == COIN_SPAWN_SPEED / COIN_DROP_SPEED && loopCounter % COIN_DROP_SPEED == 0) {
                    // load Image
                    int tmp = random.nextInt(20);
                    imageBuffer = tmp <= 3 ? bitcoin : dogecoin;

                    tmp = random.nextInt(Window.getWidth() - imageBuffer.getWidth() * 4);
                    Window.getDraw().addCoin(new Image(tmp, 0, imageBuffer.getWidth() * 4, imageBuffer.getHeight() * 4, imageBuffer));
                    loopCounter = 0;
                }

                // Move coins
                List<Image> coins = Window.getDraw().getCoins();
                for (Image coin : coins) {
                    if (coin.getY() == Window.getHeight() - coin.getHeight() - 100 && coin.isDraw()) {
                        coin.setDraw(false);
                    } else if (coin.isDraw()) {
                        coin.setY(coin.getY() + 1);
                    }
                }

                // Hit detection
                // TODO Dogecoin und Bitcoin detection / Gameover; fix collision detection
                for (Image coin : coins) {
                    if (coin.intersects(Window.getDraw().getPlayer())) {
                        coin.setDraw(false);
                        score = score + DogecoinValue; //Collisions seem to be not properly resolved sometimes
                    }
                }

                // increase loopCounter
                this.loopCounter += COIN_DROP_SPEED;
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
