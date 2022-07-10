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
import com.badlogic.gdx.utils.Disposable;

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
        // AssetManger loads and disposes of assets automatically
        this.manager = new AssetManager();

        // path to the asset folder
        this.path = ".\\assets\\";

        // descriptors for folders to asset types to be loaded
        assetFolders.add(new AssetDescriptor<>("textures\\", Texture.class));
        assetFolders.add(new AssetDescriptor<>("sounds\\", Sound.class));
        assetFolders.add(new AssetDescriptor<>("music\\", Music.class));
        assetFolders.add(new AssetDescriptor<>("skins\\", Skin.class));
        assetFolders.add(new AssetDescriptor<>("fonts\\", BitmapFont.class));
    }

    public <T> void load() {
        // loop through all the descriptors and load them
        for (AssetDescriptor<?> descriptor : new Array.ArrayIterator<>(assetFolders)) {
            // get the folder path of the descriptor
            FileHandle folder = Gdx.files.internal(path).child(descriptor.folder);

            // load the folders contents if it exists
            if (!folder.exists()) {
                System.out.println("Directory '" + folder.path() + "' does not exist");
            } else {
                loadDirectory(folder.list(), descriptor);
            }
        }

        // actually load the assets
        this.manager.finishLoading();
    }

    private <T> void loadDirectory(FileHandle[] folder, AssetDescriptor<T> descriptor) {
        // loop through all files and folders in the parsed folder
        for (FileHandle asset : folder) {
            // if the FileHandle is a folder recursively call this method again
            if (asset.isDirectory()) {
                loadDirectory(asset.list(), descriptor);
            } else {
                // load the file
                // special treatment for skin files because there are multiple files per skin
                // and the AssetManager can't detect this
                if ((asset.path().endsWith(".json") && descriptor.assetType == Skin.class) || !Objects.equals(descriptor.assetType, Skin.class))
                    this.loadFile(asset, descriptor);

                // log that the file is loaded
                Util.logFileLoaded(asset.path());
            }
        }
    }

    private <T> void loadFile(FileHandle asset, AssetDescriptor<T> descriptor) {
        // if the file is a skin it has to be a json file
        // we also need to load its texture atlas
        if (descriptor.assetType == Skin.class) {
            // load the texture atlas
            manager.load(asset.path().replace(".json", ".atlas"), TextureAtlas.class);

            // load the json skin
            manager.load(asset.path(), descriptor.assetType);
        } else {
            // add the texture path to a list of all skins
            // this is used for the shop later on
            if (descriptor.assetType == Texture.class) this.addSkin(asset);

            // load the file
            manager.load(asset.path(), descriptor.assetType);
        }
    }

    private <T> void addSkin(FileHandle asset) {
        // convert the full path to a usable relative path
        String relativePath = asset.path().replaceAll(".*" + this.path.replaceAll("\\\\", "/"), "");

        // the path is only a skin if it is in a folder named "backgrounds" or "players"
        if (relativePath.contains("backgrounds") || relativePath.contains("players")) {
            // add the path to a list
            this.shopItems.add(relativePath);

            // save the information in the map from the SkinHandler
            // only adds it if it doesn't exist (i.e. if the game has been run before. more or less)
            if (!SkinHandler.unlockedSkins.containsKey(relativePath)) {
                SkinHandler.unlockedSkins.put(relativePath, relativePath.contains("default.png"));
                SkinHandler.save();
            }

            // save the information in the map from the SkinHandler
            // only adds it if it doesn't exist (i.e. if the game has been run before. more or less)
            // the default skin is obviously free, so we need to account for that
            if (!SkinHandler.skinPrices.containsKey(relativePath)) {
                int price = relativePath.contains("default.png") ? 0 : -1;
                SkinHandler.skinPrices.put(relativePath, price == -1 ? relativePath.contains("background") ? 1000000 : 10000000 : 0);
                SkinHandler.savePrices();
            }
        }
    }

    /** Updates the AssetManager for a single task. Returns if the current task is still being processed or there are no tasks,
     * otherwise it finishes the current task and starts the next task.
     * @return true if all loading is finished.
     */
    public boolean update() {
        return this.manager.update();
    }

    /**
     * Gets a resource that has been loaded from the AssetManager.
     * @param filePath the relative path to the file
     * @param type the class of the file (i.e. Texture.class)
     * @return an asset from the AssetManager
     */
    public synchronized <T> T get(String filePath, Class<T> type) {
        return manager.get(path.concat(filePath).replace("\\", "/"), type, true);
    }

    /**
     * Gets a texture that has been loaded from the AssetManager.
     * @param filePath the relative path to the file
     * @return a texture from the AssetManager
     */
    public synchronized Texture getTexture(String filePath) {
        return this.get(filePath, Texture.class);
    }

    /**
     * Gets a texture that has been loaded from the AssetManager.
     * @param filePath the relative path to the file
     * @return a sound from the AssetManager
     */
    public synchronized Sound getSound(String filePath) {
        return this.get(filePath, Sound.class);
    }

    /**
     * Gets a texture that has been loaded from the AssetManager.
     * @param filePath the relative path to the file
     * @return a music from the AssetManager
     */
    public synchronized Music getMusic(String filePath) {
        return this.get(filePath, Music.class);
    }

    /**
     * Gets a texture that has been loaded from the AssetManager.
     * @param filePath the relative path to the file
     * @return a skin from the AssetManager
     */
    public synchronized Skin getSkin(String filePath) {
        return this.get(filePath, Skin.class);
    }

    /**
     * Gets a texture that has been loaded from the AssetManager.
     * @param filePath the relative path to the file
     * @return a font from the AssetManager
     */
    public synchronized BitmapFont getBitmapFont(String filePath) {
        return this.get(filePath, BitmapFont.class);
    }

    public List<String> getShopItems() {
        return this.shopItems;
    }

    public static class AssetDescriptor<T> {
        // folder name and asset type
        public String folder;
        public Class<T> assetType;

        public AssetDescriptor(String folder, Class<T> assetType) {
            this.folder = folder;
            this.assetType = assetType;
        }
    }
}
