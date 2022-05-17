package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.game.GameClock;
import businessdirt.dodgecoin.gui.AssetPool;
import businessdirt.dodgecoin.gui.Draw;
import businessdirt.dodgecoin.gui.images.Image;
import businessdirt.dodgecoin.gui.Window;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // load Assets
        FileHandler.loadAssets();
        Config.getConfig();

        // Player
        BufferedImage image = AssetPool.getImage("players/player.png");
        Image playerImage = new Image(0, Window.getHeight() - (image.getHeight() * 6) - Draw.Y_OFFSET - 100,
                image.getWidth() * 6, image.getHeight() * 6, image);
        Window.getDraw().setPlayer(playerImage);

        // Create Window
        Window.start();

        // GameClock
        GameClock clock = GameClock.get();
        clock.start();
    }
}
