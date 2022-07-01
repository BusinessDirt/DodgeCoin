package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.actors.FloatingMenu;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.config.data.types.Key;
import businessdirt.dodgecoin.core.util.Util;
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

        // primary key code as string
        String primaryChar = Util.getKeyCharFromCode(key.getPrimary());
        if (primaryChar.length() == 1) primaryChar = " ".concat(primaryChar).concat(" ");

        // primary key
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

        // secondary key code as string
        String secondaryChar = Util.getKeyCharFromCode(key.getSecondary());
        if (secondaryChar.length() == 1) secondaryChar = " ".concat(secondaryChar).concat(" ");

        // secondary key
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

    /**
     * Handles the change of a key in the Settings Menu
     */
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
                    // only change a key if this menu is opened
                    if (menu.getActor().isVisible()) {
                        Key key = property.getAsKey();

                        // check if the primary or secondary key is being changed
                        if (type == 'p') {
                            // set the primary key
                            key.setPrimary(event.getKeyCode());

                            // set the text of the label
                            String primaryChar = Util.getKeyCharFromCode(key.getPrimary());
                            if (primaryChar.length() == 1) primaryChar = " ".concat(primaryChar).concat(" ");
                            component.primary.setText(primaryChar);
                        } else if (type == 's') {
                            // set the secondary key
                            key.setSecondary(event.getKeyCode());

                            // set the text of the label
                            String secondaryChar = Util.getKeyCharFromCode(key.getSecondary());
                            if (secondaryChar.length() == 1) secondaryChar = " ".concat(secondaryChar).concat(" ");
                            component.secondary.setText(secondaryChar);
                        }

                        // update the property and save it
                        property.setValue(key);
                        DodgeCoin.config.writeData();

                        // deactivate the menu
                        menu.deactivate();
                    }

                    return super.keyTyped(event, character);
                }
            });
            // set the keyboard focus to the label
            this.label.layout();

            this.menu.addActor(this.label);
        }

        /**
         * Activates the KeyInputHandler
         * @param component the component the menu is being activated from
         * @param property the property of which the key shall be changed
         * @param type either 'p' for the primary key or 's' for the secondary key
         */
        public void activate(KeyComponent component, PropertyData property, char type) {
            menu.getActor().getStage().setKeyboardFocus(label);
            this.menu.activate();

            this.component = component;
            this.property = property;
            this.type = type;
        }

        /**
         * @return the main {@link Actor} of the KeyInputHandler
         */
        public Group getActor() {
            return this.menu.getActor();
        }

        /**
         * Re-instantiates the KeyInputHandler
         * @param skin the skin used for ui elements
         * @return the new instance
         */
        public static KeyInputHandler newInstance(Skin skin) {
            KeyInputHandler.instance = new KeyInputHandler(skin);
            return KeyInputHandler.instance;
        }

        /**
         * @return the current instance
         */
        public static KeyInputHandler get() {
            return KeyInputHandler.instance;
        }
    }
}
