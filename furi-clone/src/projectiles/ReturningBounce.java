package projectiles;
import graph.*;
import shapes.*;

public class ReturningBounce extends Bounce {

    public Point_ origin;

    public ReturningBounce(Shape[] targets, Circle hitbox, float speed, Line direction, Point_ origin) {
        super(targets, hitbox, speed, direction);
        this.origin = origin;
    }

    public void update() {
        this.setSpeed((float)Math.sqrt(Math.pow(ySpeed, 2) + Math.pow(xSpeed, 2)), origin);
        accelerate();
    }

    public void returning() {
        this.setSpeed(0.5f, origin);
        acceleration = 1.02f;
    }
}