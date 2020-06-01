package projectiles;

import graph.*;
import shapes.*;
import units.Unit;

import java.awt.Graphics;

public class Bullet extends Projectile {

    public Circle hitbox;

    /**
     * Constructor for a bullet
     * @param targets Targets to be damaged
     * @param hitbox Bullet hitbox
     * @param speed Bullet speed
     * @param direction Bullet direction
     */
    public Bullet(Unit[] targets, Circle hitbox, float speed, Line direction) {
        super(hitbox, targets, speed, direction);
        this.hitbox = hitbox;
    }

    /**
     * Updates bullet
     */
    public void update() {
        this.move();
    }

    /**
     * Detects if any of the targets have been hit
     * @return Array of hit detections for each target
     */
    public boolean[] hitDetect() {
        if (targets.length == 0) return new boolean[] {false};
        boolean[] hits = new boolean[targets.length];
        for (int i = 0; i < targets.length; ++i) {
            if (targets[i] != null) hits[i] = this.targets[i].hitbox.checkOverlap(this.hitbox);
        }
        return hits;
    }

    public void resetAcceleration(){}

    /**
     * Draws the bullet
     * @param g Graphics for bullet to be drawn on
     */
    public void draw(Graphics g) {
        constants.Display.drawCircle(g, this.hitbox);
    }
}