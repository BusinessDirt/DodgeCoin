package businessdirt.dodgecoin.core.config.gui;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.config.data.Category;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.gui.components.*;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.gui.components.TextComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SettingsGui {

    private ScrollPane pane;
    private static SettingsGui instance;
    private final Skin skin;
    private int currentCategory;
    private String searchQuery;

    private SettingsGui(Table categoryTable, Table propertyTable, float categoryWidth, float propertyWidth) {
        this.searchQuery = "";
        this.skin = DodgeCoin.assets.getSkin("skins/ui/skin.json");
        float scale = DodgeCoin.fullscreen.height / 1080f;

        List<Category> categories = Config.getConfig().getCategories();
        this.currentCategory = 0;

        for (int i = 0; i < categories.size(); i++) {
            int finalI = i;

            Label label = new Label(categories.get(i).getName(), skin);
            label.setWrap(true);
            label.setTouchable(Touchable.disabled);
            label.setWidth(categoryWidth - 60f * scale);
            label.setFontScale(1.1f);
            label.setAlignment(Align.center, Align.bottom);
            label.layout();

            float h = label.getGlyphLayout().height + 50f * scale;
            label.setPosition(35f * scale, h / 2 - 8f * scale);

            Group group = new Group();
            group.setSize(categoryWidth - 10f * scale, h);

            Button background = new Button(this.skin.get("category", Button.ButtonStyle.class));
            background.setDisabled(false);
            background.setSize(categoryWidth - 10f * scale, h);
            background.setPosition(5f * scale, 0f);
            background.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    searchQuery = "";
                    currentCategory = finalI;
                    setProperties(propertyTable, propertyWidth);
                }
            });

            group.addActor(background);
            group.addActor(label);

            float padTop = i == 0 ? 5f * scale : 0f;
            categoryTable.add(group).width(categoryWidth).height(h).pad(padTop, 5f * scale, 5f * scale, 5f * scale);
            categoryTable.row();
        }

        if (categories.size() > 0) setProperties(propertyTable, propertyWidth);
    }

    private void setProperties(Table propertyTable, float propertyWidth) {
        propertyTable.clear();
        boolean first = true;
        String previousSubcategory = "";
        float scale = DodgeCoin.fullscreen.height / 1080f;

        // properties filtered by the search query
        List<PropertyData> filteredProperties = Config.getConfig().getProperties().stream().filter(data -> {
            if (Objects.equals(this.searchQuery, "")) return true;
            return data.getProperty().name().contains(this.searchQuery) || data.getProperty().description().contains(this.searchQuery) ||
                    data.getProperty().category().contains(this.searchQuery) || data.getProperty().subcategory().contains(this.searchQuery);
        }).collect(Collectors.toList());

        // properties that are displayed
        // if the search query is empty the current category items will be selected
        List<PropertyData> propertyDataList = searchQuery.equals("") ? Config.getConfig().getCategories().get(this.currentCategory).getItems() : filteredProperties;

        for (PropertyData property : propertyDataList) {
            if (!property.getProperty().hidden()) {

                if ((previousSubcategory.equals("") && !Objects.equals(property.getProperty().subcategory(), ""))
                        || !previousSubcategory.equals(property.getProperty().subcategory())) {

                    float padTop = first ? 5f * scale : 0f;
                    propertyTable.add(new SubcategoryComponent(property.getProperty().subcategory(), skin, padTop, propertyWidth - 30f * scale).getActor());
                    propertyTable.row();

                    previousSubcategory = property.getProperty().subcategory();
                    first = false;
                }

                Label name = new Label(property.getProperty().name(), skin);
                name.setTouchable(Touchable.disabled);
                name.setFontScale(1.5f * scale);
                name.layout();

                Label desc = new Label(property.getProperty().description(), skin);
                desc.setTouchable(Touchable.disabled);
                desc.setWrap(true);
                desc.setWidth(900f * scale);
                desc.setFontScale(1.1f * scale);
                desc.layout();

                float h = Math.max(83f * scale + name.getGlyphLayout().height + desc.getGlyphLayout().height, 125f * scale);

                name.setPosition(35f * scale, h - 25f * scale - name.getGlyphLayout().height);
                desc.setPosition(45f * scale, 25f * scale + desc.getGlyphLayout().height / 2f);

                Group group = new Group();
                group.setSize(propertyWidth - 30f * scale, h);

                Button background = new Button(skin.get("blank", Button.ButtonStyle.class));
                background.setDisabled(false);
                background.setSize(propertyWidth - 30f * scale, h);

                group.addActor(background);
                group.addActor(name);
                group.addActor(desc);

                switch (property.getProperty().type()) {
                    case SWITCH:
                        group.addActor(new SwitchComponent(property, skin, propertyWidth - 30f * scale, h).getActor());
                        break;
                    case SLIDER:
                        SliderComponent sliderComponent = new SliderComponent(property, skin, propertyWidth - 30f * scale, h);
                        group.addActor(sliderComponent.getActor());
                        group.addActor(sliderComponent.getLabel());
                        break;
                    case NUMBER:
                        group.addActor(new NumberComponent(property, skin, propertyWidth - 30f * scale, h).getActor());
                        break;
                    case SELECTOR:
                        group.addActor(new SelectorComponent(property, skin, propertyWidth - 30f * scale, h).getActor());
                        break;
                    case TEXT:
                        group.addActor(new TextComponent(property, skin, propertyWidth - 30f * scale, h).getActor());
                        break;
                    case PARAGRAPH:
                        group.addActor(new ParagraphComponent(property, skin, propertyWidth - 30f * scale, h).getActor());
                        break;
                    case COLOR:
                        group.addActor(new ColorComponent(property, skin, propertyWidth - 30f * scale, h).getActor());
                        break;
                    case KEY:
                        group.addActor(new KeyComponent(property, skin, propertyWidth - 30f * scale, h).getActor());
                        break;
                }

                float padTop = first ? 5f * scale : 0f;
                propertyTable.add(group).pad(padTop, 5f * scale, 5f * scale, 5f * scale).top().align(Align.right);
                propertyTable.row();
                first = false;
            }
        }
    }

    public void setSearchQuery(Table propertyTable, float propertyWidth, String query) {
        this.searchQuery = query;
        this.setProperties(propertyTable, propertyWidth);
    }

    public ScrollPane getScrollPane() {
        return this.pane;
    }

    public void setScrollPane(ScrollPane pane) {
        this.pane = pane;
    }

    public static SettingsGui get() {
        return SettingsGui.instance;
    }

    public static void newInstance(Table categoryTable, Table propertyTable, float categoryWidth, float propertyWidth) {
        SettingsGui.instance = new SettingsGui(categoryTable, propertyTable, categoryWidth, propertyWidth);
    }
}
