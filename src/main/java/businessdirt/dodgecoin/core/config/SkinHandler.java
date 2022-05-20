package businessdirt.dodgecoin.core.config;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.Util;
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
    private static SkinHandler instance;

    private static File unlockedSkinFile;
    private static File skinPriceFile;
    public static final HashMap<String, Boolean> unlockedSkins = new HashMap<>();
    public static final HashMap<String, Integer> skinPrices = new HashMap<>();

    public static void load() {
        unlockedSkins.clear();
        try (FileReader in = new FileReader(unlockedSkinFile)) {
            JsonObject data = gson.fromJson(in, JsonObject.class);
            for (Map.Entry<String, JsonElement> skin : data.entrySet()) {
                unlockedSkins.put(skin.getKey(), skin.getValue().getAsBoolean());
            }
        } catch (Exception e) {
            try (FileWriter writer = new FileWriter(unlockedSkinFile)) {
                gson.toJson(new JsonObject(), writer);
            } catch (Exception ignored) {}
        }

        skinPrices.clear();
        try (FileReader in = new FileReader(skinPriceFile)) {
            JsonObject data = gson.fromJson(in, JsonObject.class);
            for (Map.Entry<String, JsonElement> price : data.entrySet()) {
                skinPrices.put(price.getKey(), price.getValue().getAsInt());
            }
        } catch (Exception e) {
            try (FileWriter writer = new FileWriter(skinPriceFile)) {
                gson.toJson(new JsonObject(), writer);
            } catch (Exception ignored) {}
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(unlockedSkinFile)) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, Boolean> skins : unlockedSkins.entrySet()) {
                obj.addProperty(skins.getKey(), skins.getValue());
            }
            gson.toJson(obj, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void savePrices() {
        try (FileWriter writer = new FileWriter(skinPriceFile)) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, Integer> price : SkinHandler.skinPrices.entrySet()) {
                obj.addProperty(price.getKey(), price.getValue());
            }
            gson.toJson(obj, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void init() {
        unlockedSkinFile = new File(Util.getConfigFolder(), "\\unlockedSkins.json");
        skinPriceFile = new File(Util.getConfigFolder(), "\\skinPrices.json");
        load();
    }
}
