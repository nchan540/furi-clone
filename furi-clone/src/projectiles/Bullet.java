package projectiles;
import graph.*;
import shapes.*;

public class Bullet extends Projectile {
    public Bullet(float x, float y, Line direction) {
        super(new Circle(x, y, 10), 10, direction);
    }

    public boolean hitDetect(Shape s) {
        return false;
    }
}