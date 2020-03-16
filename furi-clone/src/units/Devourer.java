package units;

import projectiles.*;
import graph.*;
import shapes.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Devourer extends Boss {

    public Attack curAttack;
    public int attackTimer = 0;
    public int attackDelay = 30;
    
    //always wandering; 0 = passive, 1 = performing an attack, 2 = some hitboxes are active
    public int phase = 0;
    public int wanderTimer = 30;
    //0 == nothing, 1 == shoot(), 2 == sweep();
    public int attack = 0;

    public int ID = 3;

    public ReturningBounce[] bullets = new ReturningBounce[constants.Devourer.MAXBULLETS];
    public int bulletIndex = 0;

    public Devourer(int x, int y, Player p) {
        super(constants.Devourer.HEALTH, 0, 1, x, y, 70, 1f, p);
        curAttack = new Attack();
        curAttack.hitboxes = new Shape[1];
    }

    public void update() {

        if (--wanderTimer == 0) {
            wanderTimer = 120;
            wander();
        }
        
        move();

        if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.diameter/2) {
            hitbox.p1.y = constants.Display.HEIGHT - hitbox.diameter/2; 
        }
        if (hitbox.p1.y < hitbox.diameter/2) {
            hitbox.p1.y = hitbox.diameter/2; 
        }
        if (hitbox.p1.x > constants.Display.WIDTH - hitbox.diameter/2) {
            hitbox.p1.x = constants.Display.WIDTH-hitbox.diameter/2; 
        }
        if (hitbox.p1.x < hitbox.diameter/2) {
            hitbox.p1.x = hitbox.diameter/2; 
        }

        if (phase == 0) {
            if (attackDelay > 0) --attackDelay;
            else attack();
        } else {
            if (attack == 1) {
                shoot();
            } else {
                sweep();
                if (attackTimer > 0 && attackTimer < 60) {
                    if(curAttack.checkHit(player.hitbox)) {
                        player.hit();
                    }
                }
            }
            if (--attackTimer == 0) {
                attackDelay = 60;
                phase = 0;
            } 
        }

        clearBullets();

        for (ReturningBounce b : bullets) {
            if (b != null) {
                b.move();
                b.update();
                b.changeTargets(new Shape[] {new Circle(player.hitbox.p1, (int)(player.hitbox.diameter / 2))});
                if(b.hitDetect()[0]) player.hit();
            }
        }

    }

    public void wander() {
        dir[0] = (float)(Math.random()-0.5)*5;
        dir[1] = (float)(Math.random()-0.5)*5;

        if (hitbox.p1.x > constants.Display.WIDTH - hitbox.diameter*3) dir[0] = -1.5f;
        if (hitbox.p1.x < hitbox.diameter*3) dir[0] = 1.5f;
        if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.diameter*3/2) dir[1] = -1.0f;
        if (hitbox.p1.y < hitbox.diameter*3/2) dir[1] = 1.0f;
    }

    public void attack () {
        phase = 1;
        if (bulletIndex == 0 && Math.round(Math.random()) == 0) attack = 1;
        else attack = 2;
    }

    public void shoot() {
        if (--attackTimer <= 0) {
            Point_ target = new Point_ (player.hitbox.p1.x, player.hitbox.p1.y);
            bullets[bulletIndex] = new ReturningBounce(new Shape[] {player.hitbox}, new Circle(hitbox.p1, constants.Add.BULLETSIZE + 5), constants.Add.BULLETSPEED, new Line(hitbox.p1, target), this);
            if (target.x < hitbox.p1.x) bullets[bulletIndex].setSpeed(-constants.Add.BULLETSPEED);
            if(++bulletIndex != constants.Devourer.MAXBULLETS) attackTimer = 10;
            else attackTimer = 1;
        }
    }

    public void sweep() {
        if (phase == 1) {
            attackTimer = 90;
            phase = 2;
            curAttack.hitboxes[0] = new Rectangle(this.hitbox.getLocation(), new Line(this.hitbox.getLocation(), new Point_(this.hitbox.getLocation().x+1, this.hitbox.getLocation().y-100)), 10, 2000);
        } else if (phase == 2 && attackTimer < 60) {
            curAttack.hitboxes[0].forDrawLaser().p2.x = hitbox.p1.x + (float)Math.sin((60-attackTimer-1)*Math.PI/29)*2000;
            curAttack.hitboxes[0].forDrawLaser().p2.y = hitbox.p1.y + (float)-Math.cos((60-attackTimer-1)*Math.PI/29)*2000;
            curAttack.hitboxes[0].refresh();
        }
        curAttack.hitboxes[0].forDrawLaser().p1 = hitbox.p1;
    }

    public void kill() {
        alive = false;
    }

    public void clearBullets() {
        for (int i = 0; i < bullets.length; ++i) {
            if (bullets[i] != null && bullets[i].returning() && hitbox.checkOverlap(bullets[i].hitbox)) {
                bullets[i] = null;
                --bulletIndex;
            }
        }
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {

        if (attack == 2) {
            if (attackTimer > 0) {
                if (attackTimer >= 60) {
                    g.setColor(HITBOXCOLOURS[3]);
                } else {
                    g.setColor(HITBOXCOLOURS[2]);
                }
                g2.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                constants.Display.drawLine(g2, curAttack.hitboxes[0].forDrawLaser());
            }
        }

        
        for (ReturningBounce b : bullets) {
            if (b != null) {
                if (b.returning()) g.setColor(Color.RED);
                else g.setColor(Color.PINK);
                
                constants.Display.drawCircle(g, b.hitbox);
            }
        }

        g.setColor(HITBOXCOLOURS[2]);
        constants.Display.drawCircle(g, this.hitbox);

    }

    public String toString() {
        return "The Devourer";
    }

}