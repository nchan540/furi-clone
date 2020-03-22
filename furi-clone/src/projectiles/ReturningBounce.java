package projectiles;
import graph.*;
import shapes.*;
import units.Boss;

public class ReturningBounce extends Bounce {

    public Boss origin;
    public int returnTime = 0;
    public boolean returning = false;

    public ReturningBounce(Shape[] targets, Circle hitbox, float speed, Line direction, Boss origin) {
        super(targets, hitbox, speed, direction);
        this.origin = origin;
        acceleration = 1;
    }

    public void update() {
        if (returning) {
            if (returnTime == 0) {
                if (xSpeed == 0 && ySpeed == 0) setSpeed(0.5f, new Line(hitbox.p1, origin.hitbox.p1));
                else setSpeed((float)Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2)), new Line(hitbox.p1, origin.hitbox.p1));
                if (origin.hitbox.p1.x < hitbox.p1.x) setSpeed(-3f);
            }
            if (returnTime > 0) returnTime -= 1;
        }
        accelerate();
        if (xSpeed == 0 && ySpeed == 0 && !returning) {
            returnTime = 50;
            returning = true;
            acceleration = 1.01f;
            
        }
    }

    public boolean returning() {
        return returning;
    }
}