package businessdirt.dodgecoin.core;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.config.ConfigHandler;
import businessdirt.dodgecoin.core.config.data.Property;
import businessdirt.dodgecoin.core.config.data.PropertyType;
import businessdirt.dodgecoin.core.config.data.types.Key;
import businessdirt.dodgecoin.core.util.Util;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import java.io.File;

public class Config extends ConfigHandler {

    // instance
    private static Config instance;

    // ------- CONFIG PROPERTIES ------- //

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
            type = PropertyType.KEY,
            name = "Move left",
            category = "Controls",
            subcategory = "Movement",
            description = "Keys for moving the player left"
    )
    public static Key moveLeft = new Key(Input.Keys.A, Input.Keys.LEFT);

    @Property(
            type = PropertyType.KEY,
            name = "Move right",
            category = "Controls",
            subcategory = "Movement",
            description = "Keys for moving the player right"
    )
    public static Key moveRight = new Key(Input.Keys.D, Input.Keys.RIGHT);

    public Config() {
        // initialize the config handler
        super(new File(Util.getConfigFolder(), "\\config.toml"));

        // read the data and create data write hooks
        initialize();

        // log the successful loading of the config file
        Util.logEvent("Configuration loaded! " + Util.getConfigFolder());
    }

    // instantiation
    public static Config getConfig() {
        if (Config.instance == null) Config.instance = new Config();
        return Config.instance;
    }
}
