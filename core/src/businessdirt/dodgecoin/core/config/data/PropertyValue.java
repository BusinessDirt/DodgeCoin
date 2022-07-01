package businessdirt.dodgecoin.core.config.data;

import businessdirt.dodgecoin.core.config.ConfigHandler;

import java.lang.reflect.Field;

/**
 * Manages the field of the property
 */
public class PropertyValue {

    private final Field field;

    public PropertyValue(Field field) {
        this.field = field;
    }

    public Object getValue(ConfigHandler instance) {
        try {
            return this.field.get(instance);
        } catch (IllegalAccessException ignored) {}
        return null;
    }

    public void setValue(Object value, ConfigHandler instance) {
        try {
            this.field.set(instance, value);
        } catch (IllegalAccessException ignored) {}
    }

    public Field getConfigHandlerField() {
        return this.field;
    }
}
