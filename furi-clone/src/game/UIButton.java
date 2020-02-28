//this UIElement is meant for anything that should be pressed once
// ex. menu options
// ex. 
package game;
import java.awt.Point;
import shapes.*;
import constants.*;
import graph.*;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class UIButton extends UIElementInteractable {
    
    //location and area
    private Shape button;
    private Point_ imgLocation;

    //0 is idle, 1 is hovered, 2 is pressed
    private BufferedImage[] stages = new BufferedImage[3];
    private int stage = 0;
    
    public UIButton(BufferedImage[] load, Shape area) {
        for (int i = 0; i < 3; ++i) {
            stages[i] = load[i];
        }
        button = area;
        if (area instanceof Circle)imgLocation = new Point_ (area.getLocation().x - area.getRadius(), area.getLocation().y - area.getRadius());
        else imgLocation = new Point_ (area.getLines()[0].p1.x + 10, area.getLines()[0].p1.y + 10);
    }

    public void checkPos(Point p) {
        if (button instanceof Rectangle) {
            if (button.checkBounds(p)) stage = 1;
            else stage = 0;
        } else {
            if (Point_.distanceFormula(p, button.getLocation()) < button.getRadius()) stage = 1;
            else stage = 0;
        }
    }

    public void click() {
        if (stage == 1) stage = 2;
    }

    public int getStage() {
        return stage;
    }

    public void draw(Graphics g, Graphics2D g2) {
        g.setColor(Color.BLACK);
        constants.Display.drawShape(g, g2, button);
        g.drawImage(stages[stage], (int)imgLocation.x, (int)imgLocation.y, null);
    }

}