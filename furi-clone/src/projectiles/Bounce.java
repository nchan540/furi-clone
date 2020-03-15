package projectiles;
import graph.*;
import shapes.*;

public class Bounce extends Projectile {

    public float acceleration;
    public Circle hitbox;

    public Bounce(Shape[] targets, Circle hitbox, float speed, Line direction) {
        super(hitbox, targets, speed, direction);
        acceleration = 1;
    }

    public void accelerate() {
        xSpeed *= acceleration;
        ySpeed *= acceleration;
        if (xSpeed < 0.1) xSpeed = 0;
        if (ySpeed < 0.1) ySpeed = 0;
    }

    public boolean[] hitDetect() {
        if (targets.length == 0) return new boolean[] {false};
        boolean[] hits = new boolean[targets.length];
        for (int i = 0; i < targets.length; ++i) {
            hits[i] = this.targets[i].checkOverlap(this.hitbox);
        }
        return hits;
    }

    @Override
    public void move () {
        this.hitbox.getLocation().x += xSpeed;
        this.hitbox.getLocation().y += ySpeed;

        if (hitbox.getLocation().y > constants.Display.HEIGHT - hitbox.getRadius()/2) {
            hitbox.getLocation().y = constants.Display.HEIGHT - hitbox.getRadius()/2; 
            ySpeed = -ySpeed;
            acceleration -= (1 - acceleration + 0.01);
        }
        if (hitbox.getLocation().y < hitbox.getRadius()/2) {
            hitbox.getLocation().y = hitbox.getRadius()/2; 
            ySpeed = -ySpeed;
            acceleration -= (1 - acceleration + 0.01);
        }
        if (hitbox.getLocation().x > constants.Display.WIDTH - hitbox.getRadius()/2) {
            hitbox.getLocation().x = constants.Display.WIDTH-hitbox.getRadius()/2; 
            xSpeed = -xSpeed;
            acceleration -= (1 - acceleration + 0.01);
        }
        if (hitbox.getLocation().x < hitbox.getRadius()/2) {
            hitbox.getLocation().x = hitbox.getRadius()/2; 
            xSpeed = -xSpeed;
            acceleration -= (1 - acceleration + 0.01);
        }
    }
}