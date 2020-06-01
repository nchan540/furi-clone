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

    /**
     * Constructor for a projectile
     * @param hitbox Projectile hitbox
     * @param targets Targets to be damaged
     * @param speed Projectile speed
     * @param direction Projectile direction
     */
    public Projectile(Shape hitbox, Unit[] targets, float speed, Line direction) {
        this.hitbox = hitbox;
        this.targets = targets;
        
        this.setSpeed(speed, direction);
    }

    /**
     * Set the path of the projectile
     * @param speed New projectile speed
     * @param direction New projectile direction
     */
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

    /**
     * Set the path of the projectile
     * @param speed New projectile speed
     * @param direction A point the new projectile direction touches
     */
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

    /**
     * Sets the speed of the projectile
     * @param speed New projectile speed
     */
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

    /**
     * Moves the projectile
     */
    public void move() {
        this.hitbox.getLocation().x += xSpeed;
        this.hitbox.getLocation().y += ySpeed;
    }

    /**
     * Moves the projectile to a new location
     * @param destination New location for projectile
     */
    public void move(Point_ destination) {
        this.hitbox.getLocation().x = destination.x;
        this.hitbox.getLocation().y = destination.y;
    }

    /**
     * Changes the projectile's targets
     * @param newTargets New array of targets
     */
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