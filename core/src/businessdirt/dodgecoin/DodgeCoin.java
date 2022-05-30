package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.AssetFinder;
import businessdirt.dodgecoin.game.screens.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DodgeCoin extends Game {

	private static DodgeCoin INSTANCE;
	public static AssetFinder assets;
	public static Config config;
	public static Graphics.DisplayMode fullscreen;
	public static Dimension windowed;

	@Override
	public void create() {
		setScreen(new LoadingScreen());
	}

	// logs an event with time
	public static void logEvent(String event) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
		Date date = new Date(System.currentTimeMillis());

		String prefix = "> [" + formatter.format(date) + "] ";
		event = event.replaceAll("(\\n+)(\\s*)", "\n" + new String(new char[21]).replace("\0", " "));
		System.out.println(prefix + event);
	}

	public static File getConfigFolder() {
		File configFolder = new File(System.getenv("Appdata"), "businessdirt\\DodgeCoin");
		if (!configFolder.exists()) {
			if (configFolder.mkdirs()) {
				logEvent("Created config folder: " + configFolder.getAbsolutePath());
			} else {
				logEvent("Couldn't create config folder!");
			}
		}
		return configFolder;
	}

	public static DodgeCoin get() {
		if (DodgeCoin.INSTANCE == null) DodgeCoin.INSTANCE = new DodgeCoin();
		return DodgeCoin.INSTANCE;
	}

	public static DodgeCoin init(Graphics.DisplayMode fullscreen, Dimension windowed) {
		DodgeCoin.fullscreen = fullscreen;
		DodgeCoin.windowed = windowed;
		if (DodgeCoin.INSTANCE == null) DodgeCoin.INSTANCE = new DodgeCoin();
		return DodgeCoin.INSTANCE;
	}
}
