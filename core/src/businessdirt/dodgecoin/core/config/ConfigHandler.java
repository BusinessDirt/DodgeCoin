package businessdirt.dodgecoin.core.config;

import businessdirt.dodgecoin.core.config.data.Category;
import businessdirt.dodgecoin.core.config.data.Property;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.data.PropertyType;
import businessdirt.dodgecoin.core.config.data.types.Colors;
import businessdirt.dodgecoin.core.config.data.types.Key;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigHandler {

    // list for all defined properties
    private final List<PropertyData> properties;
    private final FileConfig configFile;

    // keeps track whether something has changed or not
    private boolean dirty;

    public ConfigHandler(File file) {
        this.configFile = FileConfig.of(file);
        this.properties = new ArrayList<>();

        // get the declared properties
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Field> filteredFields = Arrays.stream(declaredFields).filter(field -> field.isAnnotationPresent(Property.class)).collect(Collectors.toList());

        // loop through all properties
        // set the field accessible
        // save its data as a PropertyData Object in the declared list
        for (Field field : filteredFields) {
            Property property = field.getAnnotation(Property.class);
            field.setAccessible(true);
            PropertyData propertyData = new PropertyData(property, field, this);
            this.properties.add(propertyData);
        }
    }

    /**
     * Reads the data from the config file.
     * Starts a timer task that saves the config every 30 seconds.
     * Adds a shutdown-hook that will save the config when the application closes.
     */
    public void initialize() {
        this.readData();
        new Timer("", false).scheduleAtFixedRate(new WriteDataTimerTask(this), 0L, 30000L);
        Runtime.getRuntime().addShutdownHook(new Thread(ConfigHandler.this::writeData));
    }

    /**
     * @return a {@link List<Category>} containing all properties sorted in their respective Categories
     */
    public final List<Category> getCategories() {
        // filter out properties hidden from the gui
        List<PropertyData> filteredProperties = this.properties.stream().filter(data -> !data.getProperty().hidden()).collect(Collectors.toList());

        // map that contains all categories
        // this will later be converted into a list of categories
        Map<String, List<PropertyData>> categoryMap = new LinkedHashMap<>();

        for (PropertyData propertyData : filteredProperties) {
            // category name
            String category = propertyData.getProperty().category();

            // list of properties that have already been assigned to this category
            List<PropertyData> dataList = categoryMap.get(category);
            // if there are no items in this category yet the list needs to be created
            if (dataList == null) dataList = new ArrayList<>();
            // add the property to the list
            dataList.add(propertyData);

            // update the map
            categoryMap.put(category, dataList);
        }

        // convert the map to a list of categories and return it
        List<Category> result = new ArrayList<>(categoryMap.size());
        categoryMap.forEach((key, value) -> {
            value.sort(new SubcategoryComparator());
            result.add(new Category(key, value));
        });
        return result;
    }

    /**
     * Reads all values from the specified file
     */
    private void readData() {
        // load the file
        this.configFile.load();

        for (PropertyData propertyData : this.properties) {
            // path and value of the property
            String propertyPath = ConfigHandler.fullPropertyPath(propertyData.getProperty());
            Object configObject = this.configFile.get(propertyPath);

            // library can't handle every data type
            // -> need to handle those differently
            if (propertyData.getProperty().type() == PropertyType.KEY) {
                Key.read(configObject, propertyData);
            } else if (propertyData.getProperty().type() == PropertyType.COLOR) {
                Colors.read(configObject, propertyData);
            } else {
                // get the default value if the value in the file is null
                if (configObject == null) configObject = propertyData.getAsAny();
                // set the property's value
                propertyData.setValue(configObject);
            }
        }
    }

    /**
     * Writes all values to the config file.
     */
    public void writeData() {
        // only write to the file if something has changed
        if (!this.dirty) return;

        for (PropertyData propertyData : this.properties) {
            // path and value of the property
            String propertyPath = ConfigHandler.fullPropertyPath(propertyData.getProperty());
            Object propertyValue = propertyData.getPropertyValue().getValue(propertyData.getInstance());

            // library can't handle every data type
            // -> need to handle those differently
            if (propertyData.getProperty().type() == PropertyType.KEY) {
                propertyValue = Key.write(propertyData);
            } else if (propertyData.getProperty().type() == PropertyType.COLOR) {
                propertyValue = Colors.write(propertyData);
            }

            // write the value to the file
            this.configFile.set(propertyPath, propertyValue);
        }

        // save the file
        this.configFile.save();

        // config values cannot be different from the ones in the file
        // because we just saved it
        this.dirty = false;
    }

    /**
     * Generates the path to the given {@code property}.
     * It will look like this: 'category'.'subcategory'.'name'
     * If the subcategory is null it will look like this: 'category'.'name'
     * @param property {@link Property}
     * @return the path of the {@code property}
     */
    private static String fullPropertyPath(Property property) {
        StringBuilder bobTheBuilder = new StringBuilder();
        bobTheBuilder.append(property.category()).append(".");

        if (!Objects.equals(property.subcategory(), "")) {
            bobTheBuilder.append(property.subcategory()).append(".");
        }

        bobTheBuilder.append(property.name());
        return bobTheBuilder.toString();
    }

    public final void markDirty() {
        this.dirty = true;
    }

    public final List<PropertyData> getProperties() {
        return this.properties;
    }

    /**
     * Saves the properties to the file
     */
    private static class WriteDataTimerTask extends TimerTask {

        private final ConfigHandler instance;

        public WriteDataTimerTask(ConfigHandler instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            this.instance.writeData();
        }
    }

    /**
     * Compares two subcategories
     */
    private static class SubcategoryComparator implements Comparator<PropertyData> {

        @Override
        public int compare(PropertyData o1, PropertyData o2) {
            return o1.getProperty().subcategory().compareTo(o2.getProperty().subcategory());
        }
    }
}
