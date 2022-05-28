package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.SkinHandler;
import businessdirt.dodgecoin.core.input.buttons.ShopButton;
import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.core.input.Keyboard;
import businessdirt.dodgecoin.core.input.buttons.ImageButton;
import businessdirt.dodgecoin.core.renderer.Renderer;
import businessdirt.dodgecoin.game.objects.Background;
import businessdirt.dodgecoin.game.objects.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopScreen extends ScreenAdapter {

    private final Renderer renderer;
    private final ImageButton left, right;
    private final ShopButton[] shop;

    // used for the actual shop
    public static List<String> shopItems = new ArrayList<>();
    private final int pages;
    private int page;

    @SuppressWarnings("all")
    public ShopScreen() {

        renderer = Renderer.newInstance();
        left = new ImageButton("textures/gui/arrow_left.png", 50, Constants.CENTER_Y - 50, 100, 100);
        right = new ImageButton("textures/gui/arrow_right.png", Constants.VIEWPORT_WIDTH - 150, Constants.CENTER_Y - 50, 100, 100);

        shop = new ShopButton[6];
        page = 1;
        pages = (int) Math.ceil((double) shopItems.size() / 6);
        int x = Constants.VIEWPORT_WIDTH / 5;
        int y = Constants.VIEWPORT_HEIGHT / 4;

        for (int i = 0; i < shop.length; i++) {
            int xPos;
            int yPos;
            if (i < 3) {
                xPos = x * (i + 1) - 15 + 15 * i;
                yPos = Constants.VIEWPORT_HEIGHT - (x * 2 - y + 20);
            } else {
                xPos = x  * (i - 2) - 15 + 15 * (i - 3);
                yPos = Constants.VIEWPORT_HEIGHT - (x + y * 2 + 10);
            }

            shop[i] = new ShopButton("", xPos, yPos, x, x);
            shop[i].setBackgroundColor(Color.DARK_GRAY);
        }

        for (int i = 0; i < shopItems.size(); i++) {
            String skinName = shopItems.get(i + (page - 1) * shop.length);
            if (!SkinHandler.unlockedSkins.containsKey(skinName)) {
                SkinHandler.unlockedSkins.put(skinName, skinName.contains("default.png"));
                SkinHandler.save();
            }
        }
        updateButtonUI();
    }

    @Override
    public void render(float delta) {
        // input
        if (Keyboard.keyTyped(Input.Keys.ESCAPE)) DodgeCoin.get().setScreen(new MenuScreen());
        if (Keyboard.keyDown(Input.Keys.ALT_LEFT) && Keyboard.keyDown(Input.Keys.F4)) {
            Gdx.app.exit();
            System.exit(0);
        }

        // shop pages
        if (left.isClicked()) {
            page -= 1;
            if (page <= 0) page = pages;
            updateButtonUI();
        }
        if (right.isClicked()) {
            page += 1;
            if (page > pages) page = 1;
            updateButtonUI();
        }

        // shop items
        for (int i = 0; i < shop.length; i++) {
            if (shop[i].isClicked()) {
                String skinName = shopItems.get(i + (page - 1) * shop.length);
                int price = 0;
                if (SkinHandler.skinPrices.containsKey(skinName)) price = SkinHandler.skinPrices.get(skinName);

                if (Config.money >= price && !SkinHandler.unlockedSkins.get(skinName)) {
                    setSkin(skinName);

                    Config.money -= price;
                    Config.getConfig().markDirty();
                    Config.getConfig().writeData();

                    SkinHandler.unlockedSkins.put(skinName, true);
                    SkinHandler.save();

                    updateButtonUI();
                } else if (SkinHandler.unlockedSkins.get(skinName)) {
                    setSkin(skinName);

                    Config.getConfig().markDirty();
                    Config.getConfig().writeData();

                    updateButtonUI();
                } else DodgeCoin.logEvent("Could not equip / buy the skin");
            }
        }

        // clear the screen from the previous frame
        Gdx.gl.glClearColor(0, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Renderer.get().drawCenteredString("Money: " + Config.money, 0, Constants.VIEWPORT_HEIGHT - 75, Constants.VIEWPORT_WIDTH, 50, Color.WHITE);
        left.draw();
        right.draw();

        int x = Constants.VIEWPORT_WIDTH / 5;
        int y = Constants.VIEWPORT_HEIGHT / 4;
        for (int i = 0; i < shop.length; i++) {
            int xPos;
            int yPos;
            if (i < 3) {
                xPos = x * (i + 1) - 15 + 15 * i;
                yPos = Constants.VIEWPORT_HEIGHT - (x - y - 20);
            } else {
                xPos = x  * (i - 2) - 15 + 15 * (i - 3);
                yPos = Constants.VIEWPORT_HEIGHT - (x + y * 2 + 20);
            }

            shop[i].draw();

            int skinIndex = i + (page - 1) * shop.length;
            String skin = skinIndex < shopItems.size() ? shopItems.get(skinIndex) : "";
            if (SkinHandler.unlockedSkins.containsKey(skin)) {
                if (!SkinHandler.unlockedSkins.get(skin)) {
                    Renderer.get().drawString(String.valueOf(SkinHandler.skinPrices.get(skin)), xPos, yPos);
                } else if (Objects.equals(Config.playerSkin, skin) || Objects.equals(Config.backgroundSkin, skin)) {
                    Renderer.get().drawString("Equipped!", xPos, yPos);
                } else {
                    Renderer.get().drawString("Bought!", xPos, yPos);
                }
            }
        }

        // IMPORTANT: if this is removed, nothing will be drawn to the screen
        renderer.render();
    }

    private void setSkin(String skin) {
        if (skin.startsWith("textures/players")) Player.get().setTexture(skin);
        else if (skin.startsWith("textures/backgrounds")) Background.get().setTexture(skin);


        if (skin.startsWith("textures/players/")) {
            Config.playerSkin = skin;
        } else {
            Config.backgroundSkin = skin;
        }

        DodgeCoin.logEvent("Changed skin to " + skin);
    }

    private void updateButtonUI() {
        for (int i = 0; i < shop.length; i++) {
            // textures
            int index = i + (page - 1) * 6;
            if (index < shopItems.size()) {
                shop[i].setTexture(shopItems.get(index));
                shop[i].setEnabled(true);
            } else {
                shop[i].setTexture("");
                shop[i].setEnabled(false);
            }

            // borders

        }
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
