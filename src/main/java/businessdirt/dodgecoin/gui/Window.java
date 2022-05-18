package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.core.game.GameState;
import businessdirt.dodgecoin.core.game.KeyBinding;
import businessdirt.dodgecoin.core.game.KeyboardHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.gui.buttons.ImageButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Window {

    private static Window instance;
    public final JFrame frame;
    private static int width, height;
    private static Draw draw;
    private static int gameXStart;

    private static GameState gameState;
    public static List<ImageButton> buttons;

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
        instance.frame.setResizable(true);
        instance.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instance.frame.setLocationRelativeTo(null);
        instance.frame.setResizable(false);
        instance.frame.setTitle("DodgeCoin");

        instance.frame.add(KeyBinding.getKeyListener());
        KeyboardHandler.get().registerKeyBindings();

        try {
            Window.buttons = get().createButtons();
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

        BufferedImage shopIcon = AssetPool.getImage("gui/shop.png");
        ImageButton shop = new ImageButton(25, Window.getHeight() - Constants.Y_OFFSET - 25 - shopIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                shopIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, shopIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, shopIcon, e -> {
                    if (Window.getGameState() == GameState.MAIN_MENU || Window.getGameState() == GameState.SETTINGS) {
                        Window.setGameState(GameState.SHOP);
                        for (ImageButton b : buttons) {
                            if (!Objects.equals(b.getName(), "cancel")) b.setEnabled(false);
                        }
                    }
                }, "shop");
        list.add(shop);

        BufferedImage cancelIcon = AssetPool.getImage("gui/cancel.png");
        ImageButton cancel = new ImageButton((Window.getWidth() / 2) - (cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER / 2) - Constants.X_OFFSET / 2,
                Window.getHeight() - Constants.Y_OFFSET - 25 - cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon, e -> {
                    KeyboardHandler.get().cancel();
                }, "cancel");
        list.add(cancel);

        BufferedImage settingsIcon = AssetPool.getImage("gui/settings.png");
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

