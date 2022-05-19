package businessdirt.dodgecoin.core;

import businessdirt.dodgecoin.gui.AssetPool;
import businessdirt.dodgecoin.gui.Shop;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public BufferedImage getImageFromResource(String fileName) throws IOException {
        return ImageIO.read(getFileFromResourceAsStream(fileName));
    }

    public String getPath(String resourceFolder) {
        return Objects.requireNonNull(getClass().getClassLoader().getResource(resourceFolder)).getPath();
    }

    public static void loadAssets() throws IOException {

        // coins
        List<File> coinFiles = listFiles("coins/");
        for (File file : coinFiles) {
            AssetPool.getImage("coins/" + file.getName());
        }

        // gui
        List<File> guiFiles = listFiles("gui/");
        for (File file : guiFiles) {
            AssetPool.getImage("gui/" + file.getName());
        }

        // players
        List<File> playerFiles = listFiles("players/");
        for (File file : playerFiles) {
            AssetPool.getImage("players/" + file.getName());
        }

        // background
        List<File> backgroundFiles = FileHandler.listFiles("backgrounds/");
        for (File file : backgroundFiles) {
            AssetPool.getImage("backgrounds/" + file.getName());
        }

        // set shop pages; subtract 1 from each for default skins
        int shopItems = playerFiles.size() + backgroundFiles.size() - 2;
        int shopPages = shopItems / 6;
        Shop.setPages(shopPages == 0 ? 1 : shopPages);

        Util.logEvent("Loaded " + coinFiles.size() + " coins, " + guiFiles.size() + " gui items, " + playerFiles.size() + " player skins");
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
