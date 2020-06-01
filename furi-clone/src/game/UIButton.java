//this UIElement is meant for anything that should be pressed once
// ex. menu options
// ex. 
package game;
import java.awt.Point;
import shapes.*;
import graph.*;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Graphics;

public class UIButton extends UIElementInteractable {
    
    //location and area
    private Shape button;
    private Point_ imgLocation;

    //button properties
    private boolean toggleable, locked = false;

    //0 is idle, 1 is hovered, 2 is pressed
    private BufferedImage[] stages = new BufferedImage[3];
    private BufferedImage lockImg;
    private boolean hovered, toggled = false;
    
    /**
     * Creates a button with a specified image
     * @param load Button image
     * @param area Button shape
     */
    public UIButton(BufferedImage[] load, Shape area) {
        for (int i = 0; i < 3; ++i) {
            stages[i] = load[i];
        }
        button = area;
        if (area instanceof Circle)imgLocation = new Point_ (area.getLocation().x - area.getRadius(), area.getLocation().y - area.getRadius());
        else imgLocation = new Point_ (area.getLines()[0].p1.x + 10, area.getLines()[0].p1.y + 10);
    }

    /**
     * Creates a toggleable button with a specified image
     * @param load Button image
     * @param area Button shape
     * @param toggleable True for toggleable, false for untoggleable
     */
    public UIButton(BufferedImage[] load, Shape area, boolean toggleable) {
        for (int i = 0; i < 3; ++i) {
            stages[i] = load[i];
        }
        button = area;
        this.toggleable = toggleable;
        if (area instanceof Circle)imgLocation = new Point_ (area.getLocation().x - area.getRadius(), area.getLocation().y - area.getRadius());
        else imgLocation = new Point_ (area.getLines()[0].p1.x + 10, area.getLines()[0].p1.y + 10);
    }

    /**
     * Creates a locked button with a specified image
     * @param load Button image
     * @param area Button shape
     * @param toggleable True for toggleable, false for untoggleable
     * @param locked True for locked, false for unlocked
     * @param lockImg Button image when locked
     */
    public UIButton(BufferedImage[] load, Shape area, boolean toggleable, boolean locked, BufferedImage lockImg) {
        for (int i = 0; i < 3; ++i) {
            stages[i] = load[i];
        }
        this.locked = locked;
        this.lockImg = lockImg;
        button = area;
        this.toggleable = toggleable;
        if (area instanceof Circle)imgLocation = new Point_ (area.getLocation().x - area.getRadius(), area.getLocation().y - area.getRadius());
        else imgLocation = new Point_ (area.getLines()[0].p1.x + 10, area.getLines()[0].p1.y + 10);
    }

    /**
     * Updates the hovered state of the button
     */
    public void checkPos(Point p) {
        if (locked) hovered = false;
        if (button instanceof Rectangle) {
            if (button.checkBounds(p)) hovered = true;
            else hovered = false;
        } else {
            if (Point_.distanceFormula(p, button.getLocation()) < button.getRadius()) hovered = true;
            else hovered = false;
        }
    }

    /**
     * If hovered, toggles button if untoggled and untoggles if toggled
     */
    public void click() {
        if (hovered) toggled = !toggled;
    }

    /**
     * Checks if button was clicked
     * @return True if clicked, false if not
     */
    public boolean getClicked() {
        if (!locked & toggled && !toggleable) {
            toggled = false;
            return true;
        }
        return toggled;
    }

    /**
     * Draws button
     * @param g Graphics to draw button on
     * @param g2 Graphics2D to draw button on
     */
    public void draw(Graphics g, Graphics2D g2) {
        g.setColor(Color.BLACK);
        constants.Display.drawShape(g, g2, button);
        if (locked) {
            g.drawImage(lockImg, (int)imgLocation.x, (int)imgLocation.y, null);
        } else if (toggled) {
            g.drawImage(stages[2], (int)imgLocation.x, (int)imgLocation.y, null);
        } else if(hovered) {
            g.drawImage(stages[1], (int)imgLocation.x, (int)imgLocation.y, null);
        } else {
            g.drawImage(stages[0], (int)imgLocation.x, (int)imgLocation.y, null);
        }
    }

}