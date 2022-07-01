package businessdirt.dodgecoin.core;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class SkinHandler {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static File unlockedSkinFile;
    private static File skinPriceFile;
    public static final HashMap<String, Boolean> unlockedSkins = new HashMap<>();
    public static final HashMap<String, Integer> skinPrices = new HashMap<>();

    public static void load() {
        // clear the map to not duplicate any pairs
        unlockedSkins.clear();

        // read the content of the file
        try (FileReader in = new FileReader(unlockedSkinFile)) {
            // convert the content to json using gson
            JsonObject data = gson.fromJson(in, JsonObject.class);

            // put the entries in the map
            for (Map.Entry<String, JsonElement> skin : data.entrySet()) {
                unlockedSkins.put(skin.getKey(), skin.getValue().getAsBoolean());
            }
        } catch (Exception e) {
            // if the file could not be read set the content of the file to a blank json file
            try (FileWriter writer = new FileWriter(unlockedSkinFile)) {
                gson.toJson(new JsonObject(), writer);
            } catch (Exception ignored) {}
        }

        // clear the map to not duplicate any pairs
        skinPrices.clear();

        // read the content of the file
        try (FileReader in = new FileReader(skinPriceFile)) {
            // convert the content to json using gson
            JsonObject data = gson.fromJson(in, JsonObject.class);

            // put the entries in the map
            for (Map.Entry<String, JsonElement> price : data.entrySet()) {
                skinPrices.put(price.getKey(), price.getValue().getAsInt());
            }
        } catch (Exception e) {
            // if the file could not be read set the content of the file to a blank json file
            try (FileWriter writer = new FileWriter(skinPriceFile)) {
                gson.toJson(new JsonObject(), writer);
            } catch (Exception ignored) {}
        }
    }

    public static void save() {
        // try to open a file writer
        try (FileWriter writer = new FileWriter(unlockedSkinFile)) {
            // JsonObject to store the map into
            JsonObject obj = new JsonObject();

            // add the map entries to the object
            for (Map.Entry<String, Boolean> skins : unlockedSkins.entrySet()) {
                obj.addProperty(skins.getKey(), skins.getValue());
            }

            // convert the object to a json string and write it to the file
            gson.toJson(obj, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void savePrices() {
        // try to open a file writer
        try (FileWriter writer = new FileWriter(skinPriceFile)) {
            // JsonObject to store the map into
            JsonObject obj = new JsonObject();

            // add the map entries to the object
            for (Map.Entry<String, Integer> price : SkinHandler.skinPrices.entrySet()) {
                obj.addProperty(price.getKey(), price.getValue());
            }

            // convert the object to a json string and write it to the file
            gson.toJson(obj, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void init() throws Exception {
        // Create the folder if it doesn't exist
        // If it fails to create it, it will log an error and exit the program
        File directory = new File(Util.getConfigFolder() + "\\skins");
        if (!directory.exists() && !directory.mkdirs()) {
            throw new Exception("Failed to create skin config folder!");
        }

        // set the files for the prices and unlocked skins
        unlockedSkinFile = new File(Util.getConfigFolder() + "\\skins", "\\unlockedSkins.json");
        skinPriceFile = new File(Util.getConfigFolder() + "\\skins", "\\skinPrices.json");

        // load the values from the files
        load();
    }
}
