package businessdirt.dodgecoin.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GameClock extends Thread {

    private static GameClock instance;

    public final int COIN_SPAWN_SPEED;
    public final int COIN_DROP_SPEED;

    private boolean running;
    private int loopCounter;

    private BufferedImage bitcoin;
    private BufferedImage dogecoin;
    private BufferedImage imageBuffer;
    private Random random = new Random();

    public GameClock() {
        this.COIN_DROP_SPEED = 5;
        this.COIN_SPAWN_SPEED = 2000;
        this.running = true;
        this.loopCounter = 0;
        this.loadCoins();
    }

    public void run() {
        while (true) {
            if (running) {
                try {
                    sleep(GameClock.get().COIN_DROP_SPEED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Spawn coin
                if (loopCounter / COIN_DROP_SPEED == COIN_SPAWN_SPEED / COIN_DROP_SPEED && loopCounter % COIN_DROP_SPEED == 0) {
                    // load Image
                    int tmp = random.nextInt(20);
                    imageBuffer = tmp <= 3 ? bitcoin : dogecoin;

                    tmp = random.nextInt(Window.get().getWidth() - imageBuffer.getWidth() * 4);
                    Window.getDraw().addCoin(new Image(tmp, 0, imageBuffer.getWidth() * 4, imageBuffer.getHeight() * 4, imageBuffer));
                    loopCounter = 0;
                }

                // Move coins
                List<Image> coins = Window.getDraw().getCoins();
                for (Image coin : coins) {
                    if (coin.getY() == Window.get().getHeight() - coin.getHeight() - 100 && coin.isDraw()) {
                        coin.setDraw(false);
                    } else if (coin.isDraw()) {
                        coin.setY(coin.getY() + 1);
                    }
                }

                // Hit detection
                // TODO Dogecoin und Botcoin detection / Gameover
                for (Image coin : coins) {
                    if (coin.intersects(Window.getDraw().getPlayer())) {
                        coin.setDraw(false);
                    }
                }

                // increase loopCounter
                this.loopCounter += COIN_DROP_SPEED;
            }
        }
    }

    private void loadCoins() {
        try {
            bitcoin = FileHandler.get().getImageFromResource("Bitcoin.png");
            dogecoin = FileHandler.get().getImageFromResource("DogecoinIcon.png");
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
