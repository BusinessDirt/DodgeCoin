package businessdirt.dodgecoin.core.config.data.types;

import businessdirt.dodgecoin.core.config.data.PropertyData;
import com.badlogic.gdx.graphics.Color;
import com.google.common.primitives.Floats;

import java.util.Arrays;
import java.util.List;

public class Colors {

    public static void read(Object configObject, PropertyData propertyData) {
        if (configObject == null) {
            propertyData.setValue(propertyData.getAsColor());
        } else {
            float[] color = Floats.toArray((List<Double>) configObject) ;
            propertyData.setValue(new Color(color[0], color[1], color[2], color[3]));
        }
    }

    public static Object write(PropertyData propertyData) {
        return Arrays.asList(propertyData.getAsColor().r, propertyData.getAsColor().g, propertyData.getAsColor().b, propertyData.getAsColor().a);
    }
}
