package businessdirt.dodgecoin.core.config.gui;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.data.Category;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.gui.components.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

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
        this.skin = DodgeCoin.assets.getSkin("skins/settings/skin.json");

        List<Category> categories = Config.getConfig().getCategories();
        this.currentCategory = 0;

        for (int i = 0; i < categories.size(); i++) {
            int finalI = i;

            Label label = new Label(categories.get(i).getName(), skin);
            label.setWrap(true);
            label.setTouchable(Touchable.disabled);
            label.setWidth(categoryWidth - 60f);
            label.setFontScale(DodgeCoin.fullscreen.width / 1745.45f);
            label.setAlignment(Align.center, Align.bottom);
            label.layout();

            float h = label.getGlyphLayout().height + 50f;
            label.setPosition(35f, h / 2 - 8f);

            Group group = new Group();
            group.setSize(categoryWidth - 10f, h);

            Button background = new Button(this.skin.get("category", Button.ButtonStyle.class));
            background.setDisabled(false);
            background.setSize(categoryWidth - 10f, h);
            background.setPosition(5f, 0f);
            background.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    searchQuery = "";
                    currentCategory = finalI;
                    setProperties(propertyTable, propertyWidth);
                    DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                }
            });

            group.addActor(background);
            group.addActor(label);

            float padTop = i == 0 ? 5f : 0f;
            categoryTable.add(group).width(categoryWidth).height(h).pad(padTop, 5f, 5f, 5f);
            categoryTable.row();
        }

        if (categories.size() > 0) setProperties(propertyTable, propertyWidth);
    }

    private void setProperties(Table propertyTable, float propertyWidth) {
        propertyTable.clear();
        boolean first = true;
        String previousSubcategory = "";

        List<PropertyData> filteredProperties;
        if (searchQuery.equals("")) {
            filteredProperties = Config.getConfig().getCategories().get(this.currentCategory).getItems();
        } else {
            filteredProperties = Config.getConfig().getProperties().stream().filter(data -> {
                if (Objects.equals(this.searchQuery, "")) return true;
                return data.getProperty().name().contains(this.searchQuery) || data.getProperty().description().contains(this.searchQuery) ||
                        data.getProperty().category().contains(this.searchQuery) || data.getProperty().subcategory().contains(this.searchQuery);
            }).collect(Collectors.toList());
        }

        for (PropertyData property : filteredProperties) {
            if (!property.getProperty().hidden()) {

                if ((previousSubcategory.equals("") && !Objects.equals(property.getProperty().subcategory(), ""))
                        || !previousSubcategory.equals(property.getProperty().subcategory())) {

                    float padTop = first ? 5f : 0f;
                    propertyTable.add(new SubcategoryComponent(property.getProperty().subcategory(), skin, padTop, propertyWidth - 30f).getActor());
                    propertyTable.row();

                    previousSubcategory = property.getProperty().subcategory();
                    first = false;
                }

                Label name = new Label(property.getProperty().name(), skin);
                name.setTouchable(Touchable.disabled);
                name.setFontScale(DodgeCoin.fullscreen.height / 720f);
                name.layout();

                Label desc = new Label(property.getProperty().description(), skin);
                desc.setTouchable(Touchable.disabled);
                desc.setWrap(true);
                desc.setWidth(propertyWidth * 0.7f);
                desc.setFontScale(DodgeCoin.fullscreen.width / 1745.45f);
                desc.layout();

                float h = 50f + name.getGlyphLayout().height * 3 + desc.getGlyphLayout().height;

                name.setPosition(35f, h - 25f - name.getGlyphLayout().height);
                desc.setPosition(45f, 25f + desc.getGlyphLayout().height / 2f);

                Group group = new Group();
                group.setSize(propertyWidth - 30f, h);

                Button background = new Button(skin.get("blank", Button.ButtonStyle.class));
                background.setDisabled(false);
                background.setSize(propertyWidth - 30f, h);

                group.addActor(background);
                group.addActor(name);
                group.addActor(desc);

                switch (property.getProperty().type()) {
                    case SWITCH:
                        group.addActor(new SwitchComponent(property, skin, propertyWidth - 30f, h).getActor());
                        break;
                    case SLIDER:
                        SliderComponent sliderComponent = new SliderComponent(property, skin, propertyWidth - 30f, h);
                        group.addActor(sliderComponent.getActor());
                        group.addActor(sliderComponent.getLabel());
                        break;
                    case NUMBER:
                        group.addActor(new NumberComponent(property, skin, propertyWidth - 30f, h).getActor());
                        break;
                    case SELECTOR:
                        group.addActor(new SelectorComponent(property, skin, propertyWidth - 30f, h).getActor());
                        break;
                    case TEXT:
                        group.addActor(new TextComponent(property, skin, propertyWidth - 30f, h).getActor());
                    case PARAGRAPH:
                        group.addActor(new ParagraphComponent(property, skin, propertyWidth - 30f, h).getActor());
                        break;
                    case COLOR:
                        group.addActor(new ColorComponent(property, skin, propertyWidth - 30f, h).getActor());
                        break;
                }

                float padTop = first ? 5f : 0f;
                propertyTable.add(group).pad(padTop, 5f, 5f, 5f).top().align(Align.right);
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
