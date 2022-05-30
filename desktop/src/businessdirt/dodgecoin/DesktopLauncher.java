package businessdirt.dodgecoin;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.awt.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("DodgeCoin");
		config.useVsync(true);
		config.setIdleFPS(5);
		config.setWindowIcon("textures/coins/dogecoin.png");

		float multiplier = 2f / 3f;
		DisplayMode fullscreen = Lwjgl3ApplicationConfiguration.getDisplayMode();
		Dimension windowed = new Dimension((int) (fullscreen.width * multiplier), (int) (fullscreen.height * multiplier));
		config.setFullscreenMode(fullscreen);

		new Lwjgl3Application(DodgeCoin.init(fullscreen, windowed), config);
	}
}
