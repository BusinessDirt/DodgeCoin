package businessdirt.dodgecoin.game;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.SkinHandler;
import businessdirt.dodgecoin.game.screens.ShopScreen;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class AssetFinder {

    private final AssetManager assetManager;
    private final String path;

    public static class AssetDescriptor {
        public String folder;
        public Class<?> assetType;

        public AssetDescriptor(String folder, Class<?> assetType) {
            this.folder = folder;
            this.assetType = assetType;
        }
    }

    private final Array<AssetDescriptor> assets = new Array<>();

    public AssetFinder(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.path = Gdx.files.getLocalStoragePath() + "assets\\";

        assets.add(new AssetDescriptor("textures/players/", Texture.class));
        assets.add(new AssetDescriptor("textures/backgrounds/", Texture.class));
        assets.add(new AssetDescriptor("textures/gui/", Texture.class));
        assets.add(new AssetDescriptor("textures/coins/", Texture.class));
        assets.add(new AssetDescriptor("fonts/", BitmapFont.class));
    }

    public void load() {
        for (AssetDescriptor descriptor : assets) {
            FileHandle folder = Gdx.files.getFileHandle(path, Files.FileType.Internal).child(descriptor.folder);
            if (!folder.exists()) {
                System.out.println("Folder does not exist: " + folder.path());
                return;
            }

            if (Objects.equals(descriptor.folder, "textures/players/") ||
                Objects.equals(descriptor.folder, "textures/backgrounds/")) {
                ShopScreen.shopItems.add(descriptor.folder);
            }

            for (FileHandle asset : folder.list()) {
                logFileLoaded(asset.path());
                assetManager.load(asset.path(), descriptor.assetType);
                if (Objects.equals(descriptor.folder, "textures/players/") || Objects.equals(descriptor.folder, "textures/backgrounds/")) {
                    if (asset.name().length() > 0) {
                        SkinHandler.skinPrices.put(descriptor.folder + asset.name(), descriptor.folder.contains("players") ? 10000000 : 1000000);
                        ShopScreen.shopItems.add(descriptor.folder + asset.name());
                        SkinHandler.savePrices();
                    }
                }
            }

            SkinHandler.load();

            ShopScreen.shopItems.remove("textures/players/");
            ShopScreen.shopItems.remove("textures/backgrounds/");
        }
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    private void logFileLoaded(String path) {
        DodgeCoin.logEvent("- Loaded File - " + path);
    }

    public synchronized <T> T get(String fileName, Class<T> type) {
        return assetManager.get(path.concat(fileName).replace("\\", "/"), type, true);
    }
}
