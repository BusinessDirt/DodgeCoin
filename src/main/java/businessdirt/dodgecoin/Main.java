package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.GameClock;
import businessdirt.dodgecoin.core.Image;
import businessdirt.dodgecoin.core.Window;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        // player
        BufferedImage image = FileHandler.get().getImageFromResource("players/player.png");
        Window.getDraw().setPlayer(new Image(0, Window.get().getHeight() - 32 * 8 - 38 - 100, 16 * 8, 32 * 8, image));

        Window.start();

        // gameclock
        GameClock clock = GameClock.get();
        clock.start();
    }
}
