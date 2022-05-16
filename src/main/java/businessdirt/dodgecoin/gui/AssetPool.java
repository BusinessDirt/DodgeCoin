package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.FileHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<Integer, BufferedImage> images = new HashMap<>();

    public static BufferedImage getImage(String resourceName) throws IOException {
        BufferedImage image = FileHandler.get().getImageFromResource(resourceName);
        if (AssetPool.images.containsKey(image.hashCode())) {
            return AssetPool.images.get(image.hashCode());
        } else {
            AssetPool.images.put(image.hashCode(), image);
            return image;
        }
    }

    public static BufferedImage getImage(Integer hashCode) {
        if (AssetPool.images.containsKey(hashCode)) return AssetPool.images.get(hashCode);
        return null;
    }
}
