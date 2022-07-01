package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.core.config.gui.components.GuiComponent;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.Config;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ParagraphComponent extends GuiComponent {

    public ParagraphComponent(PropertyData property, Skin skin, float width, float height) {
        String previousEntry = property.getAsString();
        this.actor = new TextField(previousEntry, skin);
        this.actor.setSize(GuiComponent.width, GuiComponent.height);
        this.actor.setPosition(width - 50f * scale - (GuiComponent.width + this.actor.getWidth() * this.actor.getScaleX()) / 2, height - this.actor.getHeight() * this.actor.getScaleY() / 2 - height / 2);
        this.actor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TextField field = (TextField) actor;
                int cursorPosition = field.getCursorPosition();

                // only accepts characters from a-z and A-Z
                field.setText(field.getText().replaceAll("[^a-zA-Z]", ""));
                field.setCursorPosition(Math.min(cursorPosition, field.getText().length()));
                property.setValue(field.getText());
                Config.getConfig().writeData();
            }
        });
    }
}
