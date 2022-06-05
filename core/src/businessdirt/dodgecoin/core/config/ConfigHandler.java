package businessdirt.dodgecoin.core.config;

import businessdirt.dodgecoin.core.config.data.Category;
import businessdirt.dodgecoin.core.config.data.Property;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.data.PropertyType;
import com.badlogic.gdx.graphics.Color;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.primitives.Floats;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigHandler {

    private final List<PropertyData> properties;
    private final FileConfig configFile;
    private boolean dirty;

    public ConfigHandler(File file) {
        this.configFile = FileConfig.of(file);
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Field> filteredFields = Arrays.stream(declaredFields).filter(field -> field.isAnnotationPresent(Property.class)).collect(Collectors.toList());

        this.properties = new ArrayList<>();
        for (Field item : filteredFields) {
            Property property = item.getAnnotation(Property.class);
            item.setAccessible(true);
            PropertyData data = PropertyData.fromField(property, item, this);
            this.properties.add(data);
        }
    }

    public void initialize() {
        this.readData();
        new Timer("", false).scheduleAtFixedRate(new InitializationTimerTask(this), 0L, 30000L);
        Runtime.getRuntime().addShutdownHook(new Thread(ConfigHandler.this::writeData));
    }

    public final List<Category> getCategories() {
        List<PropertyData> filtered = this.properties.stream().filter(data -> !data.getProperty().hidden()).collect(Collectors.toList());
        Map<String, List<PropertyData>> categoryMap = new LinkedHashMap<>();

        for (PropertyData item : filtered) {
            String category = item.getProperty().category();
            List<PropertyData> dataList = categoryMap.get(category);
            if (dataList == null) dataList = new ArrayList<>();
            dataList.add(item);
            categoryMap.put(category, dataList);
        }

        List<Category> result = new ArrayList<>(categoryMap.size());
        categoryMap.forEach((key, value) -> {
            value.sort(new SubcategoryComparator());
            result.add(new Category(key, value));
        });
        return result;
    }

    private void readData() {
        this.configFile.load();

        for (PropertyData property : this.properties) {
            String fullPath = ConfigHandler.fullPropertyPath(property.getProperty());
            Object configObject = this.configFile.get(fullPath);

            if (property.getProperty().type() == PropertyType.COLOR) {
                if (configObject == null) {
                    configObject = property.getAsColor().toFloatBits();
                } else {
                    float[] color = Floats.toArray((List<Double>) configObject) ;
                    property.setValue(new Color(color[0], color[1], color[2], color[3]));
                }
            } else {
                if (configObject == null) configObject = property.getAsAny();
                property.setValue(configObject);
            }
        }
    }

    public void writeData() {
        if (!this.dirty) return;

        for (PropertyData property : this.properties) {
            String fullPath = ConfigHandler.fullPropertyPath(property.getProperty());
            Object propertyValue = property.getValue().getValue(property.getInstance());

            if (property.getProperty().type() == PropertyType.COLOR)
                propertyValue = Arrays.asList(property.getAsColor().r, property.getAsColor().g, property.getAsColor().b, property.getAsColor().a);

            this.configFile.set(fullPath, propertyValue);
        }

        this.configFile.save();
        this.dirty = false;
    }

    private static String fullPropertyPath(Property fullPropertyPath) {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append(fullPropertyPath.category()).append(".");

        if (!Objects.equals(fullPropertyPath.subcategory(), "")) {
            bobTheBuilder.append(fullPropertyPath.subcategory()).append(".");
        }

        bobTheBuilder.append(fullPropertyPath.name());
        return bobTheBuilder.toString();
    }

    public final void markDirty() {
        this.dirty = true;
    }

    public final List<PropertyData> getProperties() {
        return this.properties;
    }

    private static class InitializationTimerTask extends TimerTask {

        private final ConfigHandler instance;

        public InitializationTimerTask(ConfigHandler instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            this.instance.writeData();
        }
    }

    private static class SubcategoryComparator implements Comparator<PropertyData> {

        @Override
        public int compare(PropertyData o1, PropertyData o2) {
            return o1.getProperty().subcategory().compareTo(o2.getProperty().subcategory());
        }
    }
}
