package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.core.game.GameClock;
import businessdirt.dodgecoin.core.game.GameState;
import businessdirt.dodgecoin.gui.buttons.ImageButton;
import businessdirt.dodgecoin.gui.images.Coin;
import businessdirt.dodgecoin.gui.images.Image;
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

public class Draw extends JLabel  {

    private final List<Coin> coins = new ArrayList<>();
    private Image player;
    private Image background;

    //settings input
    private static JTextField verticalResolution;

    private static JFrame frame;

    private static JButton button;

    private static JLabel label;

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // game
        if (Window.getGameState() == GameState.GAME || Window.getGameState() == GameState.PAUSE || Window.getGameState() == GameState.GAME_OVER) {
            // background
            if (background == null) {
                //this.drawImage(g2d, background);
                g2d.fillRect(0, 0, Window.getGameXStart(), Window.getHeight());
                g2d.fillRect(Window.getGameXStart() + Constants.GAME_WIDTH, 0, Window.getGameXStart(), Window.getHeight());
            }

            // coins
            try {
                for (Image coin : coins) {
                    if (coin.isDraw()) this.drawImage(g2d, coin);
                }
            } catch (ConcurrentModificationException ignored) {}

            // player
            if (player != null) {
                this.drawImage(g2d, player);
            }

            // score
            g2d.setColor(Color.WHITE);
            g2d.drawString("Score: " + GameClock.getScore(), Constants.X_OFFSET  + 50, Constants.Y_OFFSET + 50);

            // pause / game over TODO better screen overlay
            if (Window.getGameState() == GameState.PAUSE) {
                drawOverlay(g2d, new Color(255, 196, 0, 224));
                g2d.setColor(Color.WHITE);
                g2d.drawString("Pause", Constants.X_OFFSET + 50, Constants.Y_OFFSET + 50);
                g2d.drawString("Press [SPACE] or [ENTER] to resume or [ESC] to quit to main menu", Constants.X_OFFSET + 50, Constants.Y_OFFSET + 80);
            } else if (Window.getGameState() == GameState.GAME_OVER) {
                drawOverlay(g2d, new Color(255, 0, 0, 224));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.drawString("Game Over", Constants.X_OFFSET + 50, Constants.Y_OFFSET + 50);
            }
        } else if (Window.getGameState() == GameState.MAIN_MENU) { // Main Menu
            // background
            // TODO: add background
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // logo
            g2d.setColor(Color.WHITE);
            g2d.drawString("Dodge Coin", Constants.X_OFFSET + 50, Constants.Y_OFFSET + 50);

            //start instructions
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press [ENTER] to start",Constants.X_OFFSET + 50, Constants.Y_OFFSET + 80);

            // reset score
            GameClock.setScore(0);
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

    private void drawOverlay(Graphics2D g2d, Color color) {
        g2d.setColor(color);
        g2d.fillRect(Window.getWidth() / 4, Window.getHeight() / 4, Window.getWidth() / 2, Window.getHeight() / 2);
    }

    private void drawShop(Graphics2D g2d) throws IOException {
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("Shop", Constants.X_OFFSET + 50, Constants.Y_OFFSET + 50);
        g2d.drawString("Press [ESC] or [BACKSPACE] to return to main menu", Constants.X_OFFSET + 50, Constants.Y_OFFSET +80);

        BufferedImage cancelIcon = AssetPool.getImage("gui/cancel.png");
        drawImage(g2d, new businessdirt.dodgecoin.gui.images.Image((Window.getWidth() / 2) - (cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER / 2) - Constants.X_OFFSET / 2,
                Window.getHeight() - Constants.Y_OFFSET - 25 - cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon));
    }

    private void drawSettings(Graphics2D g2d) throws IllegalAccessException, IOException {
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("Settings", 75, 25);
        g2d.drawString("Press [ESC] or [BACKSPACE] to return to main menu", 75, 50);
        g2d.fillRect(50, Constants.Y_OFFSET + 50, Window.getWidth() - Constants.X_OFFSET - 100, 10);
        g2d.drawString("Vertical Resolution: " + Window.getHeight(), Constants.X_OFFSET + 50, Constants.Y_OFFSET + 80);

        BufferedImage cancelIcon = AssetPool.getImage("gui/cancel.png");
        drawImage(g2d, new businessdirt.dodgecoin.gui.images.Image((Window.getWidth() / 2) - (cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER / 2) - Constants.X_OFFSET / 2,
                Window.getHeight() - Constants.Y_OFFSET - 25 - cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER,
                cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon.getHeight() * Constants.ICON_SIZE_MULTIPLIER, cancelIcon));

        int i = 0;
        for (Field field : Config.class.getDeclaredFields()) {
            int y = Constants.Y_OFFSET + 50 + i * 75;
            if (field.isAnnotationPresent(Property.class)) {
                Property property = field.getAnnotation(Property.class);

                if (!property.hidden()) {
                    g2d.drawRect(75, y, Window.getWidth() - Constants.X_OFFSET - 150, 75);
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

    public void drawImage(Graphics2D g2d, Image image) {
        g2d.drawImage(image.getImage(), image.getX(), image.getY(), image.getWidth(), image.getHeight(), this);
    }

    public void addCoin(Coin coin) {
        this.coins.add(coin);
    }

    public List<Coin> getCoins() {
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
