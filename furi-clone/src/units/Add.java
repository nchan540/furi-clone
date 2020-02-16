package units;
import graph.Line;
import projectiles.*;
import shapes.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

public class Add extends Unit {
    
    public Unit target;
    public Shape[] bulletTarget;
    public Bullet[] bullets = new Bullet[constants.Add.MAXBULLETS];
    public int bulletAvailable;
    public float dir[] = new float[]{0, 0};
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

    public void attack() {
        if (this.attackDelay == 0) {
            this.attackDelay = constants.Add.ATTACKDELAY;
            bullets[bulletAvailable] = new Bullet(new Shape[] {new Circle(target.location, (int)(target.radius / 2))}, 
                                        new Circle(this.location, Math.round(constants.Display.HEIGHT/constants.Add.BULLETSIZERATIO)),
                                        constants.Display.HEIGHT / constants.Add.BULLETSPEEDRATIO, new Line(this.location, target.location));
            if (target.location.x < this.location.x) bullets[bulletAvailable].setSpeed(-constants.Display.HEIGHT / constants.Add.BULLETSPEEDRATIO);
            if (++bulletAvailable >= constants.Add.MAXBULLETS) bulletAvailable = 0;
        }
        --attackDelay;
    }

    public void clearBullets() {
        for (Bullet b : this.bullets) {
            if (b != null
                && 0 > b.hitbox.p1.x + b.hitbox.diameter || constants.Display.WIDTH < b.hitbox.p1.x - b.hitbox.diameter
                || 0 > b.hitbox.p1.y + b.hitbox.diameter || constants.Display.HEIGHT < b.hitbox.p1.y - b.hitbox.diameter) b = null;
        }
    }

    public void move() {
        this.location.x += dir[0]*spd;
        this.location.y += dir[1]*spd;
    }

    public void wander() {
        dir[0] = (float)(Math.random()-0.5)*5;
        dir[1] = (float)(Math.random()-0.5)*5;

        if (location.x > constants.Display.WIDTH - radius*3) dir[0] = -1.5f;
        if (location.x < radius*3) dir[0] = 1.5f;
        if (location.y > constants.Display.HEIGHT - radius*3/2) dir[1] = -1.0f;
        if (location.y < radius*3/2) dir[1] = 1.0f;
    }

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
            if (b != null) b.move();
        }

        if (this.alive && this.hp <= 0) this.kill();
    }

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
            constants.Display.drawCircle(g, new Circle(this.location, this.getRadius()));
            g.setColor(Color.PINK);
            for (Bullet b : this.bullets) {
                if (b != null) {
                    constants.Display.drawCircle(g, b.hitbox);
                }
            }
        }
    }
}