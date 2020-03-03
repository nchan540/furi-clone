package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class UIElementInteractable {

    

    public UIElementInteractable () {

    }

    public abstract void draw(Graphics g, Graphics2D g2);
    public abstract void click();
    public abstract void checkPos(Point p);

    //implemented for button, not for switch
    public abstract boolean getClicked();
}