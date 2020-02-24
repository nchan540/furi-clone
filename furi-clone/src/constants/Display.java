package constants;
import shapes.*;
import graph.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Display {
    public static final int HEIGHT = 800;
    public static final int WIDTH = 1440;

    public static final Font FONT = new Font("Arial", Font.ITALIC, 30);

    public static void drawLine(Graphics2D g, LineSegment l) {
        g.draw(new Line2D.Float(l.p1.x, l.p1.y, l.p2.x, l.p2.y));
    }

    public static void drawLine(Graphics g, LineSegment l) {
        g.drawLine((int)(l.p1.x), (int)(l.p1.y), (int)(l.p2.x), (int)(l.p2.y));
    }

    public static void drawCircle(Graphics g, Circle c) {
        g.fillOval(Math.round(c.p1.x - c.diameter/2), Math.round(c.p1.y - c.diameter/2), c.diameter, c.diameter);
    }

    public static void drawCircle(Graphics g, Point_ l, int r) {
        g.fillOval(Math.round(l.x - r/2), Math.round(l.y - r/2), Math.round(l.x + r/2), Math.round(l.y + r/2));
    }

    public static void drawShape(Graphics g, Graphics2D g2, Shape s) {
        if(s instanceof Circle) {
            drawCircle(g, s.getLocation(), s.getRadius());
        } else if (s instanceof Rectangle) {
            for (LineSegment l : s.getLines()) {
                drawLine(g2, l);
            }
        }
    }
}