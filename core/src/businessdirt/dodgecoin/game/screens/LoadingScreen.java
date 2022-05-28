package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.APIHandler;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.SkinHandler;
import businessdirt.dodgecoin.game.AssetFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

public class LoadingScreen extends ScreenAdapter {

    public LoadingScreen() {
        // Stock-market API
        APIHandler.getStockMarketApiValues();

        // Configuration
        DodgeCoin.config = Config.getConfig();
        SkinHandler.init();

        // Assets
        DodgeCoin.assets = new AssetFinder(new AssetManager());
        DodgeCoin.assets.load();
        DodgeCoin.assets.getAssetManager().finishLoading();
    }

    @Override
    public void render(float delta) {
        // clear the screen from the previous frame
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if (DodgeCoin.assets.getAssetManager().update()) DodgeCoin.get().setScreen(new MenuScreen());
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
