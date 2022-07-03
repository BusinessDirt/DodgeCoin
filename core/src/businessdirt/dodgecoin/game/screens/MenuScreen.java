package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
        // Container to align all the buttons
        Table menuContainer = new Table();
        menuContainer.setBounds(50f, 50f, Gdx.graphics.getWidth() / 5.5f, Gdx.graphics.getHeight() - 100f);
        menuContainer.align(Align.top);

        // Settings Button
        TextButton settingsButton = new TextButton("Settings", this.skin.get("settingsButton", TextButton.TextButtonStyle.class));
        settingsButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 540f);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new SettingsScreen());
            }
        });
        menuContainer.add(settingsButton).width(Gdx.graphics.getWidth() / 5.5f).height(Gdx.graphics.getHeight() / 10.8f).padBottom(5f);
        menuContainer.row();

        // Shop Button
        TextButton shopButton = new TextButton("Shop", this.skin.get("shopButton", TextButton.TextButtonStyle.class));
        shopButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 540f);
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new ShopScreen());
            }
        });
        menuContainer.add(shopButton).width(Gdx.graphics.getWidth() / 5.5f).height(Gdx.graphics.getHeight() / 10.8f).padBottom(5f);
        menuContainer.row();

        // Play Button
        TextButton playButton = new TextButton("Play", this.skin.get("playButton", TextButton.TextButtonStyle.class));
        playButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 540f);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new GameScreen());
            }
        });
        menuContainer.add(playButton).width(Gdx.graphics.getWidth() / 5.5f).height(Gdx.graphics.getHeight() / 10.8f).padBottom(5f);
        menuContainer.row();

        // Quit Button
        TextButton quitButton = new TextButton("Quit", this.skin.get("quitButton", TextButton.TextButtonStyle.class));
        quitButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 540f);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                Gdx.app.exit();
                System.exit(0);
            }
        });
        menuContainer.add(quitButton).width(Gdx.graphics.getWidth() / 5.5f).height(Gdx.graphics.getHeight() / 10.8f).padBottom(5f);
        menuContainer.row();

        // Technoblade
        Image technoblade = new Image(DodgeCoin.assets.getTexture("textures/misc/technobladeStatue.png"));
        technoblade.setScale((Gdx.graphics.getHeight() - 50f) / technoblade.getPrefHeight());
        technoblade.setPosition(Gdx.graphics.getWidth() - technoblade.getWidth() * technoblade.getScaleX() - 25f, 25f);
        this.stage.addActor(technoblade);

        this.stage.addActor(menuContainer);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
