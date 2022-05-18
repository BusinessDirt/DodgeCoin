package businessdirt.dodgecoin.gui.buttons;

import businessdirt.dodgecoin.gui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ImageButton {

    private final BufferedImage image;
    private final JButton button;
    private boolean enabled = true;
    private final String name;

    public ImageButton(int x, int y, int width, int height, BufferedImage image, ActionListener listener, String name) {
        this.image = resize(image, width, height);
        this.name = name;
        this.enabled = true;

        this.button = new JButton(new ImageIcon(this.image));
        this.button.setLocation(x, y);
        this.button.setSize(width, height);
        this.button.addActionListener(listener);
        this.button.setBackground(Color.BLACK);
        this.button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        this.button.setVisible(true);
        Window.get().frame.add(this.button);
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public BufferedImage getImage() {
        return image;
    }

    public JButton getButton() {
        return button;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.button.setVisible(enabled);
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }
}
