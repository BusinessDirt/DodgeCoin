package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.Config;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Objects;

public class NumberComponent extends GuiComponent {

    public NumberComponent(PropertyData property, Skin skin, float width, float height) {
        String previousEntry = String.valueOf(property.getAsDouble());
        this.actor = new TextField(previousEntry, skin);
        this.actor.setSize(GuiComponent.width, GuiComponent.height);
        this.actor.setPosition(width - 50f - (GuiComponent.width + this.actor.getWidth() * this.actor.getScaleX()) / 2, height - this.actor.getHeight() * this.actor.getScaleY() / 2 - height / 2);
        this.actor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TextField field = (TextField) actor;
                int cursorPosition = field.getCursorPosition();
                field.setText(field.getText().replaceAll("[^\\d.]", ""));
                System.out.println(field.getText());
                field.setCursorPosition(Math.min(cursorPosition, field.getText().length()));
                if (!Objects.equals(field.getText(), "")) {
                    property.setValue(Double.parseDouble(field.getText()));
                    Config.getConfig().writeData();
                }
            }
        });
    }
}
