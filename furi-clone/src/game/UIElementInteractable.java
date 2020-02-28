package game;

import shapes.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class UIElementInteractable {

    

    public UIElementInteractable () {

    }

    public abstract void draw(Graphics g, Graphics2D g2);
    public abstract void click();
    public abstract void checkPos(Point p);
    public abstract int getStage();
}