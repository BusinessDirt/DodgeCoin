package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.config.data.Property;
import businessdirt.dodgecoin.core.config.data.PropertyData;
import businessdirt.dodgecoin.core.util.APIHandler;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.SkinHandler;
import businessdirt.dodgecoin.core.util.AssetLoader;
import businessdirt.dodgecoin.core.util.MusicPlayer;
import businessdirt.dodgecoin.core.util.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoadingScreen extends ScreenAdapter {

    public LoadingScreen() {
        // Log basic information
        Util.logEvent("Using OpenGL Version " + Gdx.graphics.getGLVersion());

        // Stock-market API
        new Thread(APIHandler::getStockMarketApiValues).start();

        // Configuration
        try {
            SkinHandler.init();
            DodgeCoin.config = Config.getConfig();
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
        if (DodgeCoin.assets.update()) {
            new MusicPlayer("music/background2.mp3");
            DodgeCoin.get().setScreen(new MenuScreen());
        }
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
