package game;
import shapes.*;
import graph.*;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class UIPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Point mouse;
    public boolean[] bools = new boolean[2];
    public ArrayList<UIElementInteractable> elements = new ArrayList<UIElementInteractable>();

    public UIPanel(Point mouse) {
        this.mouse = mouse;
        bools[0] = false;
    }

    public void update() {
        if (elements.get(0).getClicked()) bools[0] = true;
        if (elements.get(1).getClicked()) bools[1] = true;
    }

    public void update(Point mouse) {
        this.mouse = mouse;
    }

    public void addUIElement(UIElementInteractable u) {
        elements.add(u);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(App.HITBOXCOLOURS[2]);
        constants.Display.drawCircle(g, new Circle(new Point_(mouse.x, mouse.y), 20));
        for (UIElementInteractable u : elements) {
            u.draw(g, g2);
        }
    }
}

