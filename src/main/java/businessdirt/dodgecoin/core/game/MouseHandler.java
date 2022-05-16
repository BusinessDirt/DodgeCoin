package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.gui.AssetPool;
import businessdirt.dodgecoin.gui.Draw;
import businessdirt.dodgecoin.gui.Window;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MouseHandler extends MouseAdapter {

    private static MouseHandler instance;
    private List<Button> buttonList;

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
        buttonList.add(new Button(Window.getWidth() - Draw.X_OFFSET - 25 - settingsIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER,
                Window.getHeight() - Draw.Y_OFFSET - 25 - settingsIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER,
                settingsIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER, settingsIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER, "settings")
        );

        // shop
        BufferedImage shopIcon = AssetPool.getImage("gui/shop.png");
        buttonList.add(new Button(25, Window.getHeight() - Draw.Y_OFFSET - 25 - shopIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER,
                shopIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER, shopIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER, "shop")
        );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point mouse = e.getPoint();
        for (Button button : buttonList) {
            if (button.contains(mouse)) {
                switch (button.getImageName()) {
                    case "settings":
                        Window.setGameState(GameState.SETTINGS);
                        break;
                    case "shop":
                        Window.setGameState(GameState.SHOP);
                        break;
                    default:
                        break;
                }
                Util.logEvent("Mouse clicked on button " + button.hashCode() + ", image=" + button.getImageName());
            }
        }
    }

    public static MouseHandler get() {
        if (instance == null) instance = new MouseHandler();
        return instance;
    }
}
