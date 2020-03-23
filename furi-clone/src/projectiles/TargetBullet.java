package projectiles;

import graph.*;
import shapes.*;

import java.awt.Graphics;

public class TargetBullet extends Bullet {
    
    public Point_ destination;
    public boolean alive;

    public TargetBullet(Shape[] targets, Circle hitbox, float speed, Line direction, Point_ destination) {
        super(targets, hitbox, speed, direction);
        this.destination = destination;
        this.alive = true;
    }

    @Override
    public boolean[] hitDetect() {
        if (this.alive) return super.hitDetect();
        return new boolean[targets.length];
    }

    @Override
    public void draw(Graphics g) {
        if (this.alive) super.draw(g);
    }

    @Override
    public void update() {
        this.move();
        
        if (this.hitbox.getLocation().equals(this.destination)) alive = false;
    }
}