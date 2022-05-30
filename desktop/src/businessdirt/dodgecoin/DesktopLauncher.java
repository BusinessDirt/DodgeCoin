package businessdirt.dodgecoin;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("DodgeCoin");
		config.useVsync(true);
		config.setIdleFPS(5);
		config.setWindowIcon("textures/coins/dogecoin.png");

		Graphics.DisplayMode mode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		config.setFullscreenMode(mode);

		new Lwjgl3Application(DodgeCoin.get(), config);
	}
}
