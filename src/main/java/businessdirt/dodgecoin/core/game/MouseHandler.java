package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.gui.AssetPool;
import businessdirt.dodgecoin.gui.Draw;
import businessdirt.dodgecoin.gui.Window;
import businessdirt.dodgecoin.gui.buttons.Button;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MouseHandler extends MouseAdapter {

    private static MouseHandler instance;
    private final List<Button> buttonList;

    private MouseHandler() {
        buttonList = new LinkedList<>();
        try {
            loadButtons();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadButtons() throws IOException {
        // settings
        BufferedImage settingsIcon = AssetPool.getImage("gui/settings.png");
        buttonList.add(new Button(Window.getWidth() - Constants.X_OFFSET - 25 - settingsIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER,
                Window.getHeight() - Constants.Y_OFFSET - 25 - settingsIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                settingsIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, settingsIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, "settings")
        );

        // shop
        BufferedImage shopIcon = AssetPool.getImage("gui/shop.png");
        buttonList.add(new Button(25, Window.getHeight() - Constants.Y_OFFSET - 25 - shopIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                shopIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, shopIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, "shop")
        );

        // cancel
        BufferedImage cancelIcon = AssetPool.getImage("gui/shop.png");
        buttonList.add(new Button((Window.getWidth() / 2) - (cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER / 2) - Constants.X_OFFSET / 2,
                Window.getHeight() - Constants.Y_OFFSET - 25 - cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, "cancel")
        );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point mouse = e.getPoint();
        for (Button button : buttonList) {
            if (button.contains(mouse)) {
                switch (button.getName()) {
                    case "settings":
                        if (Window.getGameState() == GameState.MAIN_MENU) Window.setGameState(GameState.SETTINGS);
                        break;
                    case "shop":
                        if (Window.getGameState() == GameState.MAIN_MENU) Window.setGameState(GameState.SHOP);
                        break;
                    case "cancel":
                        if (Window.getGameState() == GameState.SHOP || Window.getGameState() == GameState.SETTINGS) {
                            Window.setGameState(GameState.MAIN_MENU);
                        } else if (Window.getGameState() == GameState.MAIN_MENU) {
                            System.exit(0);
                        }
                        break;
                    default:
                        break;
                }
                Util.logEvent("Mouse clicked on button " + button.hashCode() + ", image=" + button.getName());
            }
        }
    }

    public static MouseHandler get() {
        if (instance == null) instance = new MouseHandler();
        return instance;
    }
}
