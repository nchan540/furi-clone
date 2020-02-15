package constants;
import graph.*;
import shapes.*;

import java.awt.Font;
import java.awt.Color;
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

    public static void drawCircle(java.awt.Graphics g, Circle c) {
        g.fillOval(Math.round(c.p1.x - c.diameter/2), Math.round(c.p1.y - c.diameter/2), c.diameter, c.diameter);
    }
}