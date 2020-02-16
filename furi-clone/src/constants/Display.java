package constants;
import shapes.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Display {

    public static float DISPLAYRATIO = 5.0f/9.0f;
    public static int HEIGHT;
    public static int WIDTH;

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
}