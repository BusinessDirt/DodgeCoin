package businessdirt.dodgecoin.core.config.gui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GuiComponent {

    /**
     * The main Actor used by the component.
     */
    protected Actor actor;

    /**
     * The scale of the component.
     * This is to properly position everything on every resolution.
     */
    protected static final float scale = Gdx.graphics.getHeight() / 1080f;

    /**
     * Width and Height of the Component to make everything uniform.
     */
    public static final float width = 140f * scale, height = 35f * scale;

    /**
     * @return the main {@link Actor} of the component
     */
    public Actor getActor() {
        return this.actor;
    }
}
