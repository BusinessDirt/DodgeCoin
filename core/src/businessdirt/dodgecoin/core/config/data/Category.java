package businessdirt.dodgecoin.core.config.data;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private final String name;
    private final List<PropertyData> items;

    public Category(String name, List<PropertyData> items) {
        this.name = name;
        this.items = items;
    }

    public String toString() {
        List<String> string = new ArrayList<>();
        this.items.forEach(data -> string.add(data.toString()));
        return "Category \"" + this.name + "\"\n" + String.join("\n", string);
    }

    public final List<PropertyData> getItems() {
        return this.items;
    }

    public String getName() {
        return name;
    }
}
