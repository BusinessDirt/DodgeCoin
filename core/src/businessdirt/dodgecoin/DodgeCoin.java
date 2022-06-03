package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.util.AssetLoader;
import businessdirt.dodgecoin.game.screens.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;

import java.awt.*;

public class DodgeCoin extends Game {

	private static DodgeCoin INSTANCE;
	public static AssetLoader assets;
	public static Config config;
	public static Graphics.DisplayMode fullscreen;
	public static Dimension windowed;

	@Override
	public void create() {
		setScreen(new LoadingScreen());
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
