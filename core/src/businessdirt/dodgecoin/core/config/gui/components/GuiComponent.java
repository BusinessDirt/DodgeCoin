package businessdirt.dodgecoin.core.config.gui.components;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class GuiComponent {

    protected Actor actor;
    public static final float width = DodgeCoin.fullscreen.width / 13.7f;
    public static final float height = DodgeCoin.fullscreen.height / 30.85f;

    public Actor getActor() {
        return this.actor;
    }
}
