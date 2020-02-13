package app;
import java.awt.Font;

public class Constants {
    public class Player {
        public static final float SPEED = 10;
        public static final int HEALTH = 3;
        public static final int DAMAGE = 20;
        public static final int DASH_DISTANCE = 250;
        public static final int ATTACK_WIDTH = 250;
    }

    
        public static final Font FONT = new Font("Arial", Font.ITALIC, 30);

    public class Add {
        public static final int HEALTH = 1;
    }

    public class Charger {
        public static final int STUNTIME = 30;
        public static final float STUNSPD = 0.4f;
        public static final float SPEEDUP = 0.1f;
    }

    public class Graphics {
        public static final int HEIGHT = 800;
        public static final int WIDTH = 1440;
    }

    public static float distanceFormula(Point_ p1, Point_ p2) {
        return (float)(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }
}