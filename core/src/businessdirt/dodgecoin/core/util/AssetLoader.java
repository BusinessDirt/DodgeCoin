package businessdirt.dodgecoin.core.util;

import businessdirt.dodgecoin.core.SkinHandler;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssetLoader {

    private final AssetManager manager;
    private final String path;
    private final Array<AssetDescriptor<?>> assetFolders = new Array<>();
    private final List<String> shopItems = new ArrayList<>();

    public AssetLoader() {
        this.manager = new AssetManager();
        this.path = ".\\assets\\";


        assetFolders.add(new AssetDescriptor<>("textures\\", Texture.class));
        assetFolders.add(new AssetDescriptor<>("sounds\\", Sound.class));
        assetFolders.add(new AssetDescriptor<>("music\\", Music.class));
        assetFolders.add(new AssetDescriptor<>("skins\\", Skin.class));
        assetFolders.add(new AssetDescriptor<>("fonts\\", BitmapFont.class));
    }

    public <T> void load() {
        for (AssetDescriptor<?> descriptor : assetFolders) {
            FileHandle folder = Gdx.files.internal(path).child(descriptor.folder);
            if (!folder.exists()) {
                System.out.println("Directory '" + folder.path() + "' does not exist");
            } else {
                loadDirectory(folder.list(), descriptor);
            }
        }

        this.manager.finishLoading();
    }

    private <T> void loadDirectory(FileHandle[] folder, AssetDescriptor<T> descriptor) {
        for (FileHandle asset : folder) {
            if (asset.isDirectory()) {
                loadDirectory(asset.list(), descriptor);
            } else {
                if ((asset.path().endsWith(".json") && descriptor.assetType == Skin.class) || !Objects.equals(descriptor.assetType, Skin.class))
                    this.loadFile(asset, descriptor);
                Util.logFileLoaded(asset.path());
            }
        }
    }

    private <T> void loadFile(FileHandle asset, AssetDescriptor<T> descriptor) {
        if (descriptor.assetType == Skin.class) {
            manager.load(asset.path().replace(".json", ".atlas"), TextureAtlas.class);
            manager.load(asset.path(), descriptor.assetType);
        } else {
            if (descriptor.assetType == Texture.class) this.addSkin(asset);
            manager.load(asset.path(), descriptor.assetType);
        }
    }

    private <T> void addSkin(FileHandle asset) {
        String relativePath = asset.path().replaceAll(".*" + this.path.replaceAll("\\\\", "/"), "");
        if (relativePath.contains("backgrounds") || relativePath.contains("players")) {
            this.shopItems.add(relativePath);

            // unlocked skins
            if (!SkinHandler.unlockedSkins.containsKey(relativePath)) {
                SkinHandler.unlockedSkins.put(relativePath, relativePath.contains("default.png"));
                SkinHandler.save();
            }

            // skin prices
            if (!SkinHandler.skinPrices.containsKey(relativePath)) {
                int price = relativePath.contains("default.png") ? 0 : -1;
                SkinHandler.skinPrices.put(relativePath, price == -1 ? relativePath.contains("background") ? 1000000 : 10000000 : 0);
                SkinHandler.savePrices();
            }
        }
    }

    public boolean update() {
        return this.manager.update();
    }

    public synchronized <T> T get(String fileName, Class<T> type) {
        return manager.get(path.concat(fileName).replace("\\", "/"), type, true);
    }

    public synchronized Texture getTexture(String fileName) {
        return manager.get(path.concat(fileName).replace("\\", "/"), Texture.class, true);
    }

    public synchronized Sound getSound(String fileName) {
        return manager.get(path.concat(fileName).replace("\\", "/"), Sound.class, true);
    }

    public synchronized Music getMusic(String fileName) {
        return manager.get(path.concat(fileName).replace("\\", "/"), Music.class, true);
    }

    public synchronized Skin getSkin(String fileName) {
        return manager.get(path.concat(fileName).replace("\\", "/"), Skin.class, true);
    }

    public synchronized BitmapFont getBitmapFont(String fileName) {
        return manager.get(path.concat(fileName).replace("\\", "/"), BitmapFont.class, true);
    }

    public List<String> getShopItems() {
        return this.shopItems;
    }

    public static class AssetDescriptor<T> {
        public String folder;
        public Class<T> assetType;

        public AssetDescriptor(String folder, Class<T> assetType) {
            this.folder = folder;
            this.assetType = assetType;
        }
    }
}
