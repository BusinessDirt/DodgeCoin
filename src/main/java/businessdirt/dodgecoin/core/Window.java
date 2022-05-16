package businessdirt.dodgecoin.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.IntBuffer;

public class Window {

    private static Window instance;
    private static JFrame jf;
    private final int width, height;
    private static Draw draw;

    public static final int MOVEMENT_SPEED = 10;

    private Window() {
        this.width = 720;
        this.height = 1080;
    }

    public static void start() {
        jf = new JFrame();
        jf.setSize(Window.get().width, Window.get().height);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setMinimumSize(new Dimension(Window.get().width, Window.get().height));
        jf.setResizable(false);
        jf.setTitle("DodgeCoin");

        jf.add(KeyBinding.getKeyListener());
        registerKeyBindings();

        Window.getDraw().setVisible(true);
        Window.getDraw().setLocation(0 , 34);
        Window.getDraw().setSize(Window.instance.width - 10, Window.instance.height - 34);
        jf.add(Window.getDraw());

        jf.setVisible(true);
    }

    private static void registerKeyBindings() {

        new KeyBinding(KeyEvent.VK_A, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image player = Window.getDraw().getPlayer();
                if (player.getX() > 0) {
                    int newX = player.getX() - MOVEMENT_SPEED;
                    player.setX(Math.max(newX, 0));
                }
            }
        });

        new KeyBinding(KeyEvent.VK_D, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image player = Window.getDraw().getPlayer();
                if (player.getX() < Window.get().getWidth() - player.getWidth() - 20) {
                    int newX = player.getX() + MOVEMENT_SPEED;
                    player.setX(Math.min(newX, Window.get().getWidth() - player.getWidth() - 20));
                }
            }
        });
    }

    public static Window get() {
        if (Window.instance == null) Window.instance = new Window();
        return Window.instance;
    }

    public static Draw getDraw() {
        if (Window.draw == null) Window.draw = new Draw();
        return Window.draw;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

