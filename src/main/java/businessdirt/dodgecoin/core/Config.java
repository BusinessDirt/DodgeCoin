package businessdirt.dodgecoin.core;

import businessdirt.dodgecoin.Main;
import com.github.businessdirt.config.ConfigHandler;
import com.github.businessdirt.config.data.Property;
import com.github.businessdirt.config.data.PropertyType;

import java.io.File;

public class Config extends ConfigHandler {

    private static Config instance;

    @Property(
            type = PropertyType.SWITCH,
            name = "Toggle Animations",
            category = "Settings",
            description = "Toggle animations on/off",
            hidden = true
    )
    public static boolean animations = true;

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
