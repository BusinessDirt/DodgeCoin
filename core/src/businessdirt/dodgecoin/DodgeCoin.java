package businessdirt.dodgecoin;

import businessdirt.dodgecoin.game.AssetFinder;
import businessdirt.dodgecoin.screens.LoadingScreen;
import com.badlogic.gdx.Game;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DodgeCoin extends Game {

	private static DodgeCoin INSTANCE;
	public static AssetFinder assets;

	private DodgeCoin() {}

	@Override
	public void create() {
		setScreen(new LoadingScreen());
	}

	public static DodgeCoin get() {
		if (DodgeCoin.INSTANCE == null) DodgeCoin.INSTANCE = new DodgeCoin();
		return DodgeCoin.INSTANCE;
	}

	public static void logEvent(String event) {
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss.SSS");
		Date date = new Date(System.currentTimeMillis());
		System.out.println("> [" + formatter.format(date) + "] " + event);
	}
}
