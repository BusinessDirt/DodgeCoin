package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.core.config.SkinHandler;
import businessdirt.dodgecoin.core.game.GameState;
import businessdirt.dodgecoin.core.game.KeyBinding;
import businessdirt.dodgecoin.core.game.KeyboardHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.gui.buttons.ImageButton;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Window {

    private static Window instance;
    public final JFrame frame;
    private static int width, height;
    private static Draw draw;
    private static int gameXStart;
    private static int playerSkinID = -1, backgroundSkinID = -1;

    private static GameState gameState;
    public static List<ImageButton> buttons;
    public static List<ImageButton> shopButtons;

    private Window() {
        Window.width = Toolkit.getDefaultToolkit().getScreenSize().width;
        Window.height = Toolkit.getDefaultToolkit().getScreenSize().height;
        Window.gameXStart = (Window.getWidth() - Constants.GAME_WIDTH - Constants.X_OFFSET) / 2;
        Window.gameState = GameState.MAIN_MENU;
        frame = new JFrame();
        Util.logEvent("Window initialized!");
    }

    public static void start() {
        instance.frame.setSize(Window.getWidth(), Window.getHeight());
        instance.frame.setMaximumSize(new Dimension(Window.getWidth(), Window.getHeight()));
        instance.frame.setMinimumSize(new Dimension(Window.getWidth(), Window.getHeight()));
        instance.frame.setResizable(true);
        instance.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instance.frame.setLocationRelativeTo(null);
        instance.frame.setResizable(true);
        instance.frame.setTitle("DodgeCoin");

        instance.frame.add(KeyBinding.getKeyListener());
        KeyboardHandler.get().registerKeyBindings();

        try {
            Window.buttons = get().createButtons();
            Window.shopButtons = get().createShopButtons();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Window.getDraw().setVisible(true);
        Window.getDraw().setBounds(0, 0, Window.width, Window.height);
        instance.frame.add(Window.getDraw());

        instance.frame.setVisible(true);
        Util.logEvent("Window created!");
    }

    private List<ImageButton> createButtons() throws IOException {
        List<ImageButton> list = new LinkedList<>();

        BufferedImage shopIcon = AssetPool.getImage("textures/gui/shop.png");
        ImageButton shop = new ImageButton(25, Window.getHeight() - Constants.Y_OFFSET - 25 - shopIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                shopIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, shopIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, shopIcon, e -> {
                    if (Window.getGameState() == GameState.MAIN_MENU || Window.getGameState() == GameState.SETTINGS) {
                        Window.setGameState(GameState.SHOP);
                        // disable all buttons except the cancel button
                        for (ImageButton b : buttons) {
                            if (!Objects.equals(b.getName(), "cancel")) b.setEnabled(false);
                        }

                        // enable shop buttons
                        for (ImageButton b : Window.shopButtons) {
                            if (!b.getName().equals("left") && !b.getName().equals("right")) {
                                b.setEnabled(true);
                            } else {
                                if (Shop.getPages() > 1) b.setEnabled(true);
                            }
                        }
                    }
                }, "shop");
        list.add(shop);

        BufferedImage cancelIcon = AssetPool.getImage("textures/gui/cancel.png");
        ImageButton cancel = new ImageButton((Window.getWidth() / 2) - (cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER / 2) - Constants.X_OFFSET / 2,
                Window.getHeight() - Constants.Y_OFFSET - 25 - cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon, e -> {
                    if (Window.getGameState() == GameState.MAIN_MENU) System.exit(0);
                    KeyboardHandler.get().cancel();
                }, "cancel");
        list.add(cancel);

        BufferedImage settingsIcon = AssetPool.getImage("textures/gui/settings.png");
        ImageButton settings = new ImageButton(Window.getWidth() - Constants.X_OFFSET - 25 - settingsIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER,
                Window.getHeight() - Constants.Y_OFFSET - 25 - settingsIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                settingsIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, settingsIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, settingsIcon, e -> {
                    if (Window.getGameState() == GameState.MAIN_MENU || Window.getGameState() == GameState.SHOP) {
                        Window.setGameState(GameState.SETTINGS);
                        for (ImageButton b : buttons) {
                            if (!Objects.equals(b.getName(), "cancel")) b.setEnabled(false);
                        }
                    }
                }, "settings");
        list.add(settings);
        return list;
    }

    private List<ImageButton> createShopButtons() throws IOException {
        List<ImageButton> buttons = new LinkedList<>();
        AtomicReference<List<String>> images = new AtomicReference<>(Shop.getShopItems());
        int x = Window.getWidth() / 5;
        int y = Window.getHeight() / 4;

        for (int i = 0; i < 6; i++) {
            int xPos = 50;
            int yPos = 50;
            if (i < 3) {
                xPos = x * (i + 1) - 15 + 15 * i;
                yPos = y - 7;
            } else {
                xPos = x  * (i - 2) - 15 + 15 * (i - 3);
                yPos = (y * 2 + 8);
            }

            BufferedImage image = images.get().size() <= i ? null : AssetPool.getImage(images.get().get(i));
            String name = i < images.get().size() ? images.get().get(i) : "$null$";

            int finalI = i;
            ImageButton button = new ImageButton(xPos, yPos, x, y, ImageButton.fitImageToSize(image, x, y), e -> {
                String itemName = finalI < images.get().size() ? images.get().get(finalI) : "$null$";
                int price = 0;
                if (SkinHandler.skinPrices.containsKey(itemName)) price = SkinHandler.skinPrices.get(itemName);

                if (!SkinHandler.unlockedSkins.containsKey(itemName) && !itemName.endsWith("/default.png")) return;
                if (Config.money >= price && SkinHandler.unlockedSkins.containsKey(itemName) && !SkinHandler.unlockedSkins.get(itemName)) {
                    if (itemName.startsWith("textures/players/")) setSkin(itemName, finalI + (Shop.getPage() - 1) * 6, backgroundSkinID);
                    else if (itemName.startsWith("textures/backgrounds/")) setSkin(itemName, playerSkinID, finalI + (Shop.getPage() - 1) * 6);

                    SkinHandler.unlockedSkins.put(itemName, true);
                    SkinHandler.save();

                    Config.money -= price;
                    Config.getConfig().markDirty();
                    Config.getConfig().writeData();

                    for (int j = 0; j < 6; j++) {
                        updateShopUI(j < Shop.getShopItems().size() ? Shop.getShopItems().get(j) : "$null$", shopButtons.get(j), j + Shop.getPage());
                    }

                    Util.logEvent("Bought " + name + " for " + price);
                } else if ((SkinHandler.unlockedSkins.containsKey(itemName) && SkinHandler.unlockedSkins.get(itemName)) || itemName.endsWith("/default.png")) {
                    if (itemName.startsWith("textures/players/")) setSkin(itemName, finalI + (Shop.getPage() - 1) * 6, backgroundSkinID);
                    else if (itemName.startsWith("textures/backgrounds/")) setSkin(itemName, playerSkinID, finalI + (Shop.getPage() - 1) * 6);

                    Config.getConfig().markDirty();
                    Config.getConfig().writeData();

                    for (int j = 0; j < Shop.getShopItems().size(); j++) {
                        updateShopUI(Shop.getShopItems().get(j), shopButtons.get(j), j + (Shop.getPage() - 1) * 6);
                    }
                } else if (price >= Config.money) Util.logEvent("Insufficient funds to purchase skin['" + itemName + "'], missing " + (price - Config.money) + "$!");
            }, "shop" + i);
            button.setEnabled(false);
            button.getButton().setBorder(BorderFactory.createLineBorder(Color.RED, 4, true));
            button.getButton().setFocusPainted(false);
            button.getButton().setBackground(Color.LIGHT_GRAY);
            button.getButton().setModel(new DefaultButtonModel() {
                @Override
                public boolean isPressed() {
                    return false;
                }

                @Override
                public boolean isRollover() {
                    return false;
                }
            });
            updateShopUI(name, button, i + Shop.getPage());
            buttons.add(button);
        }

        BufferedImage leftArrowIcon = AssetPool.getImage("textures/gui/arrow_left.png");
        BufferedImage rightArrowIcon = AssetPool.getImage("textures/gui/arrow_right.png");

        int arrowY = (Window.getHeight() - Constants.Y_OFFSET - leftArrowIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER) / 2;

        ImageButton leftArrow = new ImageButton(25, arrowY, leftArrowIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER,
                leftArrowIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, leftArrowIcon, e -> {
            Util.logEvent("Set shop page to " + Shop.decreasePage());
            images.set(Shop.getShopItems());
            for (int i = 0; i < 6; i++) {
                if (i < Shop.getShopItems().size()) updateShopUI(Shop.getShopItems().get(i), shopButtons.get(i), i + (Shop.getPage() - 1) * 6);
                try {
                    BufferedImage image = i >= images.get().size() ? null : AssetPool.getImage(images.get().get(i));
                    if (image != null) {
                        shopButtons.get(i).setEnabled(true);
                        shopButtons.get(i).setImage(ImageButton.fitImageToSize(image, x, y));
                    } else {
                        shopButtons.get(i).setEnabled(false);
                    }
                } catch (IOException ex) { throw new RuntimeException(ex); }
            }
        }, "left");
        leftArrow.setEnabled(false);
        buttons.add(leftArrow);

        ImageButton rightArrow = new ImageButton(Window.getWidth() - Constants.X_OFFSET - 25 - rightArrowIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, arrowY,
                rightArrowIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER,
                rightArrowIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, rightArrowIcon, e -> {
            Util.logEvent("Set shop page to " + Shop.increasePage());
            images.set(Shop.getShopItems());
            for (int i = 0; i < 6; i++) {
                if (i < Shop.getShopItems().size()) updateShopUI(Shop.getShopItems().get(i), shopButtons.get(i), i + (Shop.getPage() - 1) * 6);
                try {
                    BufferedImage image = i >= images.get().size() ? null : AssetPool.getImage(images.get().get(i));
                    if (image != null) {
                        shopButtons.get(i).setEnabled(true);
                        shopButtons.get(i).setImage(ImageButton.fitImageToSize(image, x, y));
                    } else {
                        shopButtons.get(i).setEnabled(false);
                    }
                } catch (IOException ex) { throw new RuntimeException(ex); }
            }
        }, "right");
        rightArrow.setEnabled(false);
        buttons.add(rightArrow);

        return buttons;
    }

    private static void setSkin(String skin, int buttonID, int buttonID2) {
        // Set color for buttons
        shopButtons.forEach(button -> {
            if (!Objects.equals(button.getName(), "left") && !Objects.equals(button.getName(), "right"))
                button.getButton().setBorder(BorderFactory.createLineBorder(Color.RED, 4));
        });

        Window.playerSkinID = buttonID;
        Window.backgroundSkinID = buttonID2;

        try {
            if (skin.startsWith("textures/players")) Window.getDraw().getPlayer().setImage(AssetPool.getImage(skin));
            else if (skin.startsWith("textures/backgrounds")) Window.getDraw().getBackgroundS().setImage(AssetPool.getImage(skin));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (skin.startsWith("textures/players/")) {
            Config.playerSkin = skin;
        } else {
            Config.backgroundSkin = skin;
        }

        Util.logEvent("Changed skin to " + skin);
    }

    private static void updateShopUI(String name, ImageButton button, int i) {
        if (Objects.equals(button.getName(), "left") || Objects.equals(button.getName(), "right")) return;
        if (i - (Shop.getPage() - 1) * 6 < 6) {
            if (Objects.equals(name, Config.playerSkin)) {
                button.getButton().setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                playerSkinID = i + (Shop.getPage() - 1) * 6;
            } else if (Objects.equals(name, Config.backgroundSkin)) {
                button.getButton().setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                backgroundSkinID = i + (Shop.getPage() - 1) * 6;
            } else {
                if ((SkinHandler.unlockedSkins.containsKey(name) && SkinHandler.unlockedSkins.get(name)) || name.endsWith("/default.png")) {
                    button.getButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
                } else {
                    button.getButton().setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                }
            }
        } else {
            if ((SkinHandler.unlockedSkins.containsKey(name) && SkinHandler.unlockedSkins.get(name)) || name.endsWith("/default.png")) {
                button.getButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
            } else {
                button.getButton().setBorder(BorderFactory.createLineBorder(Color.RED, 4));
            }
        }
    }

    public static Window get() {
        if (Window.instance == null) Window.instance = new Window();
        return Window.instance;
    }

    public static Draw getDraw() {
        if (Window.draw == null) Window.draw = new Draw();
        return Window.draw;
    }

    public static int getWidth() {
        return Window.width;
    }

    public static int getHeight() {
        return Window.height;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        Window.gameState = gameState;
    }

    public static void setHeight(int height) {
        Window.height = height;
    }

    public static void setWidth(int width) {
        Window.width = width;
    }

    public static int getGameXStart() {
        return gameXStart;
    }
}