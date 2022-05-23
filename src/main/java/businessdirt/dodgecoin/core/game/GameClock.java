package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.gui.buttons.ImageButton;
import businessdirt.dodgecoin.gui.images.Coin;
import businessdirt.dodgecoin.gui.Window;
import businessdirt.dodgecoin.gui.images.Sprite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameClock extends Thread {

    private static GameClock instance;

    private static int bitcoinValue = 10;
    private static int dogecoinValue = 1;
    private static int combo = 10;
    public static int playerVelocity = 0;

    private boolean running;
    private int loopCounter;

    private int downset = 1;

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
                sleep(1);
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
                if (loopCounter % Constants.COIN_DROP_SPEED == 0) {
                    try {
                        for (Coin coin : coins) {
                            if (coin.getY() == Window.getHeight() - Constants.Y_OFFSET - coin.getHeight() - 100 && coin.isDraw()) {
                                coin.setDraw(false);
                                Window.getDraw().getCoins().remove(coin);
                            } else if (coin.isDraw()) {
                                coin.setY(coin.getY() + downset);
                            }
                        }
                    } catch (ConcurrentModificationException ignored) {}
                }

                // Hit detection
                try { for (Coin coin : coins) {
                    if (coin.intersects(Window.getDraw().getPlayer()) && coin.isDraw()) {
                        coin.setDraw(false);

                        // Game-over mechanic
                        if (coin.type == Coin.CoinType.DOGECOIN) {
                            Window.setGameState(GameState.GAME_OVER);
                            this.running = false;
                            this.loopCounter = 0;
                            playerVelocity = 0;
                            downset = 1;
                            Window.getDraw().getCoins().clear();

                            for (ImageButton b : Window.buttons) {
                                if (Objects.equals(b.getName(), "cancel")) b.setEnabled(true);
                            }

                            //set combo
                        } else if (coin.type == Coin.CoinType.BITCOIN) {
                            //TODO: Dial in Values for combo
                            combo += 1;
                            downset +=1;
                            bitcoinValue *= combo;
                        }


                        Config.money += coin.type == Coin.CoinType.DOGECOIN ? dogecoinValue : bitcoinValue;
                        Config.getConfig().markDirty();
                        Config.getConfig().writeData();

                        Window.getDraw().getCoins().remove(coin);
                    }
                }} catch (ConcurrentModificationException ignore) {}

                // Move player
                Sprite player = Window.getDraw().getPlayer();
                if (playerVelocity < 0 && loopCounter % 2 == 0) {
                    int newX = player.getX() + playerVelocity * (Config.hardMode ? 1 : Constants.MOVEMENT_SPEED);
                    if (!Config.hardMode) {
                        player.setX(Math.max(newX, Window.getGameXStart()));
                    } else {
                        player.setX(newX < Window.getGameXStart() ? (Window.getGameXStart() + Constants.GAME_WIDTH - player.getWidth()) : newX);
                    }
                } else if (playerVelocity > 0 && loopCounter % 2 == 0) {
                    int newX = player.getX() + playerVelocity * (Config.hardMode ? 1 : Constants.MOVEMENT_SPEED);
                    if (!Config.hardMode) {
                        player.setX(Math.min(newX, Window.getGameXStart() + Constants.GAME_WIDTH - player.getWidth()));
                    } else {
                        player.setX(newX > Window.getGameXStart() + Constants.GAME_WIDTH - player.getWidth() ? Window.getGameXStart() : newX);
                    }
                }
                if (!Config.hardMode) playerVelocity = 0;

                // increase loopCounter
                this.loopCounter++;
            }
        }
    }

    private void loadCoins() {
        try {
            bitcoin = FileHandler.get().getImageFromResource("textures/coins/bitcoin.png");
            dogecoin = FileHandler.get().getImageFromResource("textures/coins/dogecoin.png");
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
