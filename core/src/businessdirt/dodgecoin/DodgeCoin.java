package businessdirt.dodgecoin;

import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.util.AssetLoader;
import businessdirt.dodgecoin.game.screens.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;

import java.awt.*;

public class DodgeCoin extends Game {

	// single instance
	private static DodgeCoin INSTANCE;

	// loads all the assets and disposes them if the game is closed
	public static AssetLoader assets;

	// Main config
	public static Config config;

	// fullscreen and windowed mode information
	public static Graphics.DisplayMode fullscreen;
	public static Dimension windowed;

	@Override
	public void create() {
		setScreen(new LoadingScreen());
	}

	// handles the instantiation
	public static DodgeCoin get() {
		if (DodgeCoin.INSTANCE == null) DodgeCoin.INSTANCE = new DodgeCoin();
		return DodgeCoin.INSTANCE;
	}

	// sets the windowed and fullscreen mode information and returns the instance
	public static DodgeCoin init(Graphics.DisplayMode fullscreen, Dimension windowed) {
		DodgeCoin.fullscreen = fullscreen;
		DodgeCoin.windowed = windowed;
		return DodgeCoin.get();
	}
}
