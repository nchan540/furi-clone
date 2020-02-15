package units;
import graph.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public abstract class Unit {
    
    public int maxHp;
    public int hp;
    public int dmg;
    public Point_ location;
    public float radius;
    public float spd;
    public int ID;

    public Unit(int maxHp, int hp, int dmg, int x, int y, int radius, float spd) {
        this.maxHp = maxHp;
        this.hp = hp;
        this.dmg = dmg;
        this.location = new Point_(x, y);
        this.radius = radius;
        this.spd = spd;
    }

    public Unit(int maxHp, int hp, int dmg, Point_ spawn, int radius, float spd) {
        this.maxHp = maxHp;
        this.hp = hp;
        this.dmg = dmg;
        this.location = new Point_(spawn.x, spawn.y);
        this.radius = radius;
        this.spd = spd;
    }

    public int getX() { return Math.round(this.location.x); }

    public int getY() { return Math.round(this.location.y); }

    public int getRadius() { return Math.round(this.radius); }

    public int getMaxHP() { return this.maxHp; }
    
    public int getHP() { return this.hp; }

    public int getDmg() { return this.dmg; }

    public float getSpeed() { return this.spd; }

    public abstract void update();

    /**
     * Moves the unit by its speed value
     * @param moveX -1 if moving left, 0 if not moving, 1 if moving right.
     * @param moveY -1 if moving up, 0 if not moving, 1 if moving down.
     * @return if move was successful (parameters within bounds)
     */
    public boolean move(int moveX, int moveY) {
        if (moveX < -1 || moveX > 1 || moveY < -1 || moveY > 1) return false;
        
        float moveSpd = this.spd;
        if (moveX != 0 && moveY != 0) {
            moveSpd = (float)Math.pow(Math.pow(this.spd, 2) / 2, 0.5);
        }

        this.location.x += moveSpd * moveX;
        this.location.y += moveSpd * moveY;
        return true;
    }

    /**
     * @param damage value being subtracted from hp 
     * @return true if piece has positive hp, false if piece has 0 or negative hp
     */
    public boolean takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp > this.maxHp) this.hp = this.maxHp;
        return this.hp > 0;
    }

    public abstract void draw(Graphics g, Graphics2D g2D, Color[] HITBOXCOLOURS);
    public abstract void kill();
}