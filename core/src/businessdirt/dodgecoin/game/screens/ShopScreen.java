package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.SkinHandler;
import businessdirt.dodgecoin.core.util.Util;
import businessdirt.dodgecoin.game.objects.Background;
import businessdirt.dodgecoin.game.objects.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShopScreen extends AbstractScreen {

    private ScrollPane pane;

    protected ShopScreen() {
        super(DodgeCoin.assets.getSkin("skins/shop/skin.json"), Color.TEAL);
    }

    @Override
    public void show() {
        // Shop Label
        Label shopLabel = new Label("Shop", skin);
        shopLabel.setAlignment(Align.center);
        shopLabel.setBounds(0, 940f, 1920f, 140f);
        shopLabel.setFontScale(2f);
        this.stage.addActor(shopLabel);

        // Back Button
        TextButton backButton = new TextButton("Back", skin.get("backButton", TextButton.TextButtonStyle.class));
        backButton.setBounds(960f - 175f, 40f, 350f, 100f);
        backButton.getLabel().setFontScale(2f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new MenuScreen());
            }
        });
        this.stage.addActor(backButton);

        // container for the scroll pane
        Table container = new Table();
        container.setBounds(0f, 0f, 1920f, 1080f);

        // table for all the shop items
        Table table = new Table();
        table.align(Align.top);
        createShopButtons(table, 760f);

        // scroll pane for scrolling
        this.pane = new ScrollPane(table, skin.get("background", ScrollPane.ScrollPaneStyle.class));
        this.pane.setSmoothScrolling(true);
        this.pane.setScrollingDisabled(false, true);
        this.pane.setFadeScrollBars(true);
        this.pane.layout();

        // add the scroll pane to the container
        container.add(this.pane).pad(140f, 200f, 180f, 200f).width(1520f).height(760f);
        this.stage.addActor(container);
        this.stage.setScrollFocus(this.pane);
    }

    private void createShopButtons(Table table, float height) {
        List<String> shopItems = DodgeCoin.assets.getShopItems();
        int lowerCount = shopItems.size() >> 1;
        int upperCount = shopItems.size() - lowerCount;

        for (int i = 0; i < upperCount; i++) {
            ShopItem item = new ShopItem(skin, shopItems.get(i), (height - 35) / 2f);
            float padRight = i == shopItems.size() - 1 ? 5f : 0f;
            table.add(item.getGroup()).width(item.width).height(item.height).pad(5f, 5f, 5f, padRight);
        }

        table.row();
        for (int i = upperCount; i < shopItems.size(); i++) {
            ShopItem item = new ShopItem(skin, shopItems.get(i), (height - 35) / 2f);
            float padRight = i == shopItems.size() - 1 ? 5f : 0f;
            table.add(item.getGroup()).width(item.width).height(item.height).pad(0f, 5f, 5f, padRight);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        this.pane.setScrollbarsVisible(true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        super.dispose();
        this.pane.getActor().clear();
        ShopItem.items.clear();
    }

    private static class ShopItem {

        public static final List<ShopItem> items = new ArrayList<>();

        private final Group group;
        private final Label name;
        private final String texturePath;
        private final Skin skin;

        private final float width;
        private final float height;

        public ShopItem(Skin skin, String texture, float height) {
            this.skin = skin;
            this.texturePath = texture;
            TextureRegion region = new TextureRegion(DodgeCoin.assets.getTexture(texture));
            if (texture.contains("players")) {
                region.setRegion(0, 0, 16, 32);
            }

            this.width = (1520f) / 3f - 9;
            this.height = height;

            this.group = new Group();
            this.group.setSize(this.width, height);

            Button button = new Button(skin.get("default", Button.ButtonStyle.class));
            button.setTransform(true);
            button.setTouchable(Touchable.enabled);
            button.setSize(width, height);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                    int price = 0;
                    if (SkinHandler.skinPrices.containsKey(texture)) price = SkinHandler.skinPrices.get(texture);

                    if (Config.money >= price && !SkinHandler.unlockedSkins.get(texture)) {
                        setSkin(texture);
                        Config.money -= price;
                        Config.getConfig().markDirty();
                        Config.getConfig().writeData();
                        SkinHandler.unlockedSkins.put(texture, true);
                        SkinHandler.save();
                    } else if (SkinHandler.unlockedSkins.get(texture)) {
                        setSkin(texture);
                        Config.getConfig().markDirty();
                        Config.getConfig().writeData();
                    } else Util.logEvent("Could not equip / buy the skin");
                    updateUI();
                }
            });

            String[] skinName = texture.split("/");
            String skinNameNoExtension = skinName[skinName.length - 1].replaceAll("[.][a-z]*", "");
            this.name = new Label(Util.title(skinNameNoExtension), skin.get("background", Label.LabelStyle.class));
            this.name.setAlignment(Align.center, Align.bottom);
            this.name.setTouchable(Touchable.disabled);
            this.name.setBounds(75f, this.height - 75f, this.width - 150f, 50f);

            int skinPrice = SkinHandler.skinPrices.get(texture);
            Label price = new Label(NumberFormat.getInstance(Locale.getDefault()).format(skinPrice), skin.get("priceWhite", Label.LabelStyle.class));
            price.setAlignment(Align.center, Align.bottom);
            price.setTouchable(Touchable.disabled);
            price.setBounds(25f, 25f, this.width - 50f, 50f);

            Image image = new Image(region);
            image.setTouchable(Touchable.disabled);
            float widthScaled = image.getWidth() * ((this.height - 170f) / image.getHeight());
            image.setSize(widthScaled, this.height - 170f);
            image.setPosition((this.width - image.getWidth()) / 2, 85f);

            this.group.addActor(button);
            this.group.addActor(image);
            this.group.addActor(price);
            this.group.addActor(this.name);

            items.add(this);
            updateUI();
        }

        private static void updateUI() {
            for (ShopItem item : items) {
                if (Objects.equals(Config.playerSkin, item.texturePath) || Objects.equals(Config.backgroundSkin, item.texturePath)) {
                    item.name.setStyle(item.skin.get("priceGreen", Label.LabelStyle.class));
                } else if (SkinHandler.unlockedSkins.get(item.texturePath)) {
                    item.name.setStyle(item.skin.get("priceOrange", Label.LabelStyle.class));
                } else {
                    item.name.setStyle(item.skin.get("priceRed", Label.LabelStyle.class));
                }
            }
        }

        private void setSkin(String skin) {
            if (skin.startsWith("textures/players")) Player.get().setTexture(skin);
            else if (skin.startsWith("textures/backgrounds")) Background.get().setTexture(skin);
            if (skin.startsWith("textures/players/")) {
                Config.playerSkin = skin;
            } else {
                Config.backgroundSkin = skin;
            }
            Util.logEvent("Changed skin to " + skin);
        }

        public Group getGroup() {
            return group;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }
    }
}
