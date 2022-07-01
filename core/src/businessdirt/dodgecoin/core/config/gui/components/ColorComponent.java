package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.actors.FloatingMenu;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class ColorComponent extends GuiComponent {

    /**
     * The displayed color
     */
    private Image color;

    public ColorComponent(PropertyData property, Skin skin, float width, float height) {
        ColorComponent instance = this;

        this.actor = new Button(skin.get("settingsUI", Button.ButtonStyle.class));
        Button button = (Button) this.actor;
        button.setTransform(true);
        button.setSize(76f * scale, 76f * scale);
        button.setPosition(width - 50f * scale - (GuiComponent.width + this.actor.getWidth() * this.actor.getScaleX()) / 2, height - this.actor.getHeight() * this.actor.getScaleY() / 2 - height / 2);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // activate the color picker
                ColorPicker.activate(instance, property);
            }
        });

        // set the color of the button
        setColor(property.getAsColor());
        // add the image to the button
        button.add(this.color).width(56f * scale).height(56f * scale).pad(10f * scale);
    }

    public void setColor(Color color) {
        // 1x1 pixels Pixmap filled with the color
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        // first creation of the image
        if (this.color == null) {
            Drawable drawable = new TextureRegionDrawable(new Texture(pixmap));
            this.color = new Image(drawable);
            return;
        }

        // set the color
        this.color.setDrawable(new TextureRegionDrawable(new Texture(pixmap)));
    }

    /**
     * ColorPicker is single instance
     */
    public static class ColorPicker {

        private final FloatingMenu picker;

        private static ColorPicker instance;
        private PropertyData property;
        private final TextField hexCode;
        private ColorComponent colorComponent;
        private final Slider alpha;
        private final Image colorWheel, colorWheelPicker;

        private ColorPicker(Skin skin) {
            this.picker = new FloatingMenu(skin, 500f * scale, 500f * scale);

            // color code in hex
            this.hexCode = new TextField("", skin);
            this.hexCode.setBounds(25f * scale, 25f * scale, 450f * scale, 50f * scale);
            this.hexCode.setAlignment(Align.center);
            this.hexCode.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    String content = hexCode.getText();

                    // check if the String starts with a #
                    if (!content.startsWith("#")) {
                        int position = hexCode.getCursorPosition();
                        content = "#" + content;
                        hexCode.setText(content);
                        hexCode.setCursorPosition(position + 1);
                    }

                    // remove any invalid characters
                    int position = hexCode.getCursorPosition();
                    content = content.replaceAll("[^#0123456789abcdef]", "");
                    hexCode.setText(content);
                    hexCode.setCursorPosition(Math.max(position, content.length() - 1));

                    // set the color to the input
                    if (content.length() == 9) {
                        Color color = property.getAsColor();
                        String[] colorChannels = content.replace("#", "").split("(?<=\\G.{" + 2 + "})");
                        color.set(Integer.decode("0x" + colorChannels[0]) / 255f, Integer.decode("0x" + colorChannels[1]) / 255f,
                                Integer.decode("0x" + colorChannels[2]) / 255f, Integer.decode("0x" + colorChannels[3]) / 255f);

                        alpha.setValue(Integer.decode("0x" + colorChannels[3]));
                        property.setValue(color);
                        colorComponent.setColor(color);
                        Config.getConfig().writeData();

                        ColorPicker.get().movePicker(color);
                    }

                    // remove any overflow chars
                    if (content.length() >= 10) {
                        position = hexCode.getCursorPosition();
                        hexCode.setText(String.valueOf(content.subSequence(0, 9)));
                        hexCode.setCursorPosition(Math.max(position, content.length() - 1));
                    }
                }
            });

            // Slider for Transparency
            this.alpha = new Slider(0, 255, 1, true, skin);
            this.alpha.setBounds(425f * scale, 100f * scale, 50f * scale, 375f * scale);
            this.alpha.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // set the alpha value of the color to the value of the slider
                    Color color = property.getAsColor();
                    color.a = alpha.getValue() / 255f;

                    // update the config value and the color of the ColorComponent
                    property.setValue(color);
                    Config.getConfig().writeData();
                    colorComponent.setColor(property.getAsColor());

                    // update the hexCode text
                    if (hexCode.getText().length() > 7) hexCode.setText(hexCode.getText().subSequence(0, 7) + toHex(alpha.getValue()));
                }
            });

            // Image for color picking
            this.colorWheel = new Image(DodgeCoin.assets.getTexture("textures/gui/settings/colorwheel.png"));
            this.colorWheel.setBounds(25f * scale, 100f * scale, 375f * scale, 375f * scale);
            this.colorWheel.addListener(new ColorWheelClickListener());

            // Picker
            this.colorWheelPicker = new Image(skin, "pickerBlack");
            this.colorWheelPicker.setBounds(0f, 0f, 16f * scale, 16f * scale);

            this.picker.addActor(this.hexCode);
            this.picker.addActor(this.alpha);
            this.picker.addActor(this.colorWheel);
            this.picker.addActor(this.colorWheelPicker);
        }

        /**
         * Activates the Color Picker.
         * @param colorComponent the Component it was activated from
         * @param property the property of which the color should be changed
         */
        public static void activate(ColorComponent colorComponent, PropertyData property) {
            // activate the FloatingMenu
            ColorPicker.get().picker.activate();

            ColorPicker.get().property = property;
            ColorPicker.get().colorComponent = colorComponent;

            Color color = property.getAsColor();
            ColorPicker.get().alpha.setValue(color.a * 255f);

            String hex = toHex(color.r * 255f) + toHex(color.g * 255f) + toHex(color.b * 255f) + toHex(color.a * 255f);
            ColorPicker.get().hexCode.setText("#" + hex);

            // find the position of the color on the color wheel
            ColorPicker.get().movePicker(color);
        }

        /**
         * Move the picker that indicates the color on the wheel to the given color.
         * @param color the color the picker shall be moved to.
         */
        private void movePicker(Color color) {
            // convert the color wheel to a pixmap and scale it to the positon on screen
            Pixmap pixmap = new Pixmap(Gdx.files.internal("textures/gui/settings/colorwheel.png"));
            float scale = colorWheel.getWidth() / pixmap.getWidth();

            // loop through all pixels
            for (int x = 0; x < pixmap.getWidth(); x++) {
                for (int y = 0; y < pixmap.getHeight(); y++) {
                    // get the color of the current pixel
                    Color pixelColor = new Color(pixmap.getPixel(x, y));

                    // check if the parsed color matches the color of the pixel
                    if (pixelColor.r == color.r && pixelColor.g == color.g && pixelColor.b == color.b) {
                        // set the position of the picker to this pixel
                        colorWheelPicker.setPosition(735f * scale + x * scale - 8f * scale, 390f * scale + (colorWheel.getHeight() - y * scale) - 8f * scale);
                    }
                }
            }
        }

        /**
         * Converts a float value to a hex number.
         * Appends a 0 at the start if the length of the hex String is 1.
         * @param v the float value to be converted
         * @return the converted hex value
         */
        private static String toHex(float v) {
            String hex = Integer.toHexString((int) (v));
            if (hex.length() == 1) hex = "0" + hex;
            return hex;
        }

        /**
         * Maps the position of the picker to a color on the wheel.
         * Saves its rgb values to the config.
         * @param x the x position on the color wheel
         * @param y the y position on the color wheel
         */
        public void setColor(float x, float y) {
            // convert the color wheel to a pixmap and scale it to the positon on screen
            Pixmap map = new Pixmap(Gdx.files.internal("assets/textures/gui/settings/colorwheel.png"));
            float scale = colorWheel.getWidth() / map.getWidth();

            // get the value of the selected color
            int color = map.getPixel((int) (x / scale), map.getHeight() - (int) (y / scale));
            Color newColor = new Color(color);
            newColor.a = this.property.getAsColor().a;

            // save the color and change other components (i.e. hexCode, etc.)
            this.property.setValue(newColor);
            this.colorComponent.setColor(newColor);
            this.hexCode.setText("#" + toHex(newColor.r * 255f) + toHex(newColor.g * 255f) + toHex(newColor.b * 255f) + toHex(newColor.a * 255f));
            Config.getConfig().writeData();
        }

        /**
         * @return the current instance
         */
        public static ColorPicker get() {
            return ColorPicker.instance;
        }

        /**
         * Re-instantiates the ColorPicker
         * @param skin the skin used by the ui elements
         * @return the new instance
         */
        public static ColorPicker newInstance(Skin skin) {
            ColorPicker.instance = new ColorPicker(skin);
            return ColorPicker.instance;
        }

        /**
         * @return The color pickers main {@link Actor}
         */
        public Group getActor() {
            return this.picker.getActor();
        }
    }

    /**
     * Detects clicks on the color wheel and maps the position to a color
     */
    private static class ColorWheelClickListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            if (inBounds(x, y)) {
                ColorPicker.get().colorWheelPicker.setPosition(ColorPicker.get().colorWheel.getX() + x - ColorPicker.get().colorWheelPicker.getWidth() / 2,
                        ColorPicker.get().colorWheel.getY() + y - ColorPicker.get().colorWheelPicker.getHeight() / 2);
                ColorPicker.get().setColor(x, y);
            }
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);
            if (inBounds(x, y)) {
                ColorPicker.get().colorWheelPicker.setPosition(ColorPicker.get().colorWheel.getX() + x - ColorPicker.get().colorWheelPicker.getWidth() / 2,
                        ColorPicker.get().colorWheel.getY() + y - ColorPicker.get().colorWheelPicker.getHeight() / 2);
                ColorPicker.get().setColor(x, y);
            }
        }

        /**
         * Checks if the position is in bounds of the color wheel.
         * @param x the x position
         * @param y the y position
         * @return true if the clicked Position is inside the color wheel
         */
        private boolean inBounds(float x, float y) {
            if (x < 0 || x > ColorPicker.get().colorWheel.getWidth()) return false;
            if (y < 0 || y > ColorPicker.get().colorWheel.getHeight()) return false;
            return inRadius(x, y, ColorPicker.get().colorWheel.getWidth() / 2);
        }

        /**
         * Checks if the clicked position is inside a circle with the given radius.
         * The position origin is assumed to be the corner of a square circumscribing the circle.
         * @param x the x position
         * @param y the y position
         * @param radius the radius of the circle
         * @return true if the position is inside the circle
         */
        private boolean inRadius(float x, float y, float radius) {
            float xPos = radius - x;
            float yPos = radius - y;
            return Math.sqrt(xPos * xPos + yPos * yPos) < radius - 2f;
        }
    }
}
