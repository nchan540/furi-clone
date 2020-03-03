package units;
import graph.*;
import shapes.*;
import projectiles.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Beast extends Boss {


    public Attack curAttack = null;
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
    public boolean volleyed = false;
    public Bullet[][] bullets = new Bullet[3][constants.Beast.MAXBULLETS];

    public int ID = 3;

    public Beast(int x, int y, Player p) {
        super(constants.Beast.HEALTH, 0, 1, x, y, 80, 1f, p);
        for (Bullet[] bulls : bullets) {
            for (Bullet b : bulls) {
                b = new Bullet(new Shape[] {new Circle(player.hitbox.p1, (int)(player.hitbox.diameter / 2))}, new Circle(this.hitbox.p1, constants.Add.BULLETSIZE), constants.Add.BULLETSPEED*2, new Line(this.hitbox.p1, player.hitbox.p1));
                b = null;
            }
        }
    }

    public void update() {
        if (stunTimer <= 0) {
            if (attackTimer == 0) {
                if (attackDelay > 0) {
                    hitbox.p1.x += Math.round(((Math.random()*2)-1) * 5);
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

                if (((dashSequence > 0 && attackTimer == 0) || attackTimer <= 5) && !volleyed) {
                    if(curAttack != null && curAttack.checkHit(new Circle(player.hitbox.p1, player.getRadius()/2)) && !hitPlayer) {
                        player.hit();
                        hitPlayer = true;
                    }
                    if (dashSequence > 0) {
                        this.hitbox.p1 = new Point_(dashLocation.x, dashLocation.y);
                    }
                    if (attackTimer == 0) {
                        attackDelay = 30;
                        hitPlayer = false;
                        curAttack = null;
                    }
                }
            }
        } else {
            --stunTimer;
        }

        clearBullets();

        for (Bullet[] bulls : bullets) {
            for (Bullet b : bulls) {
                if (b != null) {
                    b.move();
                    b.changeTargets(new Shape[] {new Circle(player.hitbox.p1, (int)(player.hitbox.diameter / 2))});
                    if(b.hitDetect()[0]) player.hit();
                }
            }
        }
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {
        if (attackTimer > 0) {
            g.setColor(HITBOXCOLOURS[3]);
            if (dashSequence > 0) {
                if (dashSequence != 1) {
                    g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                    constants.Display.drawLine(g2, curAttack.hitboxes[0].forDrawLaser());
                    constants.Display.drawCircle(g, new Circle(curAttack.hitboxes[1].getLocation(), curAttack.hitboxes[1].getRadius()*2));
                } else {
                    constants.Display.drawCircle(g, new Circle(curAttack.hitboxes[0].getLocation(), curAttack.hitboxes[0].getRadius()*2));
                }
            }
            if (stormSequence > 0 && stormSequence < 4) {
                g2.setStroke(new BasicStroke(60, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                if (attackTimer <= 5) g.setColor(HITBOXCOLOURS[2]);
                for (Shape s : curAttack.hitboxes) {
                    constants.Display.drawLine(g2, s.forDrawLaser());
                }
            }
        }

        g.setColor(Color.PINK);
        for (Bullet[] bulls : bullets) {
            for (Bullet b : bulls) {
                if (b != null) {
                    constants.Display.drawCircle(g, b.hitbox);
                }
            }
        }

        g.setColor(HITBOXCOLOURS[2]);
        if (attackTimer > 0 && volleying == 4) g.setColor(Color.GREEN);
        constants.Display.drawCircle(g, this.hitbox);
    }


    public void chase() {
        float difX = (player.hitbox.p1.x-this.hitbox.p1.x);
        float difY = (player.hitbox.p1.y-this.hitbox.p1.y);

        float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
        float[] changeSpeed = new float[]{(difX*spd/r), difY*spd/r};
        dir[0] = changeSpeed[0]*((float)Math.random() * 5f);
        dir[1] = changeSpeed[1]*((float)Math.random() * 5f);
    }

    public void attack() {
        int a = (int)(Math.random() * 5);
        
        if (a == 0 || a == 1 || Point_.distanceFormula(player.hitbox.p1, hitbox.p1) > 400) {
            dashSequence = 5;
            tripleDash();
        } else if (a == 2) {
            volleying = 4;
            attackTimer = 30;
            volleyed = true;
            volley();
        } else if (a == 3 || a == 4) {
            stormSequence = 4;
            storm();
        }
    }

    public void tripleDash() {
        if (attackTimer == 0 && --dashSequence == 0) {
            stunTimer = 30;
        } else if (attackTimer == 0) {
            if (dashSequence > 1) {
                if (dashSequence == 4) attackTimer = 30;
                else attackTimer = 15;
                setDash();
            } else {
                //blink attack
                attackTimer = 20;
                blinkAttack();
            }
        }
    }

    public void volley() {
        if (attackTimer == 0 && --volleying == 0) {
            volleyed = false;
            stunTimer = 30;
        } else if (attackTimer == 0) {
            attackTimer = 10;
            for (int i = 0; i < constants.Beast.MAXBULLETS; i++) {
                Point_ target = new Point_ (player.hitbox.p1.x + (Math.random() * 100 - 50), player.hitbox.p1.y + (Math.random() * 100 - 50));
                bullets[volleying - 1][i] = new Bullet(new Shape[] {new Circle(player.hitbox.p1, (int)(player.hitbox.diameter / 2))}, new Circle(this.hitbox.p1, constants.Add.BULLETSIZE), constants.Add.BULLETSPEED*Math.round(Math.random()*2+1), new Line(this.hitbox.p1, target));
                if (target.x < this.hitbox.p1.x) bullets[volleying - 1][i].setSpeed(constants.Add.BULLETSPEED*Math.round(Math.random()*(-2) - 1));
            }
        }
    }

    public void clearBullets() {
        for (Bullet[] bulls : bullets) {
            for (Bullet b : bulls) {
                if (b != null && (0 > b.hitbox.p1.x + b.hitbox.diameter || constants.Display.WIDTH < b.hitbox.p1.x - b.hitbox.diameter
                    || 0 > b.hitbox.p1.y + b.hitbox.diameter || constants.Display.HEIGHT < b.hitbox.p1.y - b.hitbox.diameter)) b = null;
            }
        }
    }

    public void storm() {
        if (attackTimer == 0 && --stormSequence == 0) {
            stunTimer = 30;
        } else if (attackTimer == 0) {
            if (stormSequence == 4) attackTimer = 50;
            else attackTimer = 36;
            curAttack = new Attack();
            int set = 0;
            if (stormSequence == 2) {
                curAttack.hitboxes = new Shape[8];
                for (int i = -50; i <= 50; i += 25) {
                    if (i == 0) continue;
                    for (int j = -25; j <= 25; j += 50) {
                        if (i == -25 || i == 25) curAttack.hitboxes[set] = setBeam(new Point_ (hitbox.p1.x + i + 1, hitbox.p1.y + j*2 + 1));
                        else curAttack.hitboxes[set] = setBeam(new Point_ (hitbox.p1.x + i + 1, hitbox.p1.y + j + 1));
                        set++;
                    }
                }
            } else {
                curAttack.hitboxes = new Shape[8];
                for (int i = -50; i <= 50; i += 50) {
                    for (int j = -50; j <= 50; j += 50) {
                        if (!(i == 0 && j == 0)) {
                            curAttack.hitboxes[set] = setBeam(new Point_ (hitbox.p1.x + i + 1, hitbox.p1.y + j + 1));
                            set++;
                        }
                    }
                }
            }
        }
    }

    public Rectangle setBeam(Point_ p) {
        float difX = (p.x-this.hitbox.p1.x);
        float difY = (p.y-this.hitbox.p1.y);

        Line path = new Line(difY/difX, hitbox.p1);

        return new Rectangle(hitbox.p1, path, 30, 1000*(int)(Math.abs(difX)/difX));
    }

    public void setDash() {
        float newX = hitbox.p1.x;
        float newY = hitbox.p1.y;
        float difX = (player.hitbox.p1.x - hitbox.p1.x);
        float difY = (player.hitbox.p1.y - hitbox.p1.y);
        double difR = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        difX += (200*difX / difR);
        difY += (200*difY / difR);
        difR = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        newY += difY;
        newX += difX;
        dashLocation = new Point_ (newX, newY);

        curAttack = new Attack();
        curAttack.hitboxes = new Shape[]{new Rectangle(hitbox.p1, new Line(hitbox.p1, new Point_ (newX, newY)), 5, (int)(difR*(Math.abs(difX)/difX))), new Circle (new Point_(newX, newY),(int)hitbox.diameter/2)};
    }

    public void blinkAttack() {

        float newX = hitbox.p1.x + (player.hitbox.p1.x - hitbox.p1.x);
        float newY = hitbox.p1.y + (player.hitbox.p1.y - hitbox.p1.y);

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
        blinkAnim = 50;
        dashLocation = new Point_(newX, newY);
        curAttack.hitboxes = new Circle[]{new Circle (new Point_(newX, newY),(int)hitbox.diameter*3/2)};
    }

    public void kill() {
        alive = false;
    }

    public String toString() {
        return "The Beast";
    }

}