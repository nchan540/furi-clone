package game;

import javax.swing.JPanel;
import java.awt.Point;
import java.util.ArrayList;

public abstract class UIPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    protected Point mouse;
    // Elements on the UIPanel
    public ArrayList<UIElementInteractable> elements = new ArrayList<UIElementInteractable>();

    /**
     * Assigns a mouse to the UIPanel
     * @param mouse Mouse being added
     */
    public UIPanel(Point mouse) {
        this.mouse = mouse;
    }

    public abstract void update();

    /**
     * Replaces the assigned mouse
     * @param mouse New mouse to be assigned
     */
    public void update(Point mouse) {
        this.mouse = mouse;
    }

    /**
     * Adds a UIElement to the UIPanel
     * @param u UIElement being added
     */
    public void addUIElement(UIElementInteractable u) {
        elements.add(u);
    }
}

