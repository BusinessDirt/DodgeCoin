package businessdirt.dodgecoin.core.config;

import businessdirt.dodgecoin.core.Util;
import com.github.businessdirt.config.ConfigHandler;
import com.github.businessdirt.config.data.Property;
import com.github.businessdirt.config.data.PropertyType;

import java.io.File;

public class Config extends ConfigHandler {

    private static Config instance;

    @Property(
            type = PropertyType.SWITCH,
            name = "Switch",
            category = "Settings",
            description = "description"
    )
    public static boolean switchs = true;

    @Property(
            type = PropertyType.NUMBER,
            name = "Number",
            category = "Settings",
            description = "description"
    )
    public static int number = 0;

    @Property(
            type = PropertyType.SLIDER,
            name = "Slider",
            category = "Settings",
            description = "description"
    )
    public static double slider = 0;

    @Property(
            type = PropertyType.TEXT,
            name = "Text",
            category = "Settings",
            description = "description"
    )
    public static String text = "test value";

    public Config() {
        super(new File(Util.getConfigFolder(), "\\config.toml"));
        initialize();
        Util.logEvent("Configuration loaded!");
    }

    public static Config getConfig() {
        if (Config.instance == null) Config.instance = new Config();
        return Config.instance;
    }
}
