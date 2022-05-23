package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.FileHandler;
import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.core.config.SkinHandler;
import businessdirt.dodgecoin.core.game.GameClock;
import businessdirt.dodgecoin.core.game.GameState;
import businessdirt.dodgecoin.gui.images.Coin;
import businessdirt.dodgecoin.gui.images.Sprite;
import com.github.businessdirt.config.data.Property;
import com.github.businessdirt.config.data.PropertyType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class Draw extends JLabel  {

    private final List<Coin> coins = new ArrayList<>();
    private Sprite player;
    private Sprite background;
    private boolean hitboxes = false;

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
            if (background != null) {
                this.drawImage(g2d, background);
                g2d.fillRect(0, 0, Window.getGameXStart(), Window.getHeight());
                g2d.fillRect(Window.getGameXStart() + Constants.GAME_WIDTH, 0, Window.getGameXStart(), Window.getHeight());
            }

            // coins
            try {
                for (Sprite coin : coins) {
                    if (coin.isDraw()) this.drawImage(g2d, coin);
                    if (hitboxes) g2d.drawRect(coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight());
                }
            } catch (ConcurrentModificationException ignored) {}

            // player
            if (player != null) {
                this.drawImage(g2d, player);
                if (hitboxes) g2d.drawRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
            }

            // money
            g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(20.0f));
            g2d.setColor(Color.WHITE);
            g2d.drawString("Money: " + (int) Config.money, Constants.X_OFFSET  + 50, Constants.Y_OFFSET + 50);

            //Combo
            g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(20.0f));
            g2d.setColor(Color.WHITE);
            g2d.drawString("Combo: " + (float) GameClock.combo/10, Constants.X_OFFSET  + 50, Constants.Y_OFFSET + 90);
            // pause / game over TODO better screen overlay
            if (Window.getGameState() == GameState.PAUSE) {
                drawOverlay(g2d, new Color(8, 255, 0, 224));
                g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(20.0f));
                g2d.setColor(Color.WHITE);
                g2d.drawString("Paused", Constants.X_OFFSET  + 570, Constants.Y_OFFSET + 320);
            } else if (Window.getGameState() == GameState.GAME_OVER) {
                drawOverlay(g2d, new Color(255, 0, 0, 224));
                g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(20.0f));
                g2d.setColor(Color.WHITE);
                g2d.drawString("GAME OVER", Constants.X_OFFSET  + 520, Constants.Y_OFFSET + 320);
            }
        } else if (Window.getGameState() == GameState.MAIN_MENU) { // Main Menu
            // background
            // TODO: add background
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // logo
            g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(34.0f));
            g2d.setColor(Color.WHITE);
            drawCenteredStringX(g2d, "DodgeCoin", (Window.getHeight() - g2d.getFontMetrics(g2d.getFont()).getHeight()) / 2);

            //start instructions
            g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(34.0f));
            g2d.setColor(Color.WHITE);
            drawCenteredStringX(g2d, "Press [ENTER] to start", (int) (Window.getHeight() * (3f / 4f)));

            // money
            g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(34.0f));
            g2d.setColor(Color.WHITE);
            drawCenteredStringX(g2d, "Money: " + (int) Config.money, Constants.Y_OFFSET + 90 - g2d.getFontMetrics(g2d.getFont()).getHeight());
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

        g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(34.0f));
        g2d.setColor(Color.WHITE);

        drawCenteredStringX(g2d, "Shop", Constants.Y_OFFSET + 90 - g2d.getFontMetrics().getHeight());
        g2d.fillRect(50, Constants.Y_OFFSET + 50, Window.getWidth() - Constants.X_OFFSET - 100, 10);
        drawCenteredStringX(g2d, "Money: " + Config.money, Constants.Y_OFFSET + 160);

        // draw Item prices
        g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(16.0f));
        int x = Window.getWidth() / 5;
        int y = Window.getHeight() / 4;

        List<String> items = Shop.getShopItems();
        for (int i = 0; i < items.size(); i++) {
            int xPos = 0, yPos = 0;
            if (i < 3) {
                xPos = x * (i + 1) - 10 + 15 * i;
                yPos = y - 15;
            } else {
                xPos = x * (i - 2) - 10 + 15 * (i - 3);
                yPos = (y * 3 + 35);
            }

            // format the price for better reading
            if (!items.get(i).endsWith("/default.png") || (SkinHandler.skinPrices.containsKey(items.get(i)) && !SkinHandler.unlockedSkins.get(items.get(i)))) {
                NumberFormat nf = NumberFormat.getInstance(new Locale("de", "DE"));
                int price = SkinHandler.skinPrices.get(items.get(i));
                g2d.drawString(nf.format(price), xPos, yPos);
            }
        }
    }

    private void drawSettings(Graphics2D g2d) throws IllegalAccessException, IOException {
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setFont(FileHandler.get().getFontFromResource("fonts/8-bit.ttf").deriveFont(34.0f));
        g2d.setColor(Color.WHITE);

        drawCenteredStringX(g2d, "Settings", Constants.Y_OFFSET + 90 - g2d.getFontMetrics().getHeight());
        g2d.fillRect(50, Constants.Y_OFFSET + 50, Window.getWidth() - Constants.X_OFFSET - 100, 10);

        BufferedImage cancelIcon = AssetPool.getImage("textures/gui/cancel.png");
        drawImage(g2d, new Sprite((Window.getWidth() / 2) - (cancelIcon.getWidth() * Constants.ICON_SIZE_MULTIPLIER / 2) - Constants.X_OFFSET / 2,
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

    public void drawImage(Graphics2D g2d, Sprite sprite) {
        g2d.drawImage(sprite.getImage(), sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), this);
    }

    private void drawCenteredStringX(Graphics2D g2d, String str, int y) {
        g2d.drawString(str, (Window.getWidth() - Constants.X_OFFSET - g2d.getFontMetrics().stringWidth(str)) / 2, y);
    }

    public void toggleHitboxes() {
        this.hitboxes = !this.hitboxes;
    }

    public void addCoin(Coin coin) {
        this.coins.add(coin);
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setPlayer(Sprite player) {
        this.player = player;
    }

    public Sprite getPlayer() {
        return this.player;
    }

    public void setBackground(Sprite background) {
        this.background = background;
    }

    public Sprite getBackgroundS() {
        return background;
    }
}
