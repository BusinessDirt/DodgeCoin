package businessdirt.dodgecoin.gui.buttons;

import businessdirt.dodgecoin.gui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ImageButton {

    private BufferedImage image;
    private final JButton button;
    private boolean enabled = true;
    private final String name;

    public ImageButton(int x, int y, int width, int height, BufferedImage image, ActionListener listener, String name) {
        this.image = image == null ? null : resize(image, width, height);
        this.name = name;
        this.enabled = true;

        if (this.image != null) {
            this.button = new JButton(new ImageIcon(this.image));
        } else {
            this.button = new JButton(name);
        }
        this.button.setLocation(x, y);
        this.button.setSize(width, height);
        this.button.addActionListener(listener);
        this.button.setBackground(Color.BLACK);
        this.button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        this.button.setVisible(true);
        Window.get().frame.add(this.button);
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        if (img == null) return null;
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage fitImageToSize(BufferedImage image, int width, int height) {
        if (image == null) return null;
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        float ratio = (float) height / image.getHeight();
        image = resize(image, (int) (image.getWidth() * ratio) - 16, height - 16);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, (width - image.getWidth()) / 2, (height - image.getHeight()) / 2, image.getWidth(), image.getHeight(), null);
        g2d.dispose();
        return resizedImage;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image == null ? null : resize(image, this.button.getWidth(), this.button.getHeight());
        this.button.setIcon(this.image == null ? null : new ImageIcon(this.image));
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
