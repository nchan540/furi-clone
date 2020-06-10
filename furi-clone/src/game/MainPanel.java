package game;
import shapes.*;
import graph.*;


import java.awt.Point;
import java.lang.annotation.Inherited;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MainPanel extends UIPanel {
    private static final long serialVersionUID = 1L;
    // Booleans for components with states
    public ArrayList<Boolean> bools = new ArrayList<Boolean>();

    public MainPanel(Point mouse) {
        super(mouse);
    }

    /**
     * Checks for any updates to button states
     */
    public void update() {
        for (int i = 0; i < bools.size(); ++i) {
            bools.set(i, elements.get(i).getClicked());
        }
    }

    /**
     * Adds a UIElement to the UIPanel
     * @param u UIElement being added
     */
    public void addUIElement(UIElementInteractable u) {
        elements.add(u);
        bools.add(false);
    }

    /**
     * Displays the panel
     * @param g Graphics to display panel on
     */
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

