package businessdirt.dodgecoin.game;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class AssetFinder {

    private AssetManager assetManager;
    private String path;

    public class AssetDescriptor {
        public String folder;
        public Class<?> assetType;

        public AssetDescriptor(String folder, Class<?> assetType) {
            this.folder = folder;
            this.assetType = assetType;
        }
    }

    private Array<AssetDescriptor> assets = new Array<AssetDescriptor>();

    public AssetFinder(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.path = Gdx.files.getLocalStoragePath() + "assets\\";

        assets.add(new AssetDescriptor("textures/players/", Texture.class));
        assets.add(new AssetDescriptor("textures/backgrounds/", Texture.class));
        assets.add(new AssetDescriptor("textures/gui/", Texture.class));
        assets.add(new AssetDescriptor("textures/coins/", Texture.class));
    }

    public void load() {
        for (AssetDescriptor descriptor : assets) {
            FileHandle folder = Gdx.files.getFileHandle(path, Files.FileType.Internal).child(descriptor.folder);
            if (!folder.exists()) {
                System.out.println("Folder does not exist: " + folder.path());
                return;
            }

            for (FileHandle asset : folder.list()) {
                logFileLoaded(asset.path());
                assetManager.load(asset.path(), descriptor.assetType);
            }
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
