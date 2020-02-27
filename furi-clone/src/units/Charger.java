package units;
import shapes.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

public class Charger extends Boss {

    public Attack curAttack;
    public int speedUp = 150;
    public int bonked = 0;
    public int bonkTimer = 0;

    public Charger(int x, int y, Player p) {
        super(constants.Charger.HEALTH, 0, 1, x, y, 100, 1f, p);
        alive = true;
        ID = 0;
    }

    public void update() {
        changeDir();
        move();

        if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.diameter/2) {
            hitbox.p1.y = constants.Display.HEIGHT - hitbox.diameter/2; 
            dir[1]  = -dir[1] * constants.Charger.STUNSPD; 
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = constants.Charger.STUNSPD; 
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (hitbox.p1.y < hitbox.diameter/2) {
            hitbox.p1.y = hitbox.diameter/2; 
            dir[1]  = -dir[1] * constants.Charger.STUNSPD; 
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = constants.Charger.STUNSPD; 
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (hitbox.p1.x > constants.Display.WIDTH - hitbox.diameter/2) {
            hitbox.p1.x = constants.Display.WIDTH-hitbox.diameter/2; 
            dir[0] = -dir[0] * constants.Charger.STUNSPD;
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = constants.Charger.STUNSPD; 
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (hitbox.p1.x < hitbox.diameter/2) {
            hitbox.p1.x = hitbox.diameter/2; 
            dir[0] = -dir[0] * constants.Charger.STUNSPD; 
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = constants.Charger.STUNSPD; 
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (--speedUp <= 0 && this.spd <= 1.8f) {this.spd += constants.Charger.SPEEDUP; speedUp = 150;}

        if (bonkTimer == 60) {
            this.bonked = constants.Charger.STUNTIME + (int)(Math.sqrt(Math.pow(dir[0], 2) + Math.pow(dir[1], 2)) * 20 * spd);
            System.out.println(bonked);
        }

        if (this.bonked > 0) {
            --bonked;
        } else if (spd == constants.Charger.STUNSPD && this.bonked == 0){
            spd = 1f;
        }
        if (bonkTimer > 0) {
            --bonkTimer;
        }
    }

    public void changeDir() {
        if (bonked == 0){
            if (!(Math.abs(player.getX() - this.getX()) < 3 && Math.abs(player.getY()-this.getY()) < 3)) {
            float difX = (player.hitbox.p1.x-this.hitbox.p1.x);
            float difY = (player.hitbox.p1.y-this.hitbox.p1.y);

            float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
            float[] changeSpeed = new float[]{(difX*spd/r), difY*spd/r};
            dir[0] += changeSpeed[0]*(0.35 - (spd/15)) ;
            dir[1] += changeSpeed[1]*(0.35 - (spd/15));

            dir[0] *= 0.97;
            dir[1] *= 0.97;
            }
        }
    }

    public String toString() {
        return "The Charger";
    }

    public void draw(Graphics g, Graphics2D g2D, Color[] HITBOXCOLOURS) {
        g.setColor(HITBOXCOLOURS[2]);
        if (bonked > 0) g.setColor(HITBOXCOLOURS[4]);
        constants.Display.drawCircle(g, this.hitbox);
    }

    public void kill() {
        alive = false;
    }

    @Override 
    public boolean takeDamage(int damage) {
        this.hp -= damage + (bonked / 3);
        if (this.hp > this.maxHp) this.hp = this.maxHp;
        return this.hp > 0;
    }
}