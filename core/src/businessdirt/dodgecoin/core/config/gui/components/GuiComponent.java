package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GuiComponent {

    protected Actor actor;

    protected static final float scale = DodgeCoin.fullscreen.height / 1080f;
    public static final float width = 140f * scale, height = 35f * scale;

    public Actor getActor() {
        return this.actor;
    }
}
