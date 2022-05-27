package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.APIHandler;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.IOException;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("DodgeCoin");
		config.useVsync(true);
		config.setIdleFPS(5);

		Graphics.DisplayMode mode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		config.setFullscreenMode(mode);

		new Lwjgl3Application(DodgeCoin.get(), config);
	}
}
