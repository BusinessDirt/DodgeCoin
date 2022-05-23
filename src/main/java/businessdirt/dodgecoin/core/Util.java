package businessdirt.dodgecoin.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static void logEvent(String event) {
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("[" + formatter.format(date) + "] " + event);
    }

    public static File getConfigFolder() {
        File configFolder = new File(System.getenv("Appdata"), "businessdirt\\DodgeCoin");
        if (!configFolder.exists()) {
            if (configFolder.mkdirs()) {
                Util.logEvent("Created config folder: " + configFolder.getAbsolutePath());
            } else {
                Util.logEvent("Couldn't create config folder!");
            }
        }
        return configFolder;
    }
}
