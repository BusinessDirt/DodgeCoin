package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SubcategoryComponent extends GuiComponent {

    public SubcategoryComponent(String subcategory, Skin skin, float padTop, float width) {
        this.actor = new Group();
        Group group = (Group) this.actor;
        group.setBounds(0, 0f, width, 75f);

        Button line = new Button(skin.get("divider", Button.ButtonStyle.class));
        line.setBounds(15f, 15f, width - 30f, 5);
        line.setTouchable(Touchable.disabled);

        Label label = new Label(subcategory, skin);
        label.setFontScale(DodgeCoin.fullscreen.height / 720f);
        label.setPosition(35f, 22f);
        label.setColor(skin.getColor("fontChecked"));

        group.addActor(line);
        group.addActor(label);
    }
}
