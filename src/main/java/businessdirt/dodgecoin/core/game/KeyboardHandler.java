package businessdirt.dodgecoin.core.game;

import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.config.Constants;
import businessdirt.dodgecoin.gui.images.Image;
import businessdirt.dodgecoin.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyboardHandler {

    private static KeyboardHandler instance;

    private KeyboardHandler() {
        Util.logEvent("KeyboardHandler initialized!");
    }

    public void registerKeyBindings() {

        // Move left
        new KeyBinding(new int[] { KeyEvent.VK_A, KeyEvent.VK_LEFT }, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { moveLeft(); }
        });

        // Move right
        new KeyBinding(new int[] { KeyEvent.VK_D, KeyEvent.VK_RIGHT }, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { moveRight(); }
        });

        // Pause / back
        new KeyBinding(new int[] { KeyEvent.VK_ESCAPE, KeyEvent.VK_BACK_SPACE }, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseBack();
            }
        });

        // Unpause / Start
        new KeyBinding(new int[] { KeyEvent.VK_ENTER, KeyEvent.VK_SPACE}, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { start(); }
        });

        // Log Keybinding registration
        Util.logEvent("Keybindings registered!");
    }

    private void moveLeft() {
        Image player = Window.getDraw().getPlayer();
        if (player.getX() > 0) {
            int newX = player.getX() - Constants.MOVEMENT_SPEED;
            player.setX(Math.max(newX, 0));
        }
    }

    private void moveRight() {
        Image player = Window.getDraw().getPlayer();
        if (player.getX() < Window.getWidth() - player.getWidth() - 20) {
            int newX = player.getX() + Constants.MOVEMENT_SPEED;
            player.setX(Math.min(newX, Window.getWidth() - player.getWidth() - 20));
        }
    }

    private void pauseBack() {
        if (Window.getGameState() == GameState.GAME) {
            Window.setGameState(GameState.PAUSE);
        } else if (Window.getGameState() == GameState.SETTINGS) {
            Window.setGameState(GameState.MAIN_MENU);
        } else if (Window.getGameState() == GameState.SHOP) {
            Window.setGameState(GameState.MAIN_MENU);
        } else if (Window.getGameState() == GameState.PAUSE) {
            Window.setGameState(GameState.MAIN_MENU);
            Window.getDraw().getCoins().clear();
            GameClock.setScore(0);
        } else return;
        GameClock.get().setRunning(false);
    }

    private void start() {
        if (Window.getGameState() == GameState.MAIN_MENU) {
            Window.setGameState(GameState.GAME);
        } else if (Window.getGameState() == GameState.PAUSE) {
            Window.setGameState(GameState.GAME);
        } else return;
        GameClock.get().setRunning(true);
    }

    public static KeyboardHandler get() {
        if (instance == null) instance = new KeyboardHandler();
        return instance;
    }
}
