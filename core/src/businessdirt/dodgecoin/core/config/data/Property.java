package businessdirt.dodgecoin.core.config.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    PropertyType type();

    String name();
    String description() default "";

    String category();
    String subcategory() default "";

    int min() default 0;
    int max() default 0;

    String[] options() default {};

    boolean hidden() default false;
}
