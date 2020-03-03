package game;
import shapes.*;
import graph.*;


import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MainPanel extends UIPanel {
    private static final long serialVersionUID = 1L;
    public boolean[] bools = new boolean[2];
    public ArrayList<UIElementInteractable> elements = new ArrayList<UIElementInteractable>();

    public MainPanel(Point mouse) {
        super(mouse);
        bools[0] = false;
    }

    public void update() {
        bools[0] = elements.get(0).getClicked();
        bools[1] = elements.get(1).getClicked();
    }

    public void addUIElement(UIElementInteractable u) {
        elements.add(u);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(App.HITBOXCOLOURS[2]);
        for (UIElementInteractable u : elements) {
            u.draw(g, g2);
        }
        constants.Display.drawCircle(g, new Circle(new Point_(mouse.x, mouse.y), 20));
    }
}

