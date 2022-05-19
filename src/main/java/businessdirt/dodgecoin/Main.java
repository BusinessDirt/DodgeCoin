package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.core.game.GameClock;
import businessdirt.dodgecoin.gui.AssetPool;
import businessdirt.dodgecoin.gui.images.Sprite;
import businessdirt.dodgecoin.gui.Window;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // load Assets
        FileHandler.loadAssets();
        Config.getConfig();

        // Create Window
        Window.get();
        Window.start();

        // Player
        BufferedImage playerImage = AssetPool.getImage("players/default.png");
        Sprite playerSprite = new Sprite(Window.getGameXStart() + Constants.GAME_WIDTH / 2 - playerImage.getWidth() * 3,
                Window.getHeight() - (playerImage.getHeight() * 6) - Constants.Y_OFFSET - 100,
                playerImage.getWidth() * 6, playerImage.getHeight() * 6, playerImage);
        Window.getDraw().setPlayer(playerSprite);

        // Background
        BufferedImage backgroundImage = AssetPool.getImage("backgrounds/default.png");
        Sprite backgroundSprite = new Sprite(Window.getGameXStart(), -Constants.Y_OFFSET, Constants.GAME_WIDTH, Window.getHeight(), backgroundImage);
        Window.getDraw().setBackground(backgroundSprite);

        // GameClock
        GameClock clock = GameClock.get();
        clock.start();
    }
}
