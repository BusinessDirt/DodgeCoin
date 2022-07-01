package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.gui.SettingsGui;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class SliderComponent extends GuiComponent {

    /**
     * Label used to display the slider value
     */
    private final Label label;

    public SliderComponent(PropertyData property, Skin skin, float width, float height) {
        this.actor = new Slider(property.getProperty().min(), property.getProperty().max(), 1f, false, skin);
        Slider slider = (Slider) this.actor;

        slider.setValue(property.getAsInt());
        slider.setPosition(width - 50f * scale - this.actor.getWidth() * this.actor.getScaleX(), height - height / 2 - this.actor.getHeight() * this.actor.getScaleY() * 0.5f + 15f * scale);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // un-focus / focus the properties ScrollPane
                if (SettingsGui.get().getPropertyScrollPane() != null && actor.isTouchFocusTarget()) {
                    actor.getStage().unfocus(SettingsGui.get().getPropertyScrollPane());

                    // update the config and label value
                    property.setValue((int) slider.getValue());
                    label.setText((int) slider.getValue());
                    Config.getConfig().writeData();
                } else {
                    actor.getStage().setScrollFocus(SettingsGui.get().getPropertyScrollPane());
                }
            }
        });

        this.label = new Label(property.getPropertyValue().getValue(Config.getConfig()).toString(), skin);
        this.label.setAlignment(Align.center);
        this.label.setTouchable(Touchable.disabled);
        this.label.setPosition(width - 50f * scale - this.actor.getWidth() * this.actor.getScaleX(), height - height / 2 - this.actor.getHeight() * this.actor.getScaleY() * 0.5f - 15f * scale);
        this.label.setWidth(this.actor.getWidth());
    }

    public Label getLabel() {
        return this.label;
    }
}
