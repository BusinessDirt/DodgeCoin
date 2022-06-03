package businessdirt.dodgecoin.core.config;

import businessdirt.dodgecoin.core.config.data.Category;
import businessdirt.dodgecoin.core.config.data.Property;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigHandler {

    private final List<PropertyData> properties;
    private final FileConfig configFile;
    private boolean dirty;

    public ConfigHandler(File file) {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Field> filteredFields = new ArrayList<>();

        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Property.class)) {
                filteredFields.add(declaredField);
            }
        }

        List<PropertyData> dataList = new ArrayList<>();

        for (Field item : filteredFields) {
            Property property = item.getAnnotation(Property.class);
            item.setAccessible(true);

            PropertyData data = PropertyData.fromField(property, item, this);
            dataList.add(data);
        }

        this.properties = dataList;
        this.configFile = FileConfig.of(file);
    }

    public void initialize() {
        this.readData();
        new Timer("", false).scheduleAtFixedRate(new InitializationTimerTask(this), 0L, 30000L);
        Runtime.getRuntime().addShutdownHook(new Thread(ConfigHandler.this::writeData));
    }

    private void readData() {
        this.configFile.load();

        for (PropertyData property : this.properties) {
            String fullPath = ConfigHandler.fullPropertyPath(property.getProperty());
            Object configObject = this.configFile.get(fullPath);

            if (configObject == null) configObject = property.getAsAny();
            property.setValue(configObject);
        }
    }

    public void writeData() {
        if (!this.dirty) return;

        for (PropertyData property : this.properties) {
            String fullPath = ConfigHandler.fullPropertyPath(property.getProperty());
            Object propertyValue = property.getValue().getValue(property.getInstance());
            this.configFile.set(fullPath, propertyValue);
        }

        this.configFile.save();
        this.dirty = false;
    }

    public Category getCategoryItems(String category) {
        List<PropertyData> propertyDataList = this.properties.stream().filter(property -> Objects.equals(property.getProperty().category(), category)).collect(Collectors.toList());
        return new Category(category, propertyDataList);
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
}
