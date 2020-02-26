package units;
import graph.*;
import shapes.*;

import java.util.HashSet;

import constants.Display;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Player extends Unit {

    float difX, difY, difR;

    public int iFrames, dashTimer, attackFrames, score, bossesAlive = 0;
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
        curAttack.hitboxes = new Circle[5];
        ID = constants.Player.ID;
    }

    public LineSegment[] getAttackGraphic() {

        LineSegment[] attackGraphic = new LineSegment[2];
        LineSegment path = new LineSegment(location, curAttack.hitboxes[4].getLocation());
        
        LineSegment temp = new LineSegment(new Line((-1f / path.equation.m), this.location), this.location, this.radius/2);
        Point_ bR = temp.p2;
        float x = this.location.x + (this.location.x - temp.p2.x);
        Point_ bL = new Point_(x, temp.equation.getY(x)); 

        attackGraphic[0] = new LineSegment (bR, tip);
        attackGraphic[1] = new LineSegment (bL, tip);

        return attackGraphic;
    }

    public void update() {
        if (iFrames > 0) --iFrames;
        if (dashTimer > 0) --  dashTimer;
        if (upgradeTimer > 0) --upgradeTimer;
        if (location.y > constants.Display.HEIGHT - radius - 15) location.y = constants.Display.HEIGHT - radius - 15;
        if (location.y < radius/2) location.y = radius/2;
        if (location.x > constants.Display.WIDTH - radius + 10) location.x = constants.Display.WIDTH-radius+10;
        if (location.x < radius/2) location.x = radius/2;

        setAttack();
        if (attackFrames > 0 && attackFrames-- <= 1) {
            if (!attacked.isEmpty()) {
                for (Unit u : attacked) {
                    if (!u.takeDamage(this.dmg) && u instanceof Boss) {
                        u.kill();
                        if(hp<3)++hp;
                        ++score;
                        killedBoss = true;
                        if (u instanceof Boss) killed.add(u);
                        --bossesAlive;
                    }
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
            spd = 0;
            attackFrames = 30;
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

    public void setAttack(Point_ p) {
        if (!(Math.abs(p.x - this.getX()) < 1 && Math.abs(p.y-this.getY()) < 1)) {

            difX = (p.x-this.location.x);
            difY = (p.y-this.location.y);
            difR = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));

            
        }
    }
    public void setAttack() {
        for (int i = 0; i < 5; ++i) {
            curAttack.hitboxes[i] = new Circle(new Point_(location.x + difX * ATTACK_LENGTHS[i] / difR, location.y + difY * ATTACK_LENGTHS[i] / difR), (25 - 3*i));
            tip = new Point_(location.x + difX * (ATTACK_LENGTHS[i] + 13) / difR, location.y + difY * (ATTACK_LENGTHS[i] + 13) / difR);
        }
    }

    public boolean checkAttack(Unit enemy) {
        if (!attacked.contains(enemy) && curAttack.checkHit(new Circle(enemy.location, enemy.getRadius()))) {
            attacked.add(enemy);
            return true;
        }
        return false;
    }
    
    public void hit () {
        if(iFrames == 0) {hp -= 1;iFrames = 60;dramaticPause = true;}
    }

    public void draw(Graphics g, Graphics2D g2D, Color[] HITBOXCOLOURS) {
        if (attackFrames > 0) {
            if (attackFrames > 5 && attackFrames <= 5 + constants.Player.ATTACK_FRAMES) {
                g.setColor(HITBOXCOLOURS[2]);
            } else {
                g.setColor(HITBOXCOLOURS[3]);
            }
            LineSegment[] draw = getAttackGraphic();
            g2D.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            constants.Display.drawLine(g2D, draw[0]);
            constants.Display.drawLine(g2D, draw[1]);
        }
        

        if (dashTimer > 0) {
            g.setColor(HITBOXCOLOURS[1]);
            g2D.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            g2D.drawRect(0, 0, Display.WIDTH - 8, Display.HEIGHT - 30);
        }
        if(iFrames > 0) {
            g.setColor(HITBOXCOLOURS[2]);
        } else if (upgradeTimer > 0 || dmg > constants.Player.DAMAGE) {
            g.setColor(HITBOXCOLOURS[4]);
        } else {
            g.setColor(HITBOXCOLOURS[0]);
        }
        constants.Display.drawCircle(g, new Circle(new Point_(getX(), getY()), getRadius()));
    }

    public void kill() {

    }
}