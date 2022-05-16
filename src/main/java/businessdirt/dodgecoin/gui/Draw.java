package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.game.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
                }
            } catch (ConcurrentModificationException ignored) {}

            // player
            if (player != null) drawImage(g2d, player);

            // pause / game over TODO better screen overlay
            if (Window.getGameState() == GameState.PAUSE) {
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(Color.WHITE);
                g2d.drawString("Pause", X_OFFSET + 50, Y_OFFSET + 50);
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

            } catch (IOException e) {
                e.printStackTrace();
            }

            // play button
        } else if (Window.getGameState() == GameState.SHOP) { // Shop
            drawShop(g2d);
        } else if (Window.getGameState() == GameState.SETTINGS) { // Settings
            drawSettings(g2d);
        }

        repaint();
    }

    private void drawShop(Graphics2D g2d) {
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("Shop", X_OFFSET + 50, Y_OFFSET + 50);
    }

    private void drawSettings(Graphics2D g2d) {
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.drawString("Settings", X_OFFSET + 50, Y_OFFSET + 50);
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
