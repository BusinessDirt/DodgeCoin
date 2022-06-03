package businessdirt.dodgecoin.core.config.data;

import businessdirt.dodgecoin.core.config.ConfigHandler;

import java.lang.reflect.Field;
import java.util.Objects;

public class PropertyData {

    private final ConfigHandler instance;
    private final Property property;
    private final PropertyValue value;

    public PropertyData(Property property, PropertyValue value, ConfigHandler instance) {
        this.property = property;
        this.value = value;
        this.instance = instance;
    }

    public Object getValue(Object object) {
        return this.getValue().getValue(this.getInstance());
    }

    public Object getAsAny() {
        return this.value.getValue(this.instance);
    }

    public boolean getAsBoolean() {
        return (Boolean) this.getValue().getValue(this.getInstance());
    }

    public String getAsString() {
        return (String) this.getValue().getValue(this.getInstance());
    }

    public double getAsDouble() {
        return (Double) this.getValue().getValue(this.getInstance());
    }

    public double getAsInt() {
        return (Integer) this.getValue().getValue(this.getInstance());
    }

    public Object getAs(Class<?> c) {
        return c.cast(this.getAsAny());
    }

    public void setValue(Object value) {
        this.value.setValue(value, this.getInstance());
        this.instance.markDirty();
    }

    public Property getProperty() {
        return this.property;
    }

    public PropertyValue getValue() {
        return this.value;
    }

    public ConfigHandler getInstance() {
        return this.instance;
    }

    public static PropertyData fromField(Property property, Field field, ConfigHandler instance) {
        return new PropertyData(property, new PropertyValue(field), instance);
    }

    public PropertyData copy(Property property, PropertyValue value, ConfigHandler instance) {
        return new PropertyData(property, value, instance);
    }

    public String toString() {
        return "PropertyData(property=" + this.property + ", value=" + this.value + ", instance=" + this.instance + ")";
    }

    public int hashCode() {
        int hash = (this.property != null ? this.property.hashCode() : 0) * 31;
        hash = (hash + (this.value != null ? this.value.hashCode() : 0)) * 31;
        return hash + (this.instance != null ? this.instance.hashCode() : 0);
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object instanceof PropertyData) {
            PropertyData propertyData = (PropertyData) object;
            return Objects.equals(this.property, propertyData.property) && Objects.equals(this.value, propertyData.value) && Objects.equals(this.instance, propertyData.instance);
        }

        return false;
    }
}
