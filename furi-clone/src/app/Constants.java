package app;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Constants {
    public class Player {
        public static final float SPEED = 10;
        public static final int HEALTH = 3;
        public static final int DAMAGE = 20;
        public static final int DASH_DISTANCE = 250;
        public static final int ATTACK_WIDTH = 250;
        public static final int ATTACK_FRAMES = 3;
    }

    
        public static final Font FONT = new Font("Arial", Font.ITALIC, 30);

    public class Add {
        public static final int HEALTH = 1;
    }

    public class Charger {
        public static final int HEALTH = 300;
        public static final int STUNTIME = 30;
        public static final float STUNSPD = 0.4f;
        public static final float SPEEDUP = 0.1f;
    }

    public class Brawler {
        public static final int HEALTH = 500;
    }

    public class Graphics {
        public static final int HEIGHT = 800;
        public static final int WIDTH = 1440;
    }

    public static void drawLine(Graphics2D g, LineSegment l) {
        g.draw(new Line2D.Float(l.p1.x, l.p1.y, l.p2.x, l.p2.y));
    }

    public static void drawLine(java.awt.Graphics g, LineSegment l) {
        g.drawLine((int)(l.p1.x), (int)(l.p1.y), (int)(l.p2.x), (int)(l.p2.y));
    }

    public static void drawCircle(java.awt.Graphics g, Circle c) {
        g.fillOval(Math.round(c.p1.x - c.diameter/2), Math.round(c.p1.y - c.diameter/2), c.diameter, c.diameter);
    }
    public static float distanceFormula(Point_ p1, Point_ p2) {
        return (float)(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }
}