package businessdirt.dodgecoin.game.objects;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.renderer.Renderer;
import businessdirt.dodgecoin.game.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends GameObject {

    // only one player -> single instance
    private static Player instance;

    // region of the sprite that is being drawn
    private final TextureRegion animationFrame;

    public enum AnimationFrame {
        LEFT, NONE, RIGHT
    }

    private Player() {
        super(Config.playerSkin, Constants.CENTER_X - 8 * Constants.PLAYER_SIZE_MULTIPLIER, Constants.PLAYER_Y_START,
                16 * Constants.PLAYER_SIZE_MULTIPLIER, 32 * Constants.PLAYER_SIZE_MULTIPLIER);

        // initialize the animation
        this.animationFrame = new TextureRegion(DodgeCoin.assets.get(this.texturePath, Texture.class));
        setAnimationFrame(AnimationFrame.NONE);
    }

    /**
     * Sets the animation frame. Updates the coordinates of the {@link TextureRegion}
     * @param frame the predefined animation frame
     */
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

    /**
     * Moves the player along the x-Axis
     * @param x delta x
     */
    private void translateX(int x) {
        this.x += x;
    }

    /**
     * Movement / Controls.
     * Moves the player left if {@code Config.moveLeft} is pressed.
     * Moves the player right if {@code Config.moveRight} is pressed.
     * @param dt time between frames
     */
    public void move(float dt) {
        // detect input
        // translate the player accounting for the MOVEMENT_SPEED
        // change the animation frame
        if (Config.moveLeft.pressed()) {
            translateX((int) (-Constants.MOVEMENT_SPEED * dt));
            setAnimationFrame(AnimationFrame.LEFT);
        } else if (Config.moveRight.pressed()){
            translateX((int) (Constants.MOVEMENT_SPEED * dt));
            setAnimationFrame(AnimationFrame.RIGHT);
        } else {
            setAnimationFrame(AnimationFrame.NONE);
        }

        // contain the player in the bounds of the game
        if (this.x < Constants.GAME_X_START) {
            this.x = Constants.GAME_X_START;
        }
        if (this.x > Constants.GAME_X_START + Constants.GAME_WIDTH - this.width) {
            this.x = Constants.GAME_X_START + Constants.GAME_WIDTH - this.width;
        }
    }

    /**
     * Draws the player at its position with its size.
     */
    @Override
    public void draw() {
        Renderer.get().drawImage(this.animationFrame, this.x, this.y, this.width, this.height);
    }

    public void setTexture(String path) {
        this.animationFrame.setTexture(DodgeCoin.assets.get(path, Texture.class));
    }

    /**
     * Creates the instance if it is null.
     * @return the instance
     */
    public static Player get() {
        if (Player.instance == null) Player.instance = new Player();
        return Player.instance;
    }
}
