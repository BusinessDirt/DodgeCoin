package businessdirt.dodgecoin.core.config.data.types;

import businessdirt.dodgecoin.core.config.data.PropertyData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;

public class Key {

    private int primary;
    private int secondary;

    public Key(int primary, int secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public boolean pressed() {
        return Gdx.input.isKeyPressed(primary) || Gdx.input.isKeyPressed(secondary);
    }

    public boolean justPressed() {
        return Gdx.input.isKeyJustPressed(primary) || Gdx.input.isKeyJustPressed(secondary);
    }

    public Key() {
        this(Input.Keys.UNKNOWN, Input.Keys.UNKNOWN);
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public int getSecondary() {
        return secondary;
    }

    public void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public static Object write(PropertyData propertyData) {
        return Arrays.asList(propertyData.getAsKey().getPrimary(), propertyData.getAsKey().getSecondary());
    }

    public static void read(Object configObject, PropertyData propertyData) {
        if (configObject == null) {
            propertyData.setValue(propertyData.getAsKey());
        } else {
            int[] key = Ints.toArray((List<Integer>) configObject);
            propertyData.setValue(new Key(key[0], key[1]));
        }
    }
}
