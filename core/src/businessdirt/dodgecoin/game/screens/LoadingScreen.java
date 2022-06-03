package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.util.APIHandler;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.SkinHandler;
import businessdirt.dodgecoin.core.util.AssetLoader;
import businessdirt.dodgecoin.core.util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

public class LoadingScreen extends ScreenAdapter {

    public LoadingScreen() {
        // Log basic information
        Util.logEvent("Using OpenGL Version " + Gdx.graphics.getGLVersion());

        // Stock-market API
        new Thread(APIHandler::getStockMarketApiValues).start();

        // Configuration
        try {
            DodgeCoin.config = Config.getConfig();
            SkinHandler.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assets
        DodgeCoin.assets = new AssetLoader();
        DodgeCoin.assets.load();
    }

    @Override
    public void render(float delta) {
        // clear the screen from the previous frame
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // if the assets finished loading it will enter the main menu
        if (DodgeCoin.assets.update()) DodgeCoin.get().setScreen(new MenuScreen());
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
