package businessdirt.dodgecoin.core.util;


import com.badlogic.gdx.Input;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

public class Util {

    public static void logFileLoaded(String path) {
        Util.logEvent("- Loaded File - " + path);
    }

    /**
     * Logs an event with time stamps
     * @param event the event to be logged ({@link String})
     */
    public static void logEvent(String event) {
        // format the time
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis());

        // log the event
        String prefix = "> [" + formatter.format(date) + "] ";
        System.out.println(prefix + event);
    }

    /**
     * @return the config folder. In the directory the program was executed from.
     */
    public static File getConfigFolder() {
        // config folder
        File configFolder = new File(System.getProperty("user.dir"), "config");

        // create the folder if it doesn't exist
        if (!configFolder.exists()) {
            if (configFolder.mkdirs()) {
                Util.logEvent("Created config folder: " + configFolder.getAbsolutePath());
            } else {
                Util.logEvent("Couldn't create config folder!");
            }
        }

        // return the folder
        return configFolder;
    }

    /**
     * Capitalizes the first letter of every word from the {@code inputString}.
     * @param inputString the {@link String} to be capitalized
     * @return the capitalized {@link String}
     */
    public static String title(String inputString) {
        // no need to waste resources on an empty or one character String
        if (Objects.equals(inputString, "")) return "";
        if (inputString.length() == 1) return inputString.toUpperCase();

        // to the capitalization
        StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());
        Stream.of(inputString.split(" ")).forEach(stringPart -> {
            char[] charArray = stringPart.toLowerCase().toCharArray();
            charArray[0] = Character.toUpperCase(charArray[0]);
            resultPlaceHolder.append(new String(charArray)).append(" ");
        });

        // return the capitalized string
        return resultPlaceHolder.toString().trim();
    }

    /**
     * Converts a keyCode into a readable format.
     * This is still kind of a bad solution as it doesn't account for language.
     * @param code the code to be converted to a readable format
     * @return the formatted {@code code}
     */
    public static String getKeyCharFromCode(int code) {
        return Input.Keys.toString(code);
    }
}
