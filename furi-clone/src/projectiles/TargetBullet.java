package projectiles;

import graph.*;
import shapes.*;
import units.Unit;

import java.awt.Graphics;

public class TargetBullet extends Bullet {
    
    public Point_ destination;
    public boolean alive;

    /**
     * Constructor for a targetted bullet
     * @param targets Targets to be damaged
     * @param hitbox Bullet hitbox
     * @param speed Bullet speed
     * @param direction Bullet direction
     * @param destination Bullet end point
     */
    public TargetBullet(Unit[] targets, Circle hitbox, float speed, Line direction, Point_ destination) {
        super(targets, hitbox, speed, direction);
        this.destination = destination;
        this.alive = true;
    }

    /**
     * Detects if any of the targets have been hit
     * @return Array of hit detections for each target
     */
    @Override
    public boolean[] hitDetect() {
        if (this.alive) return super.hitDetect();
        return new boolean[targets.length];
    }

    /**
     * Draws the bullet
     * @param g Graphics for bullet to be drawn on
     */
    @Override
    public void draw(Graphics g) {
        if (this.alive) super.draw(g);
    }

    /**
     * Updates bullet
     */
    @Override
    public void update() {
        this.move();
        
        if (this.hitbox.getLocation().equals(this.destination)) alive = false;
    }
}