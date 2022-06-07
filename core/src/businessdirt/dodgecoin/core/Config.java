package businessdirt.dodgecoin.core;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.config.ConfigHandler;
import businessdirt.dodgecoin.core.config.data.Property;
import businessdirt.dodgecoin.core.config.data.PropertyType;
import businessdirt.dodgecoin.core.util.Util;
import com.badlogic.gdx.graphics.Color;

import java.io.File;

public class Config extends ConfigHandler {

    private static Config instance;

    @Property(
            type = PropertyType.NUMBER,
            name = "Money",
            category = "Hidden",
            description = "Money",
            hidden = true
    )
    public static double money = 0;

    @Property(
            type = PropertyType.COLOR,
            name = "Test",
            category = "Audio",
            description = "ree"
    )
    public static Color color = new Color();

    @Property(
            type = PropertyType.TEXT,
            name = "Player Skin",
            category = "Hidden",
            description = "Player Skin for the game",
            hidden = true
    )
    public static String playerSkin = "textures/players/default.png";

    @Property(
            type = PropertyType.TEXT,
            name = "Background Skin",
            category = "Hidden",
            description = "Background Skin for the game",
            hidden = true
    )
    public static String backgroundSkin = "textures/backgrounds/default.png";

    @Property(
            type = PropertyType.SLIDER,
            name = "Music Volume",
            description = "Controls the music volume.",
            category = "Audio",
            max = 100
    )
    public static Integer musicVolume = 10;

    @Property(
            type = PropertyType.SLIDER,
            name = "SFX Volume",
            description = "Controls the SFX volume.",
            category = "Audio",
            max = 100
    )
    public static Integer sfxVolume = 10;

    @Property(
            type = PropertyType.SWITCH,
            name = "Hard Mode",
            category = "Game Options",
            description = "Adds a hard mode. Movement is faster and you can't stop."
    )
    public static boolean hardMode = false;

    public Config() {
        super(new File(Util.getConfigFolder(), "\\config.toml"));
        initialize();
        Util.logEvent("Configuration loaded! " + Util.getConfigFolder());
    }

    public static Config getConfig() {
        if (Config.instance == null) Config.instance = new Config();
        return Config.instance;
    }
}
