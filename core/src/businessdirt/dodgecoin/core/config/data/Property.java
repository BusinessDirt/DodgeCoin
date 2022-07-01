package businessdirt.dodgecoin.core.config.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    // type used for the GUI
    PropertyType type();

    String name();
    String description() default "";

    String category();
    String subcategory() default "";

    // range of numbers for sliders
    int min() default 0;
    int max() default 0;

    // options for selectors
    String[] options() default {};

    // hides the property from the GUI
    boolean hidden() default false;
}
