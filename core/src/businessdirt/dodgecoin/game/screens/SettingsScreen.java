package businessdirt.dodgecoin.game.screens;

import businessdirt.dodgecoin.DodgeCoin;
import businessdirt.dodgecoin.core.Config;
import businessdirt.dodgecoin.core.config.gui.SettingsGui;
import businessdirt.dodgecoin.core.config.gui.components.ColorComponent;
import businessdirt.dodgecoin.core.config.gui.components.KeyComponent;
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
        super(DodgeCoin.assets.getSkin("skins/ui/skin.json"), Color.TEAL);
    }

    @Override
    public void show() {
        // sizes to position everything correctly
        float containerOffset = 5f;
        float containerWidth = Gdx.graphics.getWidth() / 1.263f;
        float containerHeight = Gdx.graphics.getHeight() / 1.42f;
        float padX = (Gdx.graphics.getWidth() - containerWidth) / 2;
        float padY = (Gdx.graphics.getHeight() - containerHeight) / 2;

        float categoryWidth = containerWidth / 5f;
        float propertyWidth = containerWidth - categoryWidth;
        float itemWidth = Gdx.graphics.getWidth() / 5.5f;


        // container for the scroll pane
        Table container = new Table(skin);
        container.setBackground("scrollPane");
        container.setBounds(padX - containerOffset, padY - containerOffset, containerWidth + 2 * containerOffset, containerHeight + 2 * containerOffset);

        // table for all the config values
        Table propertyTable = new Table();
        propertyTable.align(Align.top);

        // table for all the config values
        Table categoryTable = new Table();
        categoryTable.align(Align.top);

        // re-instantiate the SettingsGui
        SettingsGui.newInstance(categoryTable, propertyTable, categoryWidth, propertyWidth);

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
        container.add(this.categoriesPane).width(categoryWidth).height(containerHeight).pad(containerOffset, containerOffset, containerOffset, 0f);
        container.add(this.valuesPane).width(propertyWidth).height(containerHeight).pad(containerOffset, 0f, containerOffset, containerOffset);
        SettingsGui.get().setScrollPane(this.valuesPane);
        this.stage.addActor(container);
        this.stage.setScrollFocus(this.valuesPane);

        // Settings Label
        Label settingsLabel = new Label("Settings", skin);
        settingsLabel.setAlignment(Align.bottomLeft);
        settingsLabel.setBounds(padX, Gdx.graphics.getHeight() - padY / 2 - itemWidth / 14f, itemWidth, itemWidth / 7f);
        settingsLabel.setFontScale(Gdx.graphics.getHeight() / 540f);
        this.stage.addActor(settingsLabel);

        // Search Field
        TextField search = new TextField("", skin);
        search.setBounds(Gdx.graphics.getWidth() - padX - itemWidth, Gdx.graphics.getHeight() - padY / 2 - itemWidth / 14f, itemWidth, itemWidth / 7f);
        search.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SettingsGui.get().setSearchQuery(propertyTable, propertyWidth, search.getText());
            }
        });
        this.stage.addActor(search);

        // Back Button
        TextButton backButton = new TextButton("Back", skin.get("backButton", TextButton.TextButtonStyle.class));
        backButton.setBounds(Gdx.graphics.getWidth() / 2f - itemWidth / 2f, padY / 2f - itemWidth / 7f, itemWidth, itemWidth / 3.5f);
        backButton.getLabel().setFontScale(Gdx.graphics.getHeight() / 540f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DodgeCoin.assets.getSound("sounds/button.mp3").play(Config.sfxVolume / 100f);
                DodgeCoin.get().setScreen(new MenuScreen());
            }
        });
        this.stage.addActor(backButton);

        // special components
        this.stage.addActor(ColorComponent.ColorPicker.newInstance(skin).getActor());
        this.stage.addActor(KeyComponent.KeyInputHandler.newInstance(skin).getActor());
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
