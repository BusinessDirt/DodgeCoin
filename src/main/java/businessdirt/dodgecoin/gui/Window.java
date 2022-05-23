package businessdirt.dodgecoin.gui;

import businessdirt.dodgecoin.core.game.GameState;
import businessdirt.dodgecoin.core.game.KeyBinding;
import businessdirt.dodgecoin.core.game.KeyboardHandler;
import businessdirt.dodgecoin.core.Util;
import businessdirt.dodgecoin.core.game.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class Window {

    private static Window instance;
    public static JFrame jf;
    private final int width, height;
    private static Draw draw;

    private static GameState gameState;

    private Window() {
        this.width = 480;
        this.height = 760;
        Window.gameState = GameState.MAIN_MENU;
        Util.logEvent("Window initialized!");
    }

    public static void start() {
        jf = new JFrame();
        jf.setSize(Window.getWidth(), Window.getHeight());
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setMinimumSize(new Dimension(Window.getWidth(), Window.getHeight()));
        jf.setResizable(false);
        jf.setTitle("DodgeCoin");

        jf.add(KeyBinding.getKeyListener());
        KeyboardHandler.get().registerKeyBindings();

        Window.getDraw().setVisible(true);
        Window.getDraw().setLocation(0 , 34);
        Window.getDraw().setSize(Window.instance.width - 10, Window.instance.height - 34);
        jf.add(Window.getDraw());

        jf.getContentPane().addMouseListener(MouseHandler.get());

        jf.setVisible(true);
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
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        Window.gameState = gameState;
    }
}

