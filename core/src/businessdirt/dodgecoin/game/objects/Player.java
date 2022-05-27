package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.game.Constants;
import businessdirt.dodgecoin.core.input.Keyboard;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends GameObject {

    private static Player instance;
    private boolean animated;
    private TextureRegion animationFrame;

    public static enum AnimationFrame {
        LEFT, NONE, RIGHT
    }

    private Player(boolean animated) {
        super(Config.playerSkin, Constants.CENTER_X - 8 * Constants.PLAYER_SIZE_MULTIPLIER, Constants.PLAYER_Y_START,
                16 * Constants.PLAYER_SIZE_MULTIPLIER, 32 * Constants.PLAYER_SIZE_MULTIPLIER);

        // whether the player texture is animated or not
        this.animated = animated;

        // initialize the animation
        this.animationFrame = new TextureRegion(DodgeCoin.assets.get(this.texturePath, Texture.class));
        if (this.animated) {
            setAnimationFrame(AnimationFrame.NONE);
        }
    }

    public void setAnimationFrame(AnimationFrame frame) {
        switch (frame) {
            case NONE:
                this.animationFrame.setRegion(0, 0, 16, 32);
                break;
            case LEFT:
                this.animationFrame.setRegion(16, 0, 16, 32);
                break;
            case RIGHT:
                this.animationFrame.setRegion(32, 0, 16, 32);
                break;
        }
    }

    private void translateX(int x) {
        this.x += x;
    }

    public void move(float dt) {
        // move
        if (Keyboard.keyDown(Input.Keys.A)) {
            translateX((int) (-Constants.MOVEMENT_SPEED * dt));
            setAnimationFrame(AnimationFrame.LEFT);
        } else if (Keyboard.keyDown(Input.Keys.D)) {
            translateX((int) (Constants.MOVEMENT_SPEED * dt));
            setAnimationFrame(AnimationFrame.RIGHT);
        } else {
            setAnimationFrame(AnimationFrame.NONE);
        }

        // contain in bounds
        if (this.x < Constants.GAME_X_START) {
            this.x = Constants.GAME_X_START;
        }
        if (this.x > Constants.GAME_X_START + Constants.GAME_WIDTH - this.width) {
            this.x = Constants.GAME_X_START + Constants.GAME_WIDTH - this.width;
        }
    }

    @Override
    public void draw() {
        Renderer.get().drawImage(this.animationFrame, this.x, this.y, this.width, this.height);
    }

    public void setTexture(String path) {
        this.animationFrame.setTexture(DodgeCoin.assets.get(path, Texture.class));
    }

    public static Player get() {
        if (Player.instance == null) Player.instance = new Player(true);
        return Player.instance;
    }
}
