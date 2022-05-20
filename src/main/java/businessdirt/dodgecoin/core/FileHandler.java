package businessdirt.dodgecoin.core;

import businessdirt.dodgecoin.core.config.SkinHandler;
import businessdirt.dodgecoin.gui.AssetPool;
import businessdirt.dodgecoin.gui.Shop;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class FileHandler {

    private static FileHandler instance;

    public InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public Font getFontFromResource(String fileName) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT , get().getFileFromResourceAsStream(fileName));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BufferedImage getImageFromResource(String fileName) throws IOException {
        return ImageIO.read(getFileFromResourceAsStream(fileName));
    }

    public String getPath(String resourceFolder) {
        return Objects.requireNonNull(getClass().getClassLoader().getResource(resourceFolder)).getPath();
    }

    public static void loadAssets() throws IOException {

        // load all coin sprites
        List<File> coinFiles = listFiles("textures/coins/");
        for (File file : coinFiles) {
            AssetPool.getImage("textures/coins/" + file.getName());
        }

        // load all gui-item sprites
        List<File> guiFiles = listFiles("textures/gui/");
        for (File file : guiFiles) {
            AssetPool.getImage("textures/gui/" + file.getName());
        }

        // load all player-skin sprites
        List<File> playerFiles = listFiles("textures/players/");
        for (File file : playerFiles) {
            String name = "textures/players/".concat(file.getName());
            AssetPool.getImage(name);

            // add the skin to it's corresponding save-file if it is not already in it
            if (!file.getName().equals("default.png") && !SkinHandler.unlockedSkins.containsKey(name)) {
                SkinHandler.unlockedSkins.put(name, false);
                SkinHandler.save();
            }

            // add the price of the skin to it's corresponding save-file; default = 1.000.000
            if (!file.getName().equals("default.png") && !SkinHandler.skinPrices.containsKey(name)) {
                SkinHandler.skinPrices.put(name, 1000000);
                SkinHandler.savePrices();
            }
        }

        // load all background sprites
        List<File> backgroundFiles = FileHandler.listFiles("textures/backgrounds/");
        for (File file : backgroundFiles) {
            String name = "textures/backgrounds/".concat(file.getName());
            AssetPool.getImage(name);

            // add the skin to it's corresponding save-file if it is not already in it
            if (!file.getName().equals("default.png") && !SkinHandler.unlockedSkins.containsKey(name)) {
                SkinHandler.unlockedSkins.put(name, false);
                SkinHandler.save();
            }

            // add the price of the skin to it's corresponding save-file; default = 100.000
            if (!file.getName().equals("default.png") && !SkinHandler.skinPrices.containsKey(name)) {
                SkinHandler.skinPrices.put(name, 100000);
                SkinHandler.savePrices();
            }
        }

        // set the shop pages to match the amount of skins available
        int shopItems = playerFiles.size() + backgroundFiles.size();
        int shopPages = (int) Math.ceil((double) shopItems / 6);
        Shop.setPages(shopPages == 0 ? 1 : shopPages);

        // log how many sprites where loaded
        Util.logEvent("Loaded " + coinFiles.size() + " coins, " + guiFiles.size() + " gui items, " + playerFiles.size() + " player skins and " + backgroundFiles.size() + " backgrounds");
    }

    public static List<File> listFiles(String directoryName) {
        File directory = new File(FileHandler.get().getPath(directoryName));

        // get all the files from a directory
        File[] files = directory.listFiles();
        List<File> resultList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(files)));
        for (File file : files) {
            if (file.isDirectory()) resultList.addAll(listFiles(file.getAbsolutePath()));
        }
        return resultList;
    }

    public static FileHandler get() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }
}
