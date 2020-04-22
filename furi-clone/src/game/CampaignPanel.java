package game;
import shapes.*;
import graph.*;


import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class CampaignPanel extends UIPanel {
    private static final long serialVersionUID = 1L;
    public ArrayList<Boolean> bools = new ArrayList<Boolean>();

    public CampaignPanel(Point mouse) {
        super(mouse);
    }

    public void update() {
        for (int i = 0; i < bools.size(); ++i) {
            bools.set(i, elements.get(i).getClicked());
        }
    }

    public void resetBools() {
        for (int i = 0; i < bools.size(); ++i) {
            bools.set(i, false);
        }
    }

    public int checkGame() {
        for (int i = 0; i < bools.size()-1; ++i) {
            if (bools.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public void addUIElement(UIElementInteractable u) {
        elements.add(u);
        bools.add(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("???");
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(App.HITBOXCOLOURS[2]);
        for (UIElementInteractable u : elements) {
            u.draw(g, g2);
        }
        g.drawString("/The Story/", 10, 10);
        constants.Display.drawCircle(g, new Circle(new Point_(mouse.x, mouse.y), 20));
    }
}

