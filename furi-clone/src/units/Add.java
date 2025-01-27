package units;
import graph.Line;
import projectiles.*;
import shapes.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

public class Add extends Unit {
    
    // Unit to fire bullets at
    public Unit target;
    // Targets bullets can hit
    public Shape[] bulletTarget;
    public Bullet[] bullets = new Bullet[constants.Add.MAXBULLETS];
    // Index in bullets
    public int bulletAvailable;
    // Direction for movement
    public float dir[] = new float[]{0, 0};
    // Delay between firing bullets
    public int attackDelay;
    public boolean alive;

    public int wanderTimer = 1;

    public Add(Unit target, int dmg, int x, int y, int radius, float spd) {
        // Ads die in one hit
        super(constants.Add.HEALTH, constants.Add.HEALTH, dmg, x, y, radius, spd);
        this.ID = constants.Add.ID;
        alive = true;
        this.target = target;
        this.bulletAvailable = 0;
        this.attackDelay = constants.Add.ATTACKDELAY;
    }

    public Projectile[] getBullets() {
        return bullets;
    }

    /**
     * Shoots a bullet if possible
     */
    public void attack() {
        if (this.attackDelay == 0) {
            this.attackDelay = constants.Add.ATTACKDELAY;
            bullets[bulletAvailable] = new Bullet(new Unit[] {target}, new Circle(this.hitbox.p1, constants.Add.BULLETSIZE), constants.Add.BULLETSPEED, new Line(this.hitbox.p1, target.hitbox.p1));
            if (target.hitbox.p1.x < this.hitbox.p1.x) bullets[bulletAvailable].setSpeed(-constants.Add.BULLETSPEED);
            if (++bulletAvailable >= 10) bulletAvailable = 0;
        }
        --attackDelay;
    }

    /**
     * Removes all the bullets that the ad has fired
     */
    public void clearBullets() {
        for (Bullet b : this.bullets) {
            if (b != null
                && 0 > b.hitbox.p1.x + b.hitbox.diameter || constants.Display.WIDTH < b.hitbox.p1.x - b.hitbox.diameter
                || 0 > b.hitbox.p1.y + b.hitbox.diameter || constants.Display.HEIGHT < b.hitbox.p1.y - b.hitbox.diameter) b = null;
        }
    }

    public void move() {
        this.hitbox.p1.x += dir[0]*spd;
        this.hitbox.p1.y += dir[1]*spd;
    }

    /**
     * Makes the ad wander randomly
     */
    public void wander() {
        dir[0] = (float)(Math.random()-0.5)*5;
        dir[1] = (float)(Math.random()-0.5)*5;

        if (hitbox.p1.x > constants.Display.WIDTH - hitbox.diameter*3) dir[0] = -1.5f;
        if (hitbox.p1.x < hitbox.diameter*3) dir[0] = 1.5f;
        if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.diameter*3/2) dir[1] = -1.0f;
        if (hitbox.p1.y < hitbox.diameter*3/2) dir[1] = 1.0f;
    }

    /**
     * Updates bullets and movement, checks if ad is still alive
     */
    public void update() {
        if (this.alive) {
            if (--wanderTimer == 0) {
                wanderTimer = 120;
                wander();
            }
            this.move();
            this.attack();
        }

        for (Bullet b : this.bullets) {
            if (b != null) b.update();
        }

        if (this.alive && this.hp <= 0) {this.kill();}
    }

    /**
     * "Kills" the ad
     */
    public void kill() {
        this.alive = false;
        for (int i = 0; i < bullets.length; ++i) {
            bullets[i] = null;
        }
    }

    public void draw(Graphics g, Graphics2D g2D, Color[] HITBOXCOLOURS) {
        if (this.alive) {
            g.setColor(Color.WHITE);
            if (attackDelay < 40) g.setColor(Color.RED);
            constants.Display.drawCircle(g, this.hitbox);
            g.setColor(Color.PINK);
            for (Bullet b : this.bullets) {
                if (b != null) {
                    b.draw(g);
                }
            }
        }
    }
}