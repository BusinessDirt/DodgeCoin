package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.SkinHandler;
import businessdirt.dodgecoin.core.util.Util;
import businessdirt.dodgecoin.game.objects.Background;
import businessdirt.dodgecoin.game.objects.Player;
import com.badlogic.gdx.Gdx;
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
        super(DodgeCoin.assets.getSkin("skins/ui/skin.json"), Color.TEAL);
    }

    /**
     * Creates all the shop-ui elements.
     */
    @Override
    public void show() {
        // sizing for the containers to scale with lower resolutions
        float containerWidth = Gdx.graphics.getWidth() / 1.263f;
        float containerHeight = Gdx.graphics.getHeight() / 1.42f;
        float padX = (Gdx.graphics.getWidth() - containerWidth) / 2;
        float padY = (Gdx.graphics.getHeight() - containerHeight) / 2;
        float itemWidth = Gdx.graphics.getWidth() / 5.5f;

        // Shop Label
        Label shopLabel = new Label("Shop", skin);
        shopLabel.setAlignment(Align.right);
        shopLabel.setBounds(padX, Gdx.graphics.getHeight() - padY, Gdx.graphics.getWidth() - 2f * padX, padY);
        shopLabel.setFontScale(Gdx.graphics.getHeight() / 540f);
        this.stage.addActor(shopLabel);


        // Money Label
        Label label = new Label("Money: " + Math.round(Config.money * 100.0f) / 100.0f + "$", skin);
        label.setBounds(padX, Gdx.graphics.getHeight() - padY, Gdx.graphics.getWidth() - 2f * padX, padY);
        label.setAlignment(Align.left);
        label.setFontScale(Gdx.graphics.getHeight() / 540f);
        this.stage.addActor(label);

        // Back Button
        TextButton backButton = new TextButton("Back", skin.get("backButton", TextButton.TextButtonStyle.class));
        backButton.setBounds(Gdx.graphics.getWidth() / 2f - itemWidth / 2f, padY / 2f - itemWidth / 7f, itemWidth, itemWidth / 3.5f);
        backButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 540f);
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
        container.setBounds(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // table for all the shop items
        Table table = new Table();
        table.align(Align.top);
        createShopButtons(table, containerWidth, containerHeight);

        // scroll pane for scrolling
        this.pane = new ScrollPane(table, skin.get("background", ScrollPane.ScrollPaneStyle.class));
        this.pane.setSmoothScrolling(true);
        this.pane.setScrollingDisabled(false, true);
        this.pane.setFadeScrollBars(true);
        this.pane.layout();

        // add the scroll pane to the container
        container.add(this.pane).pad(padY, padX, padY, padX).width(containerWidth).height(containerHeight);
        this.stage.addActor(container);
        this.stage.setScrollFocus(this.pane);
    }

    /**
     * Creates a button for every shop item.
     * @param table the table all shop items are added to
     * @param width the width of the item
     * @param height the height of the item
     */
    private void createShopButtons(Table table, float width, float height) {
        List<String> shopItems = DodgeCoin.assets.getShopItems();
        int lowerCount = shopItems.size() >> 1;
        int upperCount = shopItems.size() - lowerCount;

        // create upper row
        for (int i = 0; i < upperCount; i++) {
            ShopItem item = new ShopItem(skin, shopItems.get(i), width, (height - 35) / 2f);
            float padRight = i == shopItems.size() - 1 ? 5f : 0f;
            table.add(item.getGroup()).width(item.width).height(item.height).pad(5f, 5f, 5f, padRight);
        }

        // swap to the next row
        table.row();

        // create lower row
        for (int i = upperCount; i < shopItems.size(); i++) {
            ShopItem item = new ShopItem(skin, shopItems.get(i), width, (height - 35) / 2f);
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

        // all shop items
        public static final List<ShopItem> items = new ArrayList<>();

        private final Group group;
        private final Label name;
        private final String texturePath;
        private final Skin skin;

        private final float width;
        private final float height;

        public ShopItem(Skin skin, String texture, float width, float height) {
            // scale of the entire ui
            float scale = Gdx.graphics.getHeight() / 1080f;

            // ui skin
            this.skin = skin;

            // game skin that is going to be displayed
            this.texturePath = texture;
            TextureRegion region = new TextureRegion(DodgeCoin.assets.getTexture(texture));

            // set the region to the default frame if it is a player
            // if this would not be done it would show all the animation frames next to each other
            if (texture.contains("players")) {
                float tw = region.getTexture().getWidth();
                float th = region.getTexture().getHeight();
                region.setRegion(0, 0, (int) (tw / 3.0f), (int) th);
            }

            // size of a singular shop item
            this.width = (width) / 3f - 9;
            this.height = height;

            // group containing all ui elements
            this.group = new Group();
            this.group.setSize(this.width, this.height);

            // this button acts as a background and for click detection
            Button button = new Button(skin.get("default", Button.ButtonStyle.class));
            button.setTransform(true);
            button.setTouchable(Touchable.enabled);
            button.setSize(this.width, height);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // play sound
                    DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);

                    // get the skin price
                    int price = 0;
                    if (SkinHandler.skinPrices.containsKey(texture)) price = SkinHandler.skinPrices.get(texture);

                    // check if the skin is unlocked
                    // or if the player has enough money to buy the skin
                    if (Config.money >= price && !SkinHandler.unlockedSkins.get(texture)) {
                        // set the skin to the one that was clicked (i.e. this shop item)
                        setSkin(texture);

                        // remove the money from the players balance and save it
                        // also saves the current skin
                        Config.money -= price;
                        Config.getConfig().markDirty();
                        Config.getConfig().writeData();

                        // set the unlocked state of the skin to true and save it
                        SkinHandler.unlockedSkins.put(texture, true);
                        SkinHandler.save();

                    } else if (SkinHandler.unlockedSkins.get(texture)) { // the skin is already bought
                        // set the skin to the one that was clicked (i.e. this shop item)
                        setSkin(texture);

                        // saves the current skin
                        Config.getConfig().markDirty();
                        Config.getConfig().writeData();
                    } else Util.logEvent("Could not equip / buy the skin");
                    updateUI();
                }
            });

            // Get the name of the skin from the relative path
            String[] skinName = texture.split("/");
            String skinNameNoExtension = skinName[skinName.length - 1].replaceAll("[.][a-z]*", "");

            // name label
            this.name = new Label(Util.title(skinNameNoExtension), skin.get("background", Label.LabelStyle.class));
            this.name.setAlignment(Align.center, Align.bottom);
            this.name.setTouchable(Touchable.disabled);
            this.name.setFontScale(scale);
            this.name.setBounds(75f * scale, this.height - 75f * scale, this.width - 150f * scale, 50f * scale);

            // skin price
            int skinPrice = SkinHandler.skinPrices.get(texture);

            // price label
            Label price = new Label(NumberFormat.getInstance(Locale.getDefault()).format(skinPrice), skin.get("priceWhite", Label.LabelStyle.class));
            price.setAlignment(Align.center, Align.bottom);
            price.setTouchable(Touchable.disabled);
            price.setFontScale(Gdx.graphics.getHeight() / 1080f);
            price.setBounds(25f * scale, 25f * scale, this.width - 50f * scale, 50f * scale);

            // a preview of the skin scaled down
            Image image = new Image(region);
            image.setTouchable(Touchable.disabled);

            // scaling
            float widthScaled = image.getWidth() * ((this.height - 170f * scale) / image.getHeight());
            image.setSize(widthScaled, this.height - 170f * scale);
            image.setPosition((this.width - image.getWidth()) / 2, 85f * scale);

            this.group.addActor(button);
            this.group.addActor(image);
            this.group.addActor(price);
            this.group.addActor(this.name);

            items.add(this);
            updateUI();
        }

        /**
         * Updates the color of the price labels.
         * green -> unlocked & equipped
         * orange -> unlocked & unequipped
         * red -> not unlocked
         */
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

        /**
         * Sets either the player or the background skin.
         * @param skin the relative path to the skin
         */
        private void setSkin(String skin) {
            // detect whether it is a player or a background skin
            if (skin.startsWith("textures/players")) {
                Player.get().setTexture(skin);
                Config.playerSkin = skin;
            } else if (skin.startsWith("textures/backgrounds")) {
                Background.get().setTexture(skin);
                Config.backgroundSkin = skin;
            }

            // log the event
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
