package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.gui.images.Sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Shop {

    private static int pages = 5;
    private static int page = 1;
    private static final List<BufferedImage> shopItems = new LinkedList<>();

    public static void loadShopItems() throws IOException {
        // player skins
        List<File> playerFiles = FileHandler.listFiles("players/");
        for (File file : playerFiles) {
            if (!file.getName().equals("default.png"))
                AssetPool.getImage("players/" + file.getName());
        }

        List<File> backgroundFiles = FileHandler.listFiles("backgrounds/");
        for (File file : backgroundFiles) {
            if (!file.getName().equals("default.png"))
                AssetPool.getImage("backgrounds/" + file.getName());
        }
    }
    public static int increasePage() {
        if (page < pages) {
            page++;
        } else {
            page = 1;
        }
        return page;
    }

    public static int decreasePage() {
        if (page > 1) {
            page--;
        } else {
            page = pages;
        }
        return page;
    }

    public static void setPages(int pages) {
        Shop.pages = pages;
    }

    public static int getPages() {
        return Shop.pages;
    }

    public static List<BufferedImage> getShopItems() {
        return Shop.shopItems;
    }
}
