//erstellt von Maxi Bollschweiler
package businessdirt.dodgecoin.core.config.data;

import businessdirt.dodgecoin.core.config.ConfigHandler;
import businessdirt.dodgecoin.core.config.data.types.Key;
import com.badlogic.gdx.graphics.Color;

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

    public PropertyData(Property property, Field field, ConfigHandler instance) {
        this(property, new PropertyValue(field), instance);
    }

    public Object getAsAny() {
        return this.value.getValue(this.instance);
    }

    /**
     * @return the {@link PropertyValue} as a {@link Boolean}
     */
    public boolean getAsBoolean() {
        return (Boolean) this.getPropertyValue().getValue(this.getInstance());
    }

    /**
     * @return the {@link PropertyValue} as a {@link String}
     */
    public String getAsString() {
        return (String) this.getPropertyValue().getValue(this.getInstance());
    }

    /**
     * @return the {@link PropertyValue} as a {@link Double}
     */
    public double getAsDouble() {
        return (Double) this.getPropertyValue().getValue(this.getInstance());
    }

    /**
     * @return the {@link PropertyValue} as a {@link Integer}
     */
    public int getAsInt() {
        return (Integer) this.getPropertyValue().getValue(this.getInstance());
    }

    /**
     * @return the {@link PropertyValue} as a {@link Color}
     */
    public Color getAsColor() {
        return (Color) this.getPropertyValue().getValue(this.getInstance());
    }

    /**
     * @return the {@link PropertyValue} as a {@link Key}
     */
    public Key getAsKey() {
        return (Key) this.getPropertyValue().getValue(this.getInstance());
    }

    public void setValue(Object value) {
        this.value.setValue(value, this.getInstance());
        this.instance.markDirty();
    }

    public Property getProperty() {
        return this.property;
    }

    public PropertyValue getPropertyValue() {
        return this.value;
    }

    public ConfigHandler getInstance() {
        return this.instance;
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
