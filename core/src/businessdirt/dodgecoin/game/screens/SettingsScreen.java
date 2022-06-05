package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.gui.SettingsGui;
import businessdirt.dodgecoin.core.config.gui.components.ColorComponent;
import businessdirt.dodgecoin.core.input.Keyboard;
import businessdirt.dodgecoin.core.renderer.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class SettingsScreen extends AbstractScreen {

    private ScrollPane valuesPane, categoriesPane;

    public SettingsScreen() {
        super(DodgeCoin.assets.getSkin("skins/settings/skin.json"), Color.TEAL);
    }

    @Override
    public void show() {
        float containerOffset = 5f;

        // container for the scroll pane
        Table container = new Table(skin);
        container.setBackground("scrollPane");
        container.setBounds(200f - containerOffset, 180f - containerOffset, 1520f + 2 * containerOffset, 760f + 2 * containerOffset);

        // table for all the config values
        Table propertyTable = new Table();
        propertyTable.align(Align.top);

        // table for all the config values
        Table categoryTable = new Table();
        categoryTable.align(Align.top);

        SettingsGui.newInstance(categoryTable, propertyTable, 300f, 1220f);

        // scroll pane for the config values
        this.valuesPane = new ScrollPane(propertyTable, skin.get("default", ScrollPane.ScrollPaneStyle.class));
        this.valuesPane.setSmoothScrolling(true);
        this.valuesPane.setScrollingDisabled(true, false);
        this.valuesPane.setFadeScrollBars(true);
        this.valuesPane.layout();

        // scroll pane for the categories
        this.categoriesPane = new ScrollPane(categoryTable, skin.get("default", ScrollPane.ScrollPaneStyle.class));
        this.categoriesPane.setSmoothScrolling(true);
        this.categoriesPane.setScrollingDisabled(true, false);
        this.categoriesPane.setFadeScrollBars(true);
        this.categoriesPane.setScrollBarPositions(true, false);
        this.categoriesPane.layout();

        // add everything to the container
        container.add(this.categoriesPane).width(300f).height(760f).pad(containerOffset, containerOffset, containerOffset, 0f);
        container.add(this.valuesPane).width(1220f).height(760f).pad(containerOffset, 0f, containerOffset, containerOffset);
        SettingsGui.get().setScrollPane(this.valuesPane);
        this.stage.addActor(container);
        this.stage.setScrollFocus(this.valuesPane);

        // Settings Label
        Label settingsLabel = new Label("Settings", skin);
        settingsLabel.setAlignment(Align.bottomLeft);
        settingsLabel.setBounds(200f, 960f, 760f, 140f);
        settingsLabel.setFontScale(2f);
        this.stage.addActor(settingsLabel);

        // Search Field
        TextField search = new TextField("", skin);
        search.setBounds(1720f - 350f, 950f, 350f, 50f);
        search.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SettingsGui.get().setSearchQuery(propertyTable, 1220f, search.getText());
            }
        });
        this.stage.addActor(search);

        // Back Button
        TextButton backButton = new TextButton("Back", skin.get("backButton", TextButton.TextButtonStyle.class));
        backButton.setBounds(960f - 175f, 40f, 350f, 100f);
        backButton.getLabel().setFontScale(2f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.get().setScreen(new MenuScreen());
            }
        });
        this.stage.addActor(backButton);

        this.stage.addActor(ColorComponent.ColorPicker.newInstance(skin).getActor());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        this.valuesPane.setScrollbarsVisible(true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
