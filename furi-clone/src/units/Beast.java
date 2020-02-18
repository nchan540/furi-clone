package units;
import graph.*;
import shapes.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Beast extends Boss {


    public int attackTimer = 0;
    public int attackDelay = 30;
    public int stunTimer = 0;
    public boolean hitPlayer = false;

    public int dashSequence = 0;
    public Point_ dashLocation;
    public int blinkAnim = 0;
    public Point_ blinkedFrom;
    // 5 = begin dash sequence, 4 - 2 = three dashes, 1 = final blink attack

    public int stormSequence = 0;
    public int volleying = 0;

    public Beast(int x, int y, Player p) {
        super(constants.Beast.HEALTH, 0, 1, x, y, Math.round(constants.Display.HEIGHT/constants.Beast.SIZERATIO), constants.Display.HEIGHT / constants.Beast.SPEEDRATIO, p);
    }

    public void update() {
        if (stunTimer <= 0) {
            if (attackTimer == 0) {
                if (attackDelay > 0) {
                    location.x += Math.round(((Math.random()*2)-1) * 5);
                    chase();
                    move();
                    --attackDelay;
                } else if (attackDelay == 0) {
                    attack();
                }
            } else {
                --attackTimer;
                if (dashSequence > 0) {
                    tripleDash();
                } else if (stormSequence > 0) {
                    storm();
                } else {
                    volley();
                }

                if ((dashSequence > 0 && attackTimer == 0) || attackTimer <= 5) {
                    if(curAttack.checkHit(new Circle(player.location, player.getRadius()/2)) && !hitPlayer) {
                        player.hit();
                        hitPlayer = true;
                    }
                    if (dashSequence > 0) {
                        this.location = new Point_(dashLocation.x, dashLocation.y);
                    }
                    if (attackTimer == 0) {
                        hitPlayer = false;
                    }
                }
            }
        } else {
            --stunTimer;
        }
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {
        if (attackTimer > 0) {
            if (dashSequence > 0) {
                if (dashSequence != 1) {
                    g2.setStroke(new BasicStroke(Math.round(constants.Display.HEIGHT/80), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                    constants.Display.drawLine(g2, curAttack.hitboxes[0].forDrawLaser());
                } else {
                    constants.Display.drawCircle(g, new Circle(curAttack.hitboxes[0].getLocation(), curAttack.hitboxes[0].getRadius()*2));
                }
            }
        }


        constants.Display.drawCircle(g, new Circle(this.location, this.getRadius()));
    }


    public void chase() {
        float difX = (player.location.x-this.location.x);
        float difY = (player.location.y-this.location.y);

        float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
        float[] changeSpeed = new float[]{(difX*spd/r), difY*spd/r};
        dir[0] = changeSpeed[0]*((float)Math.random() * 5f);
        dir[1] = changeSpeed[1]*((float)Math.random() * 5f);
    }

    public void attack() {
        dashSequence = 5;
        tripleDash();
    }

    public void tripleDash() {
        if (attackTimer == 0 && --dashSequence == 0) {
            attackDelay = 60;
            stunTimer = 30;
        } else if (attackTimer == 0) {
            if (dashSequence > 1) {
                if (dashSequence == 4) attackTimer = 30;
                else attackTimer = 20;
                setDash();
            } else {
                //blink attack
                attackTimer = 30;
                blinkAttack();
            }
        }
    }

    public void volley() {

    }

    public void storm() {

    }

    public void setDash() {
        float newX = location.x;
        float newY = location.y;
        float difX = (player.location.x - location.x);
        float difY = (player.location.y - location.y);
        double difR = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        difX += (Math.round(constants.Display.HEIGHT/4)*difX / difR);
        difY += (Math.round(constants.Display.HEIGHT/4)*difY / difR);
        difR = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        newY += difY;
        newX += difX;
        dashLocation = new Point_ (newX, newY);

        curAttack = new Attack();
        curAttack.hitboxes = new Shape[]{new Rectangle(location, new Line(location, new Point_ (newX, newY)), 5, (int)(difR*(Math.abs(difX)/difX)))};
    }

    public void blinkAttack() {

        float newX = location.x + (player.location.x - location.x);
        float newY = location.y + (player.location.y - location.y);

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

        blinkedFrom = new Point_ (location.x, location.y);
        blinkAnim = 30;
        dashLocation = new Point_(newX, newY);
        curAttack.hitboxes = new Circle[]{new Circle (new Point_(newX, newY),(int)radius/2)};
    }

    public void kill() {
        alive = false;
    }

    public String toString() {
        return "THE BEAST";
    }
}