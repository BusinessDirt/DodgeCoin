package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class MenuScreen extends AbstractScreen {

    public MenuScreen() {
        super(DodgeCoin.assets.getSkin("skins/menu/skin.json"), Color.TEAL);
    }

    @Override
    public void show() {
        // Container to align the buttons
        Table menuContainer = new Table();
        menuContainer.setBounds(50f, 50f, 350f, 980f);
        menuContainer.align(Align.top);

        // Settings Button
        TextButton settingsButton = new TextButton("Settings", this.skin.get("settingsButton", TextButton.TextButtonStyle.class));
        settingsButton.getLabel().setFontScale(2f);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.get().setScreen(new SettingsScreen());
            }
        });
        menuContainer.add(settingsButton).width(350f).height(100f).padBottom(5f);
        menuContainer.row();

        // Shop Button
        TextButton shopButton = new TextButton("Shop", this.skin.get("shopButton", TextButton.TextButtonStyle.class));
        shopButton.getLabel().setFontScale(2f);
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.get().setScreen(new ShopScreen());
            }
        });
        menuContainer.add(shopButton).width(350f).height(100f).padBottom(5f);
        menuContainer.row();

        // Play Button
        TextButton playButton = new TextButton("Play", this.skin.get("playButton", TextButton.TextButtonStyle.class));
        playButton.getLabel().setFontScale(2f);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.get().setScreen(new GameScreen());
            }
        });
        menuContainer.add(playButton).width(350f).height(100f).padBottom(5f);
        menuContainer.row();

        this.stage.addActor(menuContainer);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
