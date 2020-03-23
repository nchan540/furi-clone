package projectiles;

import graph.*;
import shapes.*;
import units.Unit;

import java.awt.Graphics;

public class Bomb extends Projectile {
    public Circle hitbox;
    public TargetBullet path;
    public int size;

    /**
     * Constructor for a bomb projectile
     * @param targets shapes that can be damaged
     * @param hitbox starting hitbox of the bomb
     * @param size explosion radius of the bomb
     * @param speed speed the bomb is throw at
     * @param direction direction bomb is thrown in
     * @param destination location bomb is thrown to
     */
    public Bomb(Unit[] targets, Circle hitbox, int size, float speed, Line direction, Point_ destination) {
        super(hitbox, targets, speed, direction);
        this.hitbox = hitbox;
        this.path = new TargetBullet(new Unit[]{}, hitbox, speed, direction, destination);
        this.size = size;
    }

    /** 
     * Updates the bomb
     * - Moves if not at destination
     * - Updates explosion if at destination
     * - Deletes if finished
     */
    public void update() {
        if (path.alive) {
            path.update();
        } else {
            if (this.hitbox.getRadius() >= this.size) {
                this.hitbox.increaseRadius(this.size - this.hitbox.getRadius());
            } else {
                this.hitbox.increaseRadius(5);
            }
        }
    }

    public void draw(Graphics g) {
        if (path.alive) path.draw(g);
        else {
            g.setColor(game.App.HITBOXCOLOURS[0]);
            constants.Display.drawCircle(g, this.hitbox);
        }
    }

    public boolean[] hitDetect() {
        if (targets.length == 0) return new boolean[] {false};
        boolean[] hits = new boolean[targets.length];
        for (int i = 0; i < targets.length; ++i) {
            if (targets[i] != null) hits[i] = this.targets[i].hitbox.checkOverlap(this.hitbox);
        }
        return hits;
    }

    public void resetAcceleration(){}
}