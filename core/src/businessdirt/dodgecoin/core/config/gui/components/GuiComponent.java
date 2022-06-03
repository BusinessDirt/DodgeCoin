package businessdirt.dodgecoin.core.config.gui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GuiComponent {

    protected Actor actor;
    public static final float width = 140f, height = 35f;

    public Actor getActor() {
        return this.actor;
    }
}
