package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.game.GameState;
import businessdirt.dodgecoin.core.game.KeyBinding;
import businessdirt.dodgecoin.core.game.KeyboardHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.game.MouseHandler;

import javax.swing.*;
import java.awt.*;

public class Window {

    private static Window instance;
    public final JFrame frame;
    private static int width, height;
    private static Draw draw;

    private static GameState gameState;

    private Window() {
        Window.width = 720;
        Window.height = 1080;
        Window.gameState = GameState.MAIN_MENU;
        frame = new JFrame();
        Util.logEvent("Window initialized!");
    }

    public static void start() {
        instance.frame.setSize(Window.getWidth(), Window.getHeight());
        instance.frame.setResizable(true);
        instance.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        instance.frame.setLocationRelativeTo(null);
        instance.frame.setMinimumSize(new Dimension(Window.getWidth(), Window.getHeight()));
        instance.frame.setResizable(false);
        instance.frame.setTitle("DodgeCoin");

        instance.frame.add(KeyBinding.getKeyListener());
        KeyboardHandler.get().registerKeyBindings();

        Window.getDraw().setVisible(true);
        Window.getDraw().setLocation(0 , 34);
        Window.getDraw().setSize(Window.width, Window.height);
        instance.frame.add(Window.getDraw());

        instance.frame.getContentPane().addMouseListener(MouseHandler.get());

        instance.frame.setVisible(true);
        Util.logEvent("Window created!");
    }

    public static Window get() {
        if (Window.instance == null) Window.instance = new Window();
        return Window.instance;
    }

    public static Draw getDraw() {
        if (Window.draw == null) Window.draw = new Draw();
        return Window.draw;
    }

    public static int getWidth() {
        return Window.width;
    }

    public static int getHeight() {
        return Window.height;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        Window.gameState = gameState;
    }

    public static void setHeight(int height) {
        Window.height = height;
    }

    public static void setWidth(int width) {
        Window.width = width;
    }
}

