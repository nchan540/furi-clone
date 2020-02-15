package projectiles;
import graph.*;
import shapes.*;

public class Bullet extends Projectile {

    public Circle hitbox;

    public Bullet(Shape[] targets, Circle hitbox, float speed, Line direction) {
        super(hitbox, targets, speed, direction);
        this.hitbox = hitbox;
    }

    public boolean[] hitDetect() {
        if (targets.length == 0) return new boolean[] {false};
        boolean[] hits = new boolean[targets.length];
        for (int i = 0; i < targets.length; ++i) {
            hits[i] = this.targets[i].checkOverlap(this.hitbox);
        }
        return hits;
    }
}