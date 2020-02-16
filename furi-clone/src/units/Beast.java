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

    public Beast(int x, int y, Player p) {
        super(constants.Beast.HEALTH, 0, 1, x, y, 80, 1f, p);
        for (Bullet[] bulls : bullets) {
            for (Bullet b : bulls) {
                b = new Bullet(new Shape[] {new Circle(player.location, (int)(player.radius / 2))}, new Circle(this.location, constants.Add.BULLETSIZE), constants.Add.BULLETSPEED*2, new Line(this.location, player.location));
                b = null;
            }
        }
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

                if (((dashSequence > 0 && attackTimer == 0) || attackTimer <= 5) && !volleyed) {
                    if(curAttack != null && curAttack.checkHit(new Circle(player.location, player.getRadius()/2)) && !hitPlayer) {
                        player.hit();
                        hitPlayer = true;
                    }
                    if (dashSequence > 0) {
                        this.location = new Point_(dashLocation.x, dashLocation.y);
                    }
                    if (attackTimer == 0) {
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
                    b.changeTargets(new Shape[] {new Circle(player.location, (int)(player.radius / 2))});
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
        int a = (int)(Math.random() * 5);
        
        if (a == 0 || a == 1 || Point_.distanceFormula(player.location, location) > 400) {
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
            attackDelay = 60;
            stunTimer = 30;
        } else if (attackTimer == 0) {
            if (dashSequence > 1) {
                if (dashSequence == 4) attackTimer = 30;
                else attackTimer = 15;
                setDash();
            } else {
                //blink attack
                attackTimer = 50;
                blinkAttack();
            }
        }
    }

    public void volley() {
        if (attackTimer == 0 && --volleying == 0) {
            attackDelay = 60;
            volleyed = false;
            stunTimer = 30;
        } else if (attackTimer == 0) {
            attackTimer = 10;
            for (int i = 0; i < constants.Beast.MAXBULLETS; i++) {
                Point_ target = new Point_ (player.location.x + (Math.random() * 100 - 50), player.location.y + (Math.random() * 100 - 50));
                bullets[volleying - 1][i] = new Bullet(new Shape[] {new Circle(player.location, (int)(player.radius / 2))}, new Circle(this.location, constants.Add.BULLETSIZE), constants.Add.BULLETSPEED*3, new Line(this.location, target));
                if (target.x < this.location.x) bullets[volleying - 1][i].setSpeed(-constants.Add.BULLETSPEED * 3);
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
        if (attackTimer == 0 && n() && --stormSequence == 0) {
            attackDelay = 60;
            stunTimer = 30;
        } else if (attackTimer == 0) {
            if (stormSequence == 4) attackTimer = 50;
            else attackTimer = 25;
            curAttack = new Attack();
            int set = 0;
            if (stormSequence == 2) {
                curAttack.hitboxes = new Shape[8];
                for (int i = -50; i <= 50; i += 25) {
                    if (i == 0) continue;
                    for (int j = -25; j <= 25; j += 50) {
                        if (i == -25 || i == 25) curAttack.hitboxes[set] = setBeam(new Point_ (location.x + i + 1, location.y + j*2 + 1));
                        else curAttack.hitboxes[set] = setBeam(new Point_ (location.x + i + 1, location.y + j + 1));
                        set++;
                        System.out.println(set);
                    }
                }
            } else {
                curAttack.hitboxes = new Shape[8];
                for (int i = -50; i <= 50; i += 50) {
                    for (int j = -50; j <= 50; j += 50) {
                        if (!(i == 0 && j == 0)) {
                            curAttack.hitboxes[set] = setBeam(new Point_ (location.x + i + 1, location.y + j + 1));
                            set++;
                        }
                    }
                }
            }
        }
    }

    public Rectangle setBeam(Point_ p) {
        float difX = (p.x-this.location.x);
        float difY = (p.y-this.location.y);
        float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));

        Line path = new Line(difY/difX, location);

        Point_ end = new Point_(
            location.x + difX * radius * 5, 
            location.y + difY * radius * 5
        );
        return new Rectangle(location, path, 30, 1000*(int)(Math.abs(difX)/difX));
    }

    public void setDash() {
        float newX = location.x;
        float newY = location.y;
        float difX = (player.location.x - location.x);
        float difY = (player.location.y - location.y);
        double difR = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        difX += (200*difX / difR);
        difY += (200*difY / difR);
        difR = Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2));
        newY += difY;
        newX += difX;
        dashLocation = new Point_ (newX, newY);

        curAttack = new Attack();
        curAttack.hitboxes = new Shape[]{new Rectangle(location, new Line(location, new Point_ (newX, newY)), 5, (int)(difR*(Math.abs(difX)/difX))), new Circle (new Point_(newX, newY),(int)radius/2)};
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
        blinkAnim = 50;
        dashLocation = new Point_(newX, newY);
        curAttack.hitboxes = new Circle[]{new Circle (new Point_(newX, newY),(int)radius*3/2)};
    }

    public void kill() {
        alive = false;
    }

    public String toString() {
        return "THE BEAST";
    }

    public boolean n() {
        System.out.println(attackTimer + "????????????????????????????????????????");
        return true;
    }
}