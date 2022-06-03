package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.Config;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SelectorComponent extends GuiComponent {
    public SelectorComponent(PropertyData property, Skin skin, float width, float height) {
        this.actor = new SelectBox<String>(skin);
        @SuppressWarnings({"unchecked"})
        SelectBox<String> selectBox = (SelectBox<String>) this.actor;

        selectBox.setItems(property.getProperty().options());
        selectBox.getStyle().listStyle.selection.setTopHeight(8f);
        selectBox.getStyle().listStyle.selection.setLeftWidth(8);
        selectBox.getStyle().listStyle.selection.setBottomHeight(8f);

        selectBox.setSelected(property.getAsString());
        selectBox.setSize(GuiComponent.width, GuiComponent.height);
        selectBox.setPosition(width - 50f - (GuiComponent.width + selectBox.getWidth() * selectBox.getScaleX()) / 2, height - selectBox.getHeight() * selectBox.getScaleY() / 2 - height / 2);
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                property.setValue(selectBox.getSelected());
                Config.getConfig().writeData();
            }
        });
    }
}
