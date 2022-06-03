package businessdirt.dodgecoin.core.config.gui;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.gui.components.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import java.util.List;

public class SettingsGui {

    private ScrollPane pane;
    private final Skin skin;
    private static SettingsGui instance;

    private SettingsGui(Table table, float width) {

        this.skin = DodgeCoin.assets.getSkin("skins/settings/skin.json");

        List<PropertyData> propertyDataList = Config.getConfig().getProperties();
        for (int i = 0; i < propertyDataList.size(); i++) {
            PropertyData property = propertyDataList.get(i);
            if (!property.getProperty().hidden()) {
                Label name = new Label(property.getProperty().name(), skin);
                name.setTouchable(Touchable.disabled);
                name.setFontScale(1.5f);
                name.layout();

                Label desc = new Label(property.getProperty().description(), skin);
                desc.setTouchable(Touchable.disabled);
                desc.setWrap(true);
                desc.setWidth(1000f);
                desc.setFontScale(1.1f);
                desc.layout();

                float h = Math.max(83f + name.getGlyphLayout().height + desc.getGlyphLayout().height, 125f);

                name.setPosition(35f, h - 25f - name.getGlyphLayout().height);
                desc.setPosition(45f, 25f + desc.getGlyphLayout().height / 2f);

                Group group = new Group();
                group.setSize(width, h);

                Button background = new Button(skin.get("blank", Button.ButtonStyle.class));
                background.setDisabled(false);
                background.setSize(width - 20f, h);
                background.setX(10f);

                group.addActor(background);
                group.addActor(name);
                group.addActor(desc);

                switch (property.getProperty().type()) {
                    case SWITCH:
                        group.addActor(new SwitchComponent(property, skin, width, h).getActor());
                        break;
                    case SLIDER:
                        SliderComponent sliderComponent = new SliderComponent(property, skin, width, h);
                        group.addActor(sliderComponent.getActor());
                        group.addActor(sliderComponent.getLabel());
                        break;
                    case NUMBER:
                        group.addActor(new NumberComponent(property, skin, width, h).getActor());
                        break;
                    case SELECTOR:
                        group.addActor(new SelectorComponent(property, skin, width, h).getActor());
                        break;
                    case TEXT:
                        group.addActor(new TextComponent(property, skin, width, h).getActor());
                    case PARAGRAPH:
                        group.addActor(new ParagraphComponent(property, skin, width, h).getActor());
                        break;
                    case COLOR:
                        //group.addActor(new ColorComponent(property, skin, width, h).getActor());
                        break;
                }

                float padBottom = i < propertyDataList.size() - 1 ? 0f : 5f;
                table.add(group).pad(5f, 0f, padBottom, 0f).top().align(Align.right);
                table.row();
            }
        }
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

    public static void newInstance(Table table, float width) {
        SettingsGui.instance = new SettingsGui(table, width);
    }
}
