package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class MenuScreen extends AbstractScreen {

    public MenuScreen() {
        super(DodgeCoin.assets.getSkin("skins/ui/skin.json"), Color.TEAL);
    }

    @Override
    public void show() {
        // Container to align the buttons
        Table menuContainer = new Table();
        menuContainer.setBounds(50f, 50f, DodgeCoin.fullscreen.width / 5.5f, DodgeCoin.fullscreen.height - 100f);
        menuContainer.align(Align.top);

        // Settings Button
        TextButton settingsButton = new TextButton("Settings", this.skin.get("settingsButton", TextButton.TextButtonStyle.class));
        settingsButton.getLabel().setFontScale(DodgeCoin.fullscreen.height / 540f);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new SettingsScreen());
            }
        });
        menuContainer.add(settingsButton).width(DodgeCoin.fullscreen.width / 5.5f).height(DodgeCoin.fullscreen.height / 10.8f).padBottom(5f);
        menuContainer.row();

        // Shop Button
        TextButton shopButton = new TextButton("Shop", this.skin.get("shopButton", TextButton.TextButtonStyle.class));
        shopButton.getLabel().setFontScale(DodgeCoin.fullscreen.height / 540f);
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new ShopScreen());
            }
        });
        menuContainer.add(shopButton).width(DodgeCoin.fullscreen.width / 5.5f).height(DodgeCoin.fullscreen.height / 10.8f).padBottom(5f);
        menuContainer.row();

        // Play Button
        TextButton playButton = new TextButton("Play", this.skin.get("playButton", TextButton.TextButtonStyle.class));
        playButton.getLabel().setFontScale(DodgeCoin.fullscreen.height / 540f);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new GameScreen());
            }
        });
        menuContainer.add(playButton).width(DodgeCoin.fullscreen.width / 5.5f).height(DodgeCoin.fullscreen.height / 10.8f).padBottom(5f);
        menuContainer.row();

        // Quit Button
        TextButton quitButton = new TextButton("Quit", this.skin.get("quitButton", TextButton.TextButtonStyle.class));
        quitButton.getLabel().setFontScale(DodgeCoin.fullscreen.height / 540f);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                Gdx.app.exit();
                System.exit(0);
            }
        });
        menuContainer.add(quitButton).width(DodgeCoin.fullscreen.width / 5.5f).height(DodgeCoin.fullscreen.height / 10.8f).padBottom(5f);
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
