package businessdirt.dodgecoin.core.config.data;

import java.util.List;

public class Category {

    private final String name;
    private final List<PropertyData> items;

    public Category(String name, List<PropertyData> items) {
        this.name = name;
        this.items = items;
    }

    public String toString() {
        return "Category \"" + this.name + "\"\n" + String.join("\n", (CharSequence) this.items);
    }

    public final List<PropertyData> getItems() {
        return this.items;
    }
}
