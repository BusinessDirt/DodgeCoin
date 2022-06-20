package businessdirt.dodgecoin.core.config;

import businessdirt.dodgecoin.core.config.data.Category;
import businessdirt.dodgecoin.core.config.data.Property;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.data.PropertyType;
import businessdirt.dodgecoin.core.config.data.types.Colors;
import businessdirt.dodgecoin.core.config.data.types.Key;
import com.badlogic.gdx.graphics.Color;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.primitives.Floats;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigHandler {

    private final List<PropertyData> properties;
    private final FileConfig configFile;
    private boolean dirty;

    public ConfigHandler(File file) {
        this.configFile = FileConfig.of(file);
        this.properties = new ArrayList<>();

        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Field> filteredFields = Arrays.stream(declaredFields).filter(field -> field.isAnnotationPresent(Property.class)).collect(Collectors.toList());

        for (Field field : filteredFields) {
            Property property = field.getAnnotation(Property.class);
            field.setAccessible(true);
            PropertyData propertyData = PropertyData.fromField(property, field, this);
            this.properties.add(propertyData);
        }
    }

    public void initialize() {
        this.readData();
        new Timer("", false).scheduleAtFixedRate(new InitializationTimerTask(this), 0L, 30000L);
        Runtime.getRuntime().addShutdownHook(new Thread(ConfigHandler.this::writeData));
    }

    public final List<Category> getCategories() {
        List<PropertyData> filteredProperties = this.properties.stream().filter(data -> !data.getProperty().hidden()).collect(Collectors.toList());
        Map<String, List<PropertyData>> categoryMap = new LinkedHashMap<>();

        for (PropertyData propertyData : filteredProperties) {
            String category = propertyData.getProperty().category();

            List<PropertyData> dataList = categoryMap.get(category);
            if (dataList == null) dataList = new ArrayList<>();
            dataList.add(propertyData);

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

        for (PropertyData propertyData : this.properties) {
            String propertyPath = ConfigHandler.fullPropertyPath(propertyData.getProperty());
            Object configObject = this.configFile.get(propertyPath);

            if (propertyData.getProperty().type() == PropertyType.KEY) {
                Key.read(configObject, propertyData);
            } else if (propertyData.getProperty().type() == PropertyType.COLOR) {
                Colors.read(configObject, propertyData);
            } else {
                if (configObject == null) configObject = propertyData.getAsAny();
                propertyData.setValue(configObject);
            }
        }
    }

    public void writeData() {
        if (!this.dirty) return;

        for (PropertyData propertyData : this.properties) {
            String propertyPath = ConfigHandler.fullPropertyPath(propertyData.getProperty());
            Object propertyValue = propertyData.getPropertyValue().getValue(propertyData.getInstance());

            if (propertyData.getProperty().type() == PropertyType.KEY) {
                propertyValue = Key.write(propertyData);
            } else if (propertyData.getProperty().type() == PropertyType.COLOR) {
                propertyValue = Colors.write(propertyData);
            }

            this.configFile.set(propertyPath, propertyValue);
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
