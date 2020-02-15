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
    public int chaseTime, stunTime, attackTime, attack = 0;
    public boolean wandering, chasing, attacking, hitPlayer, blink = false;

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
            if (--attackTime == 10) {
                if (attack == 1) this.location = curAttack.hitboxes[0].getLocation();
            }
            if (attackTime > 0 && attackTime <= 10) {
                if(curAttack.checkHit(new Circle(player.location, player.getRadius()/2)) && !hitPlayer) {
                    player.hit();
                    if (blink) {player.takeDamage(1); blink = false;}
                    hitPlayer = true;
                }
            }
            if (attackTime == 0) blink = false;
        }

        if (location.y > constants.Display.HEIGHT - radius + 10) {
            location.y = constants.Display.HEIGHT - radius + 10; 
        }
        if (location.y < radius/2) {
            location.y = radius/2; 
        }
        if (location.x > constants.Display.WIDTH - radius + 35) {
            location.x = constants.Display.WIDTH-radius + 35; 
        }
        if (location.x < radius/2) {
            location.x = radius/2; 
        }
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {
        if (attackTime > 0) {
            if (attackTime > 10) g.setColor(HITBOXCOLOURS[3]);
            else g.setColor(HITBOXCOLOURS[2]);
            
            if (!(attack == 3)) constants.Display.drawCircle(g, new Circle(curAttack.hitboxes[0].getLocation(), curAttack.hitboxes[0].getRadius()*2));
            else {
                for (LineSegment l : curAttack.hitboxes[0].getLines()) {
                    g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                    constants.Display.drawLine(g2, l);
                }
                constants.Display.drawCircle(g2, new Circle(curAttack.hitboxes[1].getLocation(), curAttack.hitboxes[1].getRadius()*2));
                constants.Display.drawCircle(g2, new Circle(curAttack.hitboxes[2].getLocation(), curAttack.hitboxes[2].getRadius()*2));
            }
        }
        g.setColor(HITBOXCOLOURS[2]);
        if (stunTime > 0 && attackTime == 0) g.setColor(HITBOXCOLOURS[5]);
        constants.Display.drawCircle(g, new Circle(location, getRadius()));
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

                if (location.x > constants.Display.WIDTH - radius*3) dir[0] = -1.5f;
                if (location.x < radius*3) dir[0] = 1.5f;
                if (location.y > constants.Display.HEIGHT - radius*3/2) dir[1] = -1.0f;
                if (location.y < radius*3/2) dir[1] = 1.0f;

                wanderTimer = 150;
            }
            wandering = !wandering;
        }
        --wanderTimer;
    }

    public void chase() {
        if (Point_.distanceFormula(location, player.location) < (radius * 3)) {
            chasing = true;
            wanderTimer = 0;
        }
    }

    public void chasing() {
        if (!(Math.abs(player.getX() - this.getX()) < 3 && Math.abs(player.getY()-this.getY()) < 3)) {
            float difX = (player.location.x-this.location.x);
            float difY = (player.location.y-this.location.y);

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

        float newX = location.x + (player.location.x - location.x) * prediction;
        float newY = location.y + (player.location.y - location.y) * prediction;

        if (newY > constants.Display.HEIGHT - radius + 10) {
            newY = constants.Display.HEIGHT - radius + 10; 
        }
        if (newY < radius/2) {
            newY = radius/2; 
        }
        if (newX > constants.Display.WIDTH - radius + 35) {
            newX = constants.Display.WIDTH-radius + 35; 
        }
        if (newX < radius/2) {
            newX = radius/2; 
        }

        blink = true;
        curAttack.hitboxes = new Circle[]{new Circle (new Point_(newX, newY),(int)radius/2)};
    }

    public void lineAttack() {
        float difX = (player.location.x-this.location.x);
        float difY = (player.location.y-this.location.y);
        float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));

        Line path = new Line(difY/difX, location);

        Point_ end = new Point_(
            location.x + difX * radius * 2 / r, 
            location.y + difY * radius * 2 / r
        );

        curAttack.hitboxes = new Shape[3];
        curAttack.hitboxes[0] = new Rectangle(location, path, (int)radius/2, (int)radius*2*(int)(Math.abs(difX)/difX));
        curAttack.hitboxes[1] = new Circle(new Point_(location.x + (end.x - location.x)/4, location.y + (end.y - location.y)/4), (int)(radius/2));
        curAttack.hitboxes[2] = new Circle(new Point_(location.x + (end.x - location.x)*3/4, location.y + (end.y - location.y)*3/4), (int)(radius/2));
        //Rectangle(Point_ p, Line centre, int width, int length)
    }

    public void tantrum() {
        curAttack.hitboxes = new Circle[]{new Circle(location, getRadius()*3/2)};
    }


    public String toString() {
        return "The Brawler";
    }

    public void kill() {
        alive = false;
    }
}