package projectiles;

import graph.*;
import shapes.*;
import units.Unit;

import java.awt.Graphics;

public abstract class Projectile {
    public Shape hitbox;
    public Unit[] targets;
    public float xSpeed;
    public float ySpeed;
    public Line direction;
    public int damage = 1;
    private boolean ally = false;

    public Projectile(Shape hitbox, Unit[] targets, float speed, Line direction) {
        this.hitbox = hitbox;
        this.targets = targets;
        
        this.setSpeed(speed, direction);
    }

    public void setSpeed(float speed, Line direction) {
        this.direction = direction;
        
        float deltaY = direction.getY(1) - direction.getY(0);
        float deltaX = 1;

        // 1 (deltaX) squared is 1, and deltaX will always be 1, so the value can be put it to improve speed
        // the ratio of speed : hypotenuse is the same as the ratio of xSpeed : deltaX and ySpeed : deltaY
        float ratio = speed / (float)(Math.abs(Math.sqrt(1 + Math.pow(deltaY, 2))));
        // speed / xSpeed = deltaX / ratio
        this.xSpeed = deltaX * ratio;
        this.ySpeed = deltaY * ratio;
    }

    public void setSpeed (float speed, Point_ direction) {
        float deltaY = direction.y - this.hitbox.getLocation().y;
        float deltaX = 1;

        // 1 (deltaX) squared is 1, and deltaX will always be 1, so the value can be put in to improve speed
        // the ratio of speed : hypotenuse is the same as the ratio of xSpeed : deltaX and ySpeed : deltaY
        float ratio = speed / (float)(Math.abs(Math.sqrt(1 + Math.pow(deltaY, 2))));
        // speed / xSpeed = deltaX / ratio
        this.xSpeed = deltaX * ratio;
        this.ySpeed = deltaY * ratio;
    }

    public void setSpeed(float speed) {
        float deltaY = direction.getY(1) - direction.getY(0);
        float deltaX = 1;

        // 1 (deltaX) squared is 1, and deltaX will always be 1, so the value can be put it to improve speed
        // the ratio of speed : hypotenuse is the same as the ratio of xSpeed : deltaX and ySpeed : deltaY
        float ratio = speed / (float)(Math.abs(Math.sqrt(1 + Math.pow(deltaY, 2))));
        // speed / xSpeed = deltaX / ratio
        this.xSpeed = deltaX * ratio;
        this.ySpeed = deltaY * ratio;
    }

    public void move() {
        this.hitbox.getLocation().x += xSpeed;
        this.hitbox.getLocation().y += ySpeed;
    }

    public void move(Point_ destination) {
        this.hitbox.getLocation().x = destination.x;
        this.hitbox.getLocation().y = destination.y;
    }

    public void changeTargets(Unit[] newTargets) {
        this.targets = newTargets;
    }


    public abstract void resetAcceleration();
    public abstract void update();
    public abstract void draw(Graphics g);
    public abstract boolean[] hitDetect();

    public boolean isAlly() {
        return ally;
    }

    public void deflect() {
        ally = true;
    }
}