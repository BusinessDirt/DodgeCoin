package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Shop {

    private static int pages = 5;
    private static int page = 1;
    private static final List<String> shopItems = new LinkedList<>();

    public static void loadShopItems() throws IOException {
        // player skins
        List<File> playerFiles = FileHandler.listFiles("textures/players/");
        for (File file : playerFiles) {
            shopItems.add("textures/players/".concat(file.getName()));
        }

        List<File> backgroundFiles = FileHandler.listFiles("textures/backgrounds/");
        for (File file : backgroundFiles) {
            shopItems.add("textures/backgrounds/".concat(file.getName()));
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
    public static int getPage() {
        return Shop.page;
    }

    public static List<String> getShopItems() {
        int startIndex = (page - 1) * 6;
        int endIndex = page * 6;
        return Shop.shopItems.subList(startIndex, Math.min(endIndex, Shop.shopItems.size()));
    }
}
