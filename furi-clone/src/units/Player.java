package units;
import graph.*;
import shapes.*;

import java.util.HashSet;

import constants.Display;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.awt.Point;

public class Player extends Unit {

    float difX, difY, difR;
    public int attackType;

    public int iFrames, dashTimer, attackFrames, score, bossesAlive, energy = 0;
    public int upgradeTimer = 0;
    public boolean dramaticPause, dashQueued, killedBoss = false;
    public int[] queueXY = {0, 0};
    public HashSet<Unit> attacked = new HashSet<>();
    public HashSet<Unit> killed = new HashSet<>();

    public Attack curAttack = new Attack();

    public final int[] ATTACK_LENGTHS = {40, 65, 87, 106, 123};
    public Point_ tip;

    public Player(int x, int y, int r) {
        super (constants.Player.HEALTH, 0, constants.Player.DAMAGE, x, y, r, constants.Player.SPEED);
        ID = constants.Player.ID;
    }

    public LineSegment[] getAttackGraphic() {

        LineSegment[] attackGraphic = new LineSegment[2];
        LineSegment path = new LineSegment(hitbox.p1, curAttack.hitboxes[4].getLocation());
        
        LineSegment temp = new LineSegment(new Line((-1f / path.equation.m), this.hitbox.p1), this.hitbox.p1, this.hitbox.diameter/2);
        Point_ bR = temp.p2;
        float x = this.hitbox.p1.x + (this.hitbox.p1.x - temp.p2.x);
        Point_ bL = new Point_(x, temp.equation.getY(x)); 

        attackGraphic[0] = new LineSegment (bR, tip);
        attackGraphic[1] = new LineSegment (bL, tip);

        return attackGraphic;
    }

    public void update() {
        if (iFrames > 0) --iFrames;
        if (dashTimer > 0) --  dashTimer;
        if (upgradeTimer > 0) --upgradeTimer;

        if (attackFrames > 0 && attackFrames-- <= 1) {
            if (!attacked.isEmpty()) {
                if(attackType == 2) dmg = 5;
                for (Unit u : attacked) {
                    if (!u.takeDamage(this.dmg) && u instanceof Boss) {
                        u.kill();
                        if(hp<constants.Player.HEALTH)++hp;
                        if(hp<constants.Player.HEALTH)++hp;
                        ++score;
                        killedBoss = true;
                        if (u instanceof Boss) killed.add(u);
                        --bossesAlive;
                    }
                    if (energy < 20 && attackType != 2) ++energy;
                }
            }
            spd = constants.Player.SPEED;
            attacked.clear();
            if (dashQueued) {
                dash(queueXY[0], queueXY[1]);
                dashQueued = false;
            }
            dmg = constants.Player.DAMAGE;
            upgradeTimer = 0;
        }
    }

    public void attack() {
        if (attackFrames <= 0) {
            if (attackType == 1) {
                curAttack.hitboxes = new Circle[5];
            } else {
                curAttack.hitboxes = new Rectangle[1];
            }
            setAttack();
            spd = 0;
            if (attackType == 1) attackFrames = 30;
            else attackFrames = 15;
            if (upgradeTimer > 0) dmg *= 3;
        }
    }

    public void dash(int x, int y) {
        if (dashTimer == 0) {
            if (attackFrames <= 5 || attackFrames >= 10) {
                dashTimer = 60;
                float prevSpd = spd;
                spd = constants.Player.DASH_DISTANCE;
                move(x, y);
                if (attackFrames > 0) setAttack();
                spd = prevSpd;
                if (!(x == 0 && y == 0)) { 
                    if (attackFrames > 10) {
                        dmg *= 3;
                    } else {
                        upgradeTimer = 3;
                    }
                } 
            } else {
                dashQueued = true;
                queueXY = new int[]{x, y};
            }
        }
    }

    //initial declaration of the attack coordinates RELATIVE to player position
    public void setAttack(Point p, boolean rightClick) {
        if (rightClick) attackType = 1;
        else {attackType = 2; energy -= 5;}
        if (!(Math.abs(p.x - this.getX()) < 1 && Math.abs(p.y-this.getY()) < 1)) {

            difX = (p.x-this.hitbox.p1.x);
            difY = (p.y-this.hitbox.p1.y);
            difR = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));

            
        }
    }


    //adjusting attack coordinates based on changes in player position
    public void setAttack() {
        if (attackType == 1) {
            for (int i = 0; i < 5; ++i) {
                curAttack.hitboxes[i] = new Circle(new Point_(hitbox.p1.x + difX * ATTACK_LENGTHS[i] / difR, hitbox.p1.y + difY * ATTACK_LENGTHS[i] / difR), (25 - 3*i));
                tip = new Point_(hitbox.p1.x + difX * (ATTACK_LENGTHS[i] + 13) / difR, hitbox.p1.y + difY * (ATTACK_LENGTHS[i] + 13) / difR);
            }
        } else {
            Point_ end = new Point_(hitbox.p1.x + difX, hitbox.p1.y+difY);
            curAttack.hitboxes[0] = new Rectangle(end, new Line(hitbox.p1, end), 5, -1*Math.round(difR*(Math.abs(difX)/difX)));
        }
    }

    public boolean checkAttack(Unit enemy) {
        if (!attacked.contains(enemy) && curAttack.checkHit(new Circle(enemy.hitbox.p1, enemy.getRadius()))) {
            attacked.add(enemy);
            return true;
        }
        return false;
    }
    
    public void hit () {
        if(iFrames == 0) {hp -= 1;iFrames = 60;dramaticPause = true;}
    }

    public void draw(Graphics g, Graphics2D g2D, Color[] HITBOXCOLOURS) {
        if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.diameter/2) hitbox.p1.y = constants.Display.HEIGHT - hitbox.diameter/2;
        if (hitbox.p1.y < hitbox.diameter/2) hitbox.p1.y = hitbox.diameter/2;
        if (hitbox.p1.x > constants.Display.WIDTH - hitbox.diameter/2) hitbox.p1.x = constants.Display.WIDTH-hitbox.diameter/2;
        if (hitbox.p1.x < hitbox.diameter/2) hitbox.p1.x = hitbox.diameter/2;
        if (attackFrames > 0) {
            if (attackFrames > 5 && attackFrames <= 5 + constants.Player.ATTACK_FRAMES) {
                g.setColor(HITBOXCOLOURS[2]);
            } else {
                g.setColor(HITBOXCOLOURS[3]);
            }
            g2D.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            if (attackType == 1) {
                LineSegment[] draw = getAttackGraphic();
                constants.Display.drawLine(g2D, draw[0]);
                constants.Display.drawLine(g2D, draw[1]);
            } else {
                constants.Display.drawLine(g2D, curAttack.hitboxes[0].forDrawLaser());
            }
        }
        

        // if (dashTimer > 0) {
        //     g.setColor(HITBOXCOLOURS[1]);
        //     g2D.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        //     g2D.drawRect(0, 0, Display.WIDTH - 8, Display.HEIGHT - 30);
        // }
        if(iFrames > 0) {
            g.setColor(HITBOXCOLOURS[2]);
        } else if (upgradeTimer > 0 || dmg > constants.Player.DAMAGE) {
            g.setColor(HITBOXCOLOURS[4]);
        } else {
            g.setColor(HITBOXCOLOURS[0]);
        }
        constants.Display.drawCircle(g, this.hitbox);
    }

    public void kill() {

    }
}