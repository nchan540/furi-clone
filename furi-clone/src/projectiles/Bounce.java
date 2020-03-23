package projectiles;

import java.awt.Graphics;

import graph.*;
import shapes.*;
import units.Unit;

public class Bounce extends Projectile {

    public float acceleration = 1;
    public Circle hitbox;

    public Bounce(Unit[] targets, Circle hitbox, float speed, Line direction) {
        super(hitbox, targets, speed, direction);
        this.hitbox = hitbox;
    }

    public void accelerate() {
        xSpeed *= acceleration;
        ySpeed *= acceleration;
        if (Math.abs(xSpeed) < 0.1)
            xSpeed = 0;
        if (Math.abs(ySpeed) < 0.1)
            ySpeed = 0;
    }

    public boolean[] hitDetect() {
        if (targets.length == 0)
            return new boolean[] { false };
        boolean[] hits = new boolean[targets.length];
        for (int i = 0; i < targets.length; ++i) {
            if (targets[i] != null) hits[i] = this.targets[i].hitbox.checkOverlap(this.hitbox);
        }
        return hits;
    }

    public void resetAcceleration() {
        acceleration = 1;
    }

    @Override
    public void move() {
        if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.getRadius() / 2) {
            hitbox.p1.y = constants.Display.HEIGHT - hitbox.getRadius() / 2;
            ySpeed = -ySpeed;
            acceleration -= 0.004;
        }
        if (hitbox.p1.y < hitbox.getRadius() / 2) {
            hitbox.p1.y = hitbox.getRadius() / 2;
            ySpeed = -ySpeed;
            acceleration -= 0.004;
        }
        if (hitbox.p1.x > constants.Display.WIDTH - hitbox.getRadius() / 2) {
            hitbox.p1.x = constants.Display.WIDTH - hitbox.getRadius() / 2;
            xSpeed = -xSpeed;
            acceleration -= 0.004;
        }
        if (hitbox.p1.x < hitbox.getRadius() / 2) {
            hitbox.p1.x = hitbox.getRadius() / 2;
            xSpeed = -xSpeed;
            acceleration -= 0.004;
        }
        this.hitbox.p1.x += xSpeed;
        this.hitbox.p1.y += ySpeed;
    }

    public void update() {
    }

    public void draw(Graphics g) {
    }
}