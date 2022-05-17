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
            description = "Money"
    )
    public static int money = 0;

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
