package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.core.config.gui.components.GuiComponent;
import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.data.types.Key;
import businessdirt.dodgecoin.core.util.Util;
import businessdirt.dodgecoin.core.actors.FloatingMenu;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class KeyComponent extends GuiComponent {

    private final TextButton primary, secondary;

    public KeyComponent(PropertyData property, Skin skin, float width, float height) {
        KeyComponent instance = this;
        Key key = property.getAsKey();
        float yOff = (height - 25f * scale) / 2 - GuiComponent.height;

        this.actor = new Group();
        this.actor.setSize(GuiComponent.width, height);
        this.actor.setPosition(width - 50f * scale - GuiComponent.width, 0f);
        Group group = (Group) this.actor;

        String primaryChar = Util.getKeyCharFromCode(key.getPrimary());
        if (primaryChar.length() == 1) primaryChar = " ".concat(primaryChar).concat(" ");

        this.primary = new TextButton(primaryChar, skin.get("settingsUI", TextButton.TextButtonStyle.class));
        this.primary.setSize(GuiComponent.width, GuiComponent.height);
        this.primary.setPosition(0f, height - yOff - GuiComponent.height);
        this.primary.getLabel().setWrap(true);
        this.primary.align(Align.center);
        this.primary.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                KeyInputHandler.get().activate(instance, property, 'p');
            }
        });

        String secondaryChar = Util.getKeyCharFromCode(key.getSecondary());
        if (secondaryChar.length() == 1) secondaryChar = " ".concat(secondaryChar).concat(" ");

        this.secondary = new TextButton(secondaryChar, skin.get("settingsUI", TextButton.TextButtonStyle.class));
        this.secondary.setSize(GuiComponent.width, GuiComponent.height);
        this.secondary.setPosition(0f, yOff);
        this.secondary.getLabel().setWrap(true);
        this.secondary.align(Align.center);
        this.secondary.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                KeyInputHandler.get().activate(instance, property, 's');
            }
        });

        group.addActor(this.primary);
        group.addActor(this.secondary);
    }

    public static class KeyInputHandler {

        private final FloatingMenu menu;
        private static KeyInputHandler instance;
        private final Label label;

        private PropertyData property;
        private KeyComponent component;
        private char type;

        public KeyInputHandler(Skin skin) {
            this.menu = new FloatingMenu(skin, 500f * scale, 500f * scale);

            this.label = new Label("Press a key to bind it", skin);
            this.label.setSize(500f * scale, 500f * scale);
            this.label.setFontScale(2f * scale);
            this.label.setWrap(true);
            this.label.setAlignment(Align.center);
            this.label.addListener(new InputListener() {
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    if (menu.getActor().isVisible()) {
                        Key key = property.getAsKey();
                        if (type == 'p') {
                            key.setPrimary(event.getKeyCode());

                            String primaryChar = Util.getKeyCharFromCode(key.getPrimary());
                            if (primaryChar.length() == 1) primaryChar = " ".concat(primaryChar).concat(" ");
                            component.primary.setText(primaryChar);
                        } else if (type == 's') {
                            key.setSecondary(event.getKeyCode());

                            String secondaryChar = Util.getKeyCharFromCode(key.getSecondary());
                            if (secondaryChar.length() == 1) secondaryChar = " ".concat(secondaryChar).concat(" ");
                            component.secondary.setText(secondaryChar);
                        }

                        property.setValue(key);
                        menu.deactivate();
                        DodgeCoin.config.writeData();
                    }

                    return super.keyTyped(event, character);
                }
            });
            // set the keyboard focus to the label
            this.label.layout();

            this.menu.addActor(this.label);
        }

        public void activate(KeyComponent component, PropertyData property, char type) {
            menu.getActor().getStage().setKeyboardFocus(label);
            this.menu.activate();

            this.component = component;
            this.property = property;
            this.type = type;
        }

        public Group getActor() {
            return this.menu.getActor();
        }

        public static KeyInputHandler newInstance(Skin skin) {
            KeyInputHandler.instance = new KeyInputHandler(skin);
            return KeyInputHandler.instance;
        }

        public static KeyInputHandler get() {
            return KeyInputHandler.instance;
        }
    }
}
