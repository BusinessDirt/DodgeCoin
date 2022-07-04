//erstellt von Maxi Bollschweiler
package businessdirt.dodgecoin.core.config.data;

/**
 * Defines the Type of Actor that will be displayed in the Settings Screen <br>
 * The following {@link PropertyType}s can be paired with the following Data Types <br>
 *      {@code SWITCH} -> {@link Boolean} <br>
 *      {@code TEXT} -> {@link String} <br>
 *      {@code PARAGRAPH} -> {@link String} <br>
 *      {@code NUMBER} -> {@link Double} <br>
 *      {@code SLIDER} -> {@link Integer} <br>
 *      {@code SELECTOR} -> {@link String} <br>
 *      {@code COLOR} -> {@link com.badlogic.gdx.graphics.Color} <br>
 *      {@code KEY} -> {@link businessdirt.dodgecoin.core.config.data.types.Key} <br>
 */
public enum PropertyType {
    SWITCH, TEXT, PARAGRAPH, SLIDER, NUMBER, COLOR, SELECTOR, KEY;
}
