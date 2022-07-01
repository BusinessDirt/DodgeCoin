package businessdirt.dodgecoin.core.config.gui.components;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SubcategoryComponent extends GuiComponent {

    public SubcategoryComponent(String subcategory, Skin skin, float padTop, float width) {
        this.actor = new Group();
        Group group = (Group) this.actor;
        group.setBounds(0, 0f, width, 75f * scale);

        Button line = new Button(skin.get("divider", Button.ButtonStyle.class));
        line.setBounds(15f * scale, 15f * scale, width - 30f * scale, 5f * scale);
        line.setTouchable(Touchable.disabled);

        Label label = new Label(subcategory, skin);
        label.setFontScale(1.75f * scale);
        label.setPosition(35f * scale, 22f * scale);
        label.setColor(skin.getColor("fontChecked"));

        group.addActor(line);
        group.addActor(label);
    }
}
