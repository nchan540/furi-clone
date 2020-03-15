package units;
import graph.*;
import shapes.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Brawler extends Boss {

    public Attack curAttack = new Attack();
    public int NUM = 1;
    public int wanderTimer = 150;
    public int chaseTime, stunTime, attackTime, attack, blinkAnim = 0;
    public boolean wandering, chasing, attacking, hitPlayer, blink = false;
    public Point_ blinkedFrom;

    public Brawler(int x, int y, Player p) {
        super(constants.Brawler.HEALTH, 0, 1, x, y, 125, 0.5f, p);
        alive = true;
        ID = 1;
    }

    public void update() {
        if (stunTime == 0) {
            move();
            wander();
            if (!chasing && !attacking) chase();
            if (chasing) chasing();
            if (attacking) attack();
        } else {
            --stunTime;
            --blinkAnim;
            if (--attackTime == 10) {
                if (attack == 1) this.hitbox.p1 = curAttack.hitboxes[0].getLocation();
            }
            if (attackTime > 0 && attackTime <= 10) {
                if(curAttack.checkHit(new Circle(player.hitbox.p1, player.getRadius())) && !hitPlayer) {
                    player.hit();
                    if (!hitPlayer && blink) {player.takeDamage(1);}
                    hitPlayer = true;
                }
            }
            if (attackTime == 0) {
                blink = false;
            }
        }

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
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {
        if (attackTime > 0) {
            if (attackTime > 10) g.setColor(HITBOXCOLOURS[3]);
            else g.setColor(HITBOXCOLOURS[2]);
            
            if (!(attack == 3)) constants.Display.drawCircle(g, new Circle(curAttack.hitboxes[0].getLocation(), curAttack.hitboxes[0].getRadius()));
            else {
                for (LineSegment l : curAttack.hitboxes[0].getLines()) {
                    g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                    constants.Display.drawLine(g2, l);
                }
                constants.Display.drawCircle(g2, new Circle(curAttack.hitboxes[1].getLocation(), curAttack.hitboxes[1].getRadius()));
                constants.Display.drawCircle(g2, new Circle(curAttack.hitboxes[2].getLocation(), curAttack.hitboxes[2].getRadius()));
            }
        }
        if (blinkAnim > 0 && blinkAnim <= 6 ) {
            g.setColor(Color.WHITE);
            constants.Display.drawCircle(g, new Circle(blinkedFrom, getRadius() * 2 / (7 - blinkAnim)));
        }
        g.setColor(HITBOXCOLOURS[2]);
        if (stunTime > 0 && attackTime == 0) g.setColor(HITBOXCOLOURS[5]);
        constants.Display.drawCircle(g, this.hitbox);
    }

    public void wander() {
        if (wanderTimer == 0) {
            if (wandering) {
                dir[0] = 0;
                dir[1] = 0;
                wanderTimer = 60;
            } else {
                dir[0] = (float)(Math.random()-0.5)*5;
                dir[1] = (float)(Math.random()-0.5)*5;

                if (hitbox.p1.x > constants.Display.WIDTH - hitbox.diameter*3) dir[0] = -1.5f;
                if (hitbox.p1.x < hitbox.diameter*3) dir[0] = 1.5f;
                if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.diameter*3/2) dir[1] = -1.0f;
                if (hitbox.p1.y < hitbox.diameter*3) dir[1] = 1.0f;

                wanderTimer = 150;
            }
            wandering = !wandering;
        }
        --wanderTimer;
    }

    public void chase() {
        if (Point_.distanceFormula(hitbox.p1, player.hitbox.p1) < (hitbox.diameter * 5)) {
            chasing = true;
            wanderTimer = 0;
        }
    }

    public void chasing() {
        if (!(Math.abs(player.getX() - this.getX()) < 3 && Math.abs(player.getY()-this.getY()) < 3)) {
            float difX = (player.hitbox.p1.x-this.hitbox.p1.x);
            float difY = (player.hitbox.p1.y-this.hitbox.p1.y);

            float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
            float[] changeSpeed = new float[]{(difX*spd/r), difY*spd/r};
            dir[0] = changeSpeed[0]*(25f);
            dir[1] = changeSpeed[1]*(25f);
            
        }
        if (++chaseTime == 60) {
            chasing = false;
            attacking = true;
            chaseTime = 0;
        }
    }

    public void attack() {
        hitPlayer = false;
        attacking = false;
        stunTime = 120;
        attackTime = 40;
        wandering = false;
        dir[0] = 0;
        dir[1] = 0;
        if (Math.floor(Math.random() * 3) == 0)  {
            blinkAttack();
            attack = 1;
        } else if (Math.floor(Math.random() * 2) == 0) {
            tantrum();
            attack = 2;
        } else {
            lineAttack();
            attack = 3;
        }
    }

    public void blinkAttack() {
        float prediction;
        if(Math.round(Math.random()) == 0) {
            prediction = 1.5f;
        } else {
            prediction = 1f;
        }

        float newX = hitbox.p1.x + (player.hitbox.p1.x - hitbox.p1.x) * prediction;
        float newY = hitbox.p1.y + (player.hitbox.p1.y - hitbox.p1.y) * prediction;

        if (newY > constants.Display.HEIGHT - hitbox.diameter/2) {
            newY = constants.Display.HEIGHT - hitbox.diameter/2; 
        }
        if (newY < hitbox.diameter/2) {
            newY = hitbox.diameter/2; 
        }
        if (newX > constants.Display.WIDTH - hitbox.diameter/2) {
            newX = constants.Display.WIDTH-hitbox.diameter/2; 
        }
        if (newX < hitbox.diameter/2) {
            newX = hitbox.diameter/2; 
        }

        blinkedFrom = new Point_ (hitbox.p1.x, hitbox.p1.y);
        blink = true;
        blinkAnim = 40;
        curAttack.hitboxes = new Circle[]{new Circle (new Point_(newX, newY),(int)hitbox.diameter)};
    }

    public void lineAttack() {
        float difX = (player.hitbox.p1.x-this.hitbox.p1.x);
        float difY = (player.hitbox.p1.y-this.hitbox.p1.y);
        float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));

        Line path = new Line(difY/difX, hitbox.p1);

        Point_ end = new Point_(
            hitbox.p1.x + difX * hitbox.diameter * 2 / r, 
            hitbox.p1.y + difY * hitbox.diameter * 2 / r
        );

        curAttack.hitboxes = new Shape[3];
        curAttack.hitboxes[0] = new Rectangle(hitbox.p1, path, (int)hitbox.diameter/2, (int)hitbox.diameter*2*(int)(Math.abs(difX)/difX));
        curAttack.hitboxes[1] = new Circle(new Point_(hitbox.p1.x + (end.x - hitbox.p1.x)/4, hitbox.p1.y + (end.y - hitbox.p1.y)/4), (int)(hitbox.diameter));
        curAttack.hitboxes[2] = new Circle(new Point_(hitbox.p1.x + (end.x - hitbox.p1.x)*3/4, hitbox.p1.y + (end.y - hitbox.p1.y)*3/4), (int)(hitbox.diameter));
        //Rectangle(Point_ p, Line centre, int width, int length)
    }

    public void tantrum() {
        curAttack.hitboxes = new Circle[]{new Circle(hitbox.p1, getRadius()*3)};
    }


    public String toString() {
        return "The Brawler";
    }

    public void kill() {
        alive = false;
    }
}