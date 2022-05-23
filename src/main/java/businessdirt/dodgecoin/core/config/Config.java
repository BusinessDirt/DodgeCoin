package businessdirt.dodgecoin.core.config;

import businessdirt.dodgecoin.core.Util;
import com.github.businessdirt.config.ConfigHandler;
import com.github.businessdirt.config.data.Property;
import com.github.businessdirt.config.data.PropertyType;

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
            type = PropertyType.SWITCH,
            name = "Hard Mode",
            category = "Hidden",
            description = "Adds a hard mode. Movement is faster and you can't stop.",
            hidden = true
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
