package businessdirt.dodgecoin.core.game;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class KeyBinding {

    /**
     * Creates a listener to register inputs for keybindings
     */
    private static final JLabel listener = new JLabel();

    /**
     * The {@code keyCode} for the {@code KeyBinding}
     */
    private final int[] keyCodes;

    /**
     * The {@code modifier} for the {@code KeyBinding}
     */
    private final int modifier;

    /**
     * Creates a new instance of KeyBinding
     * @param keyCode from {@code java.awt.event.KeyEvent}
     * @param action i.e. {@code javax.swing.AbstractAction}
     */
    public KeyBinding(int keyCode, AbstractAction action) {
        this(keyCode, action, getModifier(keyCode));
    }
    public KeyBinding(int[] keyCodes, AbstractAction action) {
        this(keyCodes, action, getModifier(keyCodes[0]));
    }

    /**
     * Creates a new instance of KeyBinding
     * @param keyCode from {@code java.awt.event.KeyEvent}
     * @param action from {@code javax.swing.AbstractAction}
     * @param modifier from {@code java.awt.event.InputEvent}
     */
    public KeyBinding(int keyCode, AbstractAction action, int modifier) {
        this.keyCodes = new int[] { keyCode };
        this.modifier = modifier;
        listener.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyCode, modifier), keyCode);
        listener.getActionMap().put(keyCode, action);
    }

    /**
     * Creates a new instance of KeyBinding with multiple keyCodes
     * @param keyCodes from {@code java.awt.event.KeyEvent}
     * @param action from {@code javax.swing.AbstractAction}
     * @param modifier from {@code java.awt.event.InputEvent}
     */
    private KeyBinding(int[] keyCodes, AbstractAction action, int modifier) {
        this.keyCodes = keyCodes;
        this.modifier = modifier;

        for (int keyCode : keyCodes) {
            listener.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyCode, modifier), keyCode);
            listener.getActionMap().put(keyCode, action);
        }
    }

    /**
     * Gets the {@code modifier} for the given {@code keyCode}
     * @param keyCode from {@code java.awt.event.KeyEvent}
     * @return the {@code modifier} for the {@code keyCode}
     */
    private static int getModifier(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_CONTROL:
                return InputEvent.CTRL_DOWN_MASK;
            case KeyEvent.VK_SHIFT:
                return InputEvent.SHIFT_DOWN_MASK;
            case KeyEvent.VK_ALT:
                return InputEvent.ALT_DOWN_MASK;
            default:
                return 0;
        }
    }

    /**
     * Gets the {@code listener} that registers inputs
     * @return the {@code listener}
     */
    public static JLabel getKeyListener() {
        return listener;
    }

    /**
     * Gets the keyCodes of the {@code KeyBinding}
     * @return the {@code keyCodes}
     */
    public int[] getKeyCodes() {
        return keyCodes;
    }

    /**
     * Gets the modifier of the {@code KeyBinding}
     * @return the {@code modifier}
     */
    public int getModifier() {
        return modifier;
    }
}
