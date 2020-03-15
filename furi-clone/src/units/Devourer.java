package units;

import projectiles.*;
import graph.*;
import shapes.*;
import constants.*;

public class Devourer extends Boss {

    public Attack curAttack;
    public int attackTimer;
    public int attackDelay;
    
    //always wandering; 0 = passive, 1 = performing an attack, 2 = some hitboxes are active
    public int phase = 0;
    //0 == nothing, 1 == shoot(), 2 == sweep();
    public int attack = 0;

    public ReturningBounce[] bullets = new ReturningBounce[12];
    public int bulletIndex = 0;

    public Devourer(int x, int y, Player p) {
        super(constants.Devourer.HEALTH, 0, 1, x, y, 70, 1f, p);
    }

    public void update() {

    }

    public void shoot() {
        
    }

    public void sweep() {
        if (phase == 0) {
            curAttack.hitboxes[0] = new Rectangle(this.hitbox.getLocation(), new Line(this.hitbox.getLocation(), new Point(this.hitbox.getLocation().x+1, this.hitbox.getLocation().y+100)), 10, 2000);
        } else if (phase == 2) {
            curAttack.hitboxes[0].forDrawLaser().p2.x = (float)Math.sin((60-attackTimer)*Math.PI/10);
            curAttack.hitboxes[0].forDrawLaser().p2.y = (float)Math.sin((60-attackTimer)*Math.PI/10);
            curAttack.hitboxes[0].refresh();
        }
    }

    public void kill() {
        
    }

    public void draw() {

    }
}