package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.AssetFinder;
import businessdirt.dodgecoin.game.screens.LoadingScreen;
import com.badlogic.gdx.Game;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DodgeCoin extends Game {

	private static DodgeCoin INSTANCE;
	public static AssetFinder assets;
	public static Config config;

	@Override
	public void create() {
		setScreen(new LoadingScreen());
	}

	public static void logEvent(String event) {
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss.SSS");
		Date date = new Date(System.currentTimeMillis());
		System.out.println("> [" + formatter.format(date) + "] " + event);
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
}
