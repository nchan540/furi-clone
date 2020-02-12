package app;
import java.awt.Font;

public class Constants {
    public class Player {
        public static final float SPEED = 10;
        public static final int HEALTH = 3;
        public static final int DAMAGE = 10000;
        public static final int DASH_DISTANCE = 250;
    }

    
        public static final Font FONT = new Font("Arial", Font.ITALIC, 30);

    public class Add {
        public static final int HEALTH = 1;
    }

    public class Graphics {
        public static final int HEIGHT = 800;
        public static final int WIDTH = 1300;
    }

    public static float distanceFormula(Point p1, Point p2) {
        return (float)(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }
}