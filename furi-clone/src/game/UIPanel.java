package game;

import javax.swing.JPanel;
import java.awt.Point;
import java.util.ArrayList;

public abstract class UIPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    protected Point mouse;
    public ArrayList<UIElementInteractable> elements = new ArrayList<UIElementInteractable>();

    public UIPanel(Point mouse) {
        this.mouse = mouse;
    }

    public abstract void update();

    public void update(Point mouse) {
        this.mouse = mouse;
    }

    public void addUIElement(UIElementInteractable u) {
        elements.add(u);
    }
}

