package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.gui.SettingsGui;
import businessdirt.dodgecoin.core.input.Keyboard;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class SettingsScreen extends AbstractScreen {

    private ScrollPane pane;

    public SettingsScreen() {
        super(DodgeCoin.assets.getSkin("skins/settings/skin.json"), Color.TEAL);
    }

    @Override
    public void show() {
        // container for the scroll pane
        Table container = new Table();
        container.setBounds(0f, 0f, 1920f, 1080f);

        // table for all the config values
        Table table = new Table();
        table.align(Align.top);
        SettingsGui.newInstance(table, 1520f);

        // scroll pane for scrolling
        this.pane = new ScrollPane(table, skin.get("background", ScrollPane.ScrollPaneStyle.class));
        this.pane.setSmoothScrolling(true);
        this.pane.setScrollingDisabled(true, false);
        this.pane.setFadeScrollBars(true);
        this.pane.layout();

        // add the scroll pane to the container
        container.add(this.pane).pad(140f, 200f, 180f, 200f).width(1520f).height(760f);
        SettingsGui.get().setScrollPane(this.pane);
        this.stage.addActor(container);
        this.stage.setScrollFocus(this.pane);

        // Settings Label
        Label settingsLabel = new Label("Settings", skin);
        settingsLabel.setAlignment(Align.center);
        settingsLabel.setBounds(0, 940f, 1920f, 140f);
        settingsLabel.setFontScale(2f);
        this.stage.addActor(settingsLabel);

        // Back Button
        TextButton backButton = new TextButton("Back", skin.get("backButton", TextButton.TextButtonStyle.class));
        backButton.setBounds(960f - 175f, 40f, 350f, 100f);
        backButton.getLabel().setFontScale(2f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new MenuScreen());
            }
        });
        this.stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        this.pane.setScrollbarsVisible(true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
