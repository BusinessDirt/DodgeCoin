package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.config.Config;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.gui.buttons.ImageButton;
import businessdirt.dodgecoin.gui.images.Sprite;
import businessdirt.dodgecoin.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class KeyboardHandler {

    private static KeyboardHandler instance;

    private KeyboardHandler() {
        Util.logEvent("KeyboardHandler initialized!");
    }

    public void registerKeyBindings() {

        // Move left
        new KeyBinding(new int[] { KeyEvent.VK_A, KeyEvent.VK_LEFT }, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Config.hardMode) {
                    GameClock.playerVelocity = -1;
                } else {
                    GameClock.playerVelocity = 0;
                    int newX = Window.getDraw().getPlayer().getX() - Constants.MOVEMENT_SPEED;
                    Window.getDraw().getPlayer().setX(Math.max(newX, Window.getGameXStart()));
                }
            }
        });

        // Move right
        new KeyBinding(new int[] { KeyEvent.VK_D, KeyEvent.VK_RIGHT }, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Config.hardMode) {
                    GameClock.playerVelocity = 1;
                } else {
                    GameClock.playerVelocity = 0;
                    int newX = Window.getDraw().getPlayer().getX() + Constants.MOVEMENT_SPEED;
                    Window.getDraw().getPlayer().setX(Math.min(newX, Window.getGameXStart() + Constants.GAME_WIDTH - Window.getDraw().getPlayer().getWidth()));
                }
            }
        });

        // Pause / back
        new KeyBinding(new int[] { KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE }, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        // Unpause / Start
        new KeyBinding(new int[] { KeyEvent.VK_ENTER, KeyEvent.VK_SPACE}, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { start(); }
        });

        // Toggle Hitboxes
        new KeyBinding(KeyEvent.VK_F3, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { Window.getDraw().toggleHitboxes(); }
        }, InputEvent.SHIFT_DOWN_MASK);

        // Log Keybinding registration
        Util.logEvent("Keybindings registered!");
    }

    public void cancel() {
        if (Window.getGameState() == GameState.GAME) {
            Window.setGameState(GameState.PAUSE);
            GameClock.playerVelocity = 0;

            for (ImageButton b : Window.buttons) {
                if (Objects.equals(b.getName(), "cancel")) b.setEnabled(true);
            }
        } else if (Window.getGameState() == GameState.SETTINGS) {
            Window.setGameState(GameState.MAIN_MENU);
        } else if (Window.getGameState() == GameState.SHOP) {
            Window.setGameState(GameState.MAIN_MENU);
            for (ImageButton b : Window.shopButtons) {
                b.setEnabled(false);
            }
        } else if (Window.getGameState() == GameState.GAME_OVER || Window.getGameState() == GameState.PAUSE) {
            Window.setGameState(GameState.MAIN_MENU);
            Window.getDraw().getCoins().clear();
        } else return;
        GameClock.get().setRunning(false);

        if (Window.getGameState() != GameState.PAUSE) for (ImageButton b : Window.buttons) {
            b.setEnabled(true);
        }
    }

    private void start() {
        if (Window.getGameState() == GameState.MAIN_MENU) {
            Window.setGameState(GameState.GAME);
        } else if (Window.getGameState() == GameState.PAUSE || Window.getGameState() == GameState.GAME_OVER) {
            Window.setGameState(GameState.GAME);
        } else return;
        GameClock.get().setRunning(true);

        for (ImageButton b : Window.buttons) {
            b.setEnabled(false);
        }
    }

    public static KeyboardHandler get() {
        if (instance == null) instance = new KeyboardHandler();
        return instance;
    }
}
