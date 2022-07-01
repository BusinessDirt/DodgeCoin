package businessdirt.dodgecoin.core.actors;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Basically a pop-up menu.
 */
public class FloatingMenu {

    private final Skin skin;
    private final float x, y;
    private final float width, height;

    private final Group menu;
    private final List<Actor> actors;

    public FloatingMenu(Skin skin, float x, float y, float width, float height) {
        this.skin = skin;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.menu = new Group();
        this.menu.setBounds(0f, 0f, DodgeCoin.fullscreen.width, DodgeCoin.fullscreen.height);
        this.actors = new LinkedList<>();

        setBackgroundOpacity(0.4f);

        // the menu
        Button button = new Button(skin.get("blank", Button.ButtonStyle.class));
        button.setZIndex(1);
        button.setName("menu");
        button.setPosition(x, y);
        button.setSize(width, height);
        menu.addActor(button);

        this.deactivate();
    }

    public FloatingMenu(Skin skin, float width, float height) {
        this(skin, (DodgeCoin.fullscreen.width - width) / 2, (DodgeCoin.fullscreen.height - height) / 2, width, height);
    }

    public void setBackgroundOpacity(float opacity) {
        // remove the previous background
        Actor background = menu.findActor("background");
        if (background != null) menu.removeActor(background);

        // generate a new pixmap with the given opacity
        Pixmap pixmap = new Pixmap(DodgeCoin.fullscreen.width, DodgeCoin.fullscreen.height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0f, 0f, 0f, opacity));
        pixmap.fill();

        // create a drawable and then an image of the pixmap
        Drawable drawable = new TextureRegionDrawable(new Texture(pixmap));
        Image drawableImage = new Image(drawable);
        drawableImage.setName("background");
        drawableImage.setZIndex(0);
        drawableImage.addListener(new BackgroundClickListener(this));

        // add the background to the group
        menu.addActor(drawableImage);
    }

    public void addActor(Actor actor) {
        actor.setZIndex(2 + actors.size());
        actor.setPosition(this.x + actor.getX(), this.y + actor.getY());
        if (actor.getName() == null || actor.getName().equals("")) actor.setName("actor" + actors.size());
        this.menu.addActor(actor);
    }

    public void removeActor(String name) {
        Optional<Actor> rem = this.actors.stream().filter(actor -> actor.getName().equals(name)).findFirst();
        rem.ifPresent(this.actors::remove);
    }

    public void removeActor(int id) {
        this.actors.remove(id);
    }

    public void activate() {
        this.menu.setVisible(true);
    }

    public void deactivate() {
        this.menu.setVisible(false);
    }

    public Group getActor() {
        return this.menu;
    }

    /**
     * Listens to clicks on the background of a {@link FloatingMenu}.
     * If it is clicked the menu will deactivate.
     */
    private static class BackgroundClickListener extends ClickListener {

        private final FloatingMenu floatingMenu;

        public BackgroundClickListener(FloatingMenu floatingMenu) {
            this.floatingMenu = floatingMenu;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            this.floatingMenu.deactivate();
        }
    }
}
