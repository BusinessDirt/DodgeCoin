package businessdirt.dodgecoin.core.util;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

public class Util {

    public static void logFileLoaded(String path) {
        Util.logEvent("- Loaded File - " + path);
    }

    public static void logEvent(String event) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis());

        String prefix = "> [" + formatter.format(date) + "] ";
        System.out.println(prefix + event);
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

    public static String title(String inputString) {
        if (Objects.equals(inputString, "")) return "";
        if (inputString.length() == 1) return inputString.toUpperCase();

        StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());
        Stream.of(inputString.split(" ")).forEach(stringPart -> {
            char[] charArray = stringPart.toLowerCase().toCharArray();
            charArray[0] = Character.toUpperCase(charArray[0]);
            resultPlaceHolder.append(new String(charArray)).append(" ");
        });

        return resultPlaceHolder.toString().trim();
    }
}
