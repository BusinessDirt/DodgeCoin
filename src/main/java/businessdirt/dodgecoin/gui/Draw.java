package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.game.GameClock;
import businessdirt.dodgecoin.core.game.GameState;
import businessdirt.dodgecoin.core.game.MouseHandler;
import com.github.businessdirt.config.data.Property;
import com.github.businessdirt.config.data.PropertyType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class Draw extends JLabel {

    private final List<Image> coins = new ArrayList<>();
    private Image player;
    private Image background;

    public static final int X_OFFSET = 17;
    public static final int Y_OFFSET = 40;
    public static final int ICON_SIZE_MULTIPLIER = 6;



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // game
        if (Window.getGameState() == GameState.GAME || Window.getGameState() == GameState.PAUSE || Window.getGameState() == GameState.GAME_OVER) {
            // background
            if (background != null) drawImage(g2d, background);

            // coins
            try {
                for (Image coin : coins) {
                    if (coin.isDraw()) drawImage(g2d, coin);
                    g2d.drawString("Score: " + GameClock.getScore(), X_OFFSET  + 50, Y_OFFSET + 50);
                }
            } catch (ConcurrentModificationException ignored) {}

            // player
            if (player != null) drawImage(g2d, player);

            // pause / game over TODO better screen overlay
            if (Window.getGameState() == GameState.PAUSE) {
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.drawString("Pause", X_OFFSET + 50, Y_OFFSET + 50);
                g2d.drawString("Press [SPACE] or [ENTER] to resume or [ESC] to quit to main menu", X_OFFSET + 50, Y_OFFSET + 80);
            } else if (Window.getGameState() == GameState.GAME_OVER) {
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.drawString("Game Over", X_OFFSET + 50, Y_OFFSET + 50);
            }
        } else if (Window.getGameState() == GameState.MAIN_MENU) { // Main Menu
            // background
            // TODO: add background
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // logo
            g2d.setColor(Color.WHITE);
            g2d.drawString("Dodge Coin", X_OFFSET + 50, Y_OFFSET + 50);

            //start instructions
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press [ENTER] to start",X_OFFSET + 50, Y_OFFSET + 80);

            // reset score
            GameClock.setScore(0);

            // shop icon
            try {
                BufferedImage shopIcon = AssetPool.getImage("gui/shop.png");
                drawImage(g2d, new Image(25, Window.getHeight() - Y_OFFSET - 25 - shopIcon.getHeight() * ICON_SIZE_MULTIPLIER,
                        shopIcon.getWidth() * ICON_SIZE_MULTIPLIER, shopIcon.getHeight() * ICON_SIZE_MULTIPLIER, shopIcon));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // settings icon
            try {
                BufferedImage settingsIcon = AssetPool.getImage("gui/settings.png");
                drawImage(g2d, new Image(Window.getWidth() - X_OFFSET - 25 - settingsIcon.getWidth() * ICON_SIZE_MULTIPLIER,
                        Window.getHeight() - Y_OFFSET - 25 - settingsIcon.getHeight() * ICON_SIZE_MULTIPLIER,
                        settingsIcon.getWidth() * ICON_SIZE_MULTIPLIER, settingsIcon.getHeight() * ICON_SIZE_MULTIPLIER, settingsIcon));

                BufferedImage cancelIcon = AssetPool.getImage("gui/cancel.png");
                drawImage(g2d, new Image((Window.getWidth() / 2) - (cancelIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER / 2) - X_OFFSET / 2,
                        Window.getHeight() - Draw.Y_OFFSET - 25 - cancelIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER,
                        cancelIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER, cancelIcon));

            } catch (IOException e) {
                e.printStackTrace();
            }

            // play button
        } else if (Window.getGameState() == GameState.SHOP) { // Shop
            try {
                drawShop(g2d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (Window.getGameState() == GameState.SETTINGS) { // Settings
            try {
                drawSettings(g2d);
            } catch (IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        }

        repaint();
    }

    private void drawShop(Graphics2D g2d) throws IOException {
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("Shop", X_OFFSET + 50, Y_OFFSET + 50);
        g2d.drawString("Press [ESC] or [BACKSPACE] to return to main menu", X_OFFSET + 50, Y_OFFSET +80);

        BufferedImage cancelIcon = AssetPool.getImage("gui/cancel.png");
        drawImage(g2d, new Image((Window.getWidth() / 2) - (cancelIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER / 2) - X_OFFSET / 2,
                Window.getHeight() - Draw.Y_OFFSET - 25 - cancelIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER,
                cancelIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER, cancelIcon));
    }

    private void drawSettings(Graphics2D g2d) throws IllegalAccessException, IOException {
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("Settings", 75, 25);
        g2d.drawString("Press [ESC] or [BACKSPACE] to return to main menu", 75, 50);
        g2d.fillRect(50, Y_OFFSET + 50, Window.getWidth() - X_OFFSET - 100, 10);

        BufferedImage cancelIcon = AssetPool.getImage("gui/cancel.png");
        drawImage(g2d, new Image((Window.getWidth() / 2) - (cancelIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER / 2) - X_OFFSET / 2,
                Window.getHeight() - Draw.Y_OFFSET - 25 - cancelIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER,
                cancelIcon.getWidth() * Draw.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Draw.ICON_SIZE_MULTIPLIER, cancelIcon));

        int i = 0;
        for (Field field : Config.class.getDeclaredFields()) {
            int y = Y_OFFSET + 50 + i * 75;
            if (field.isAnnotationPresent(Property.class)) {
                Property property = field.getAnnotation(Property.class);

                if (!property.hidden()) {
                    g2d.drawRect(75, y, Window.getWidth() - X_OFFSET - 150, 75);
                    g2d.drawString(property.description(), 110, y + 55);

                    if (property.type() == PropertyType.SWITCH) {
                        g2d.drawString(property.name() + "  " + field.getBoolean(Boolean.class), 100, y + 30);
                    } else if (property.type() == PropertyType.NUMBER) {
                        g2d.drawString(property.name() + "  " + field.getInt(Integer.class), 100, y + 30);
                    } else if (property.type() == PropertyType.TEXT) {
                        g2d.drawString(property.name() + "  " + field.get(String.class), 100, y + 30);
                    } else if (property.type() == PropertyType.SLIDER) {
                        g2d.drawString(property.name() + "  " + field.getDouble(Double.class), 100, y + 30);
                    }
                }
            }
            i++;
        }
    }

    private void drawImage(Graphics2D g2d, Image image) {
        g2d.drawImage(image.getImage(), image.getX(), image.getY(), image.getWidth(), image.getHeight(), this);
    }

    public void addCoin(Image coin) {
        this.coins.add(coin);
    }

    public List<Image> getCoins() {
        return coins;
    }

    public void setPlayer(Image player) {
        this.player = player;
    }

    public Image getPlayer() {
        return this.player;
    }

    public void setBackground(Image background) {
        this.background = background;
    }
}
