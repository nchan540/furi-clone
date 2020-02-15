package units;
import shapes.*;
import graph.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Laserman extends Boss {

    public int attackTimer = 0;
    public Attack[] curAttack;
    public int attackType = 0;

    public int wanderTimer = 150;
    public boolean wandering, hitPlayer = false;

    public Laserman(int x, int y, Player p) {
        super(constants.Laserman.HEALTH, 0, 1, x, y, 150, 0.5f, p);
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {
        if (attackTimer > 0) {
            g2.setStroke(new BasicStroke(30, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            if (attackType == 1) {
                if (attackTimer > 30) {
                    g.setColor(HITBOXCOLOURS[3]);
                    constants.Display.drawLine(g2, setBeam(player.location).forDrawLaser());
                } else if (attackTimer > 25 && attackTimer < 30) {
                    g.setColor(HITBOXCOLOURS[3]);
                    constants.Display.drawLine(g2, curAttack[0].hitboxes[0].forDrawLaser());
                    //all three beams black
                } else if (attackTimer > 20 && attackTimer < 25) {
                    g.setColor(HITBOXCOLOURS[3]);
                    constants.Display.drawLine(g2, curAttack[0].hitboxes[0].forDrawLaser());
                    constants.Display.drawLine(g2, curAttack[1].hitboxes[0].forDrawLaser());
                } else if (attackTimer > 15 && attackTimer < 20) {
                    g.setColor(HITBOXCOLOURS[3]);
                    constants.Display.drawLine(g2, curAttack[0].hitboxes[0].forDrawLaser());
                    constants.Display.drawLine(g2, curAttack[1].hitboxes[0].forDrawLaser());
                    constants.Display.drawLine(g2, curAttack[2].hitboxes[0].forDrawLaser());
                } else if (attackTimer <= 15 && attackTimer > 10) {
                    g.setColor(HITBOXCOLOURS[2]);
                    constants.Display.drawLine(g2, curAttack[0].hitboxes[0].forDrawLaser());
                    g.setColor(HITBOXCOLOURS[3]);
                    constants.Display.drawLine(g2, curAttack[1].hitboxes[0].forDrawLaser());
                    constants.Display.drawLine(g2, curAttack[2].hitboxes[0].forDrawLaser());
                    //two black beams, one red beam
                } else if (attackTimer <= 10 && attackTimer > 5) {
                    g.setColor(HITBOXCOLOURS[2]);
                    constants.Display.drawLine(g2, curAttack[1].hitboxes[0].forDrawLaser());
                    g.setColor(HITBOXCOLOURS[3]);
                    constants.Display.drawLine(g2, curAttack[2].hitboxes[0].forDrawLaser());
                    // one black beam, one red beam
                } else if (attackTimer <= 5) {
                    g.setColor(HITBOXCOLOURS[2]);
                    constants.Display.drawLine(g2, curAttack[2].hitboxes[0].forDrawLaser());
                    // one red beam
                }
            } else if (attackType == 2) {
                if (attackTimer > 5) {
                    g.setColor(HITBOXCOLOURS[3]);
                    //set black
                } else {
                    g.setColor(HITBOXCOLOURS[2]);
                    //set red
                }
                for (Attack a : curAttack) {
                    constants.Display.drawLine(g2, a.hitboxes[0].forDrawLaser());
                }
                //draw
            } else {
                if (attackTimer > 5) {
                    g.setColor(HITBOXCOLOURS[3]);
                    //set black
                } else {
                    g.setColor(HITBOXCOLOURS[2]);
                    //set red
                }
                constants.Display.drawCircle(g, new Circle(curAttack[0].hitboxes[0].getLocation(), curAttack[0].hitboxes[0].getRadius()));
                //draw
            }
        }

        g.setColor(HITBOXCOLOURS[2]);
        constants.Display.drawCircle(g, new Circle(location, getRadius()));
    }

    public void update() {
        if (wanderTimer == 0 && wandering == false) {
            attack();
            wanderTimer = 60;
        }
        wander();
        if (attackTimer > 0) --attackTimer;
        System.out.println(attackTimer);
        if ((attackTimer < 5 && attackTimer != 0 && attackType != 1) || (attackType == 1 && (attackTimer <= 15)) ) {
            for (Attack a : curAttack) {
                if(a.checkHit(new Circle(player.location, player.getRadius()/2)) && !hitPlayer) {
                    player.hit();
                    hitPlayer = true;
                }
            }
        }
        if (attackTimer == 0 || attackTimer == 5 || attackTimer == 10) {
            if (attackTimer == 0) attackType = 0;
            hitPlayer = false;
        } else {
            if (attackType == 1) {
                if (attackTimer == 30 || attackTimer == 25 || attackTimer == 20) {
                    curAttack[(30-attackTimer)/5].hitboxes = new Shape[]{setBeam(player.location)};
                } else if (attackType == 3) {
                    if (attackTimer > 5) {
                        curAttack[0].hitboxes[0].setRadius(5);
                    }
                }
            }
        }

        move();
    }

    public void wander() {
        if (wanderTimer == 0) {
            if (wandering) {
                dir[0] = 0;
                dir[1] = 0;
                wandering = false;
                return;
            } else {
                dir[0] = (float)(Math.random()-0.5)*3;
                dir[1] = (float)(Math.random()-0.5)*3;

                if (location.x > constants.Display.WIDTH - radius*3) dir[0] = -0.5f;
                if (location.x < radius*3) dir[0] = 0.3f;
                if (location.y > constants.Display.HEIGHT - radius*3/2) dir[1] = -0.3f;
                if (location.y < radius*3/2) dir[1] = 0.3f;

                wanderTimer = 150;
            }
            wandering = true;
        }
        --wanderTimer;
    }

    public void attack() {
        if (Math.round(Math.random()) == 0) {
            System.out.println("triple");
            tripleBeam();
        } else {
            System.out.println("triple");
            tripleBeam();
        }
    }

    public void tripleBeam() {
        attackType = 1;
        attackTimer = 45;
        curAttack = new Attack[] {new Attack(), new Attack(), new Attack()};
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
        return new Rectangle(location, path, 20, 3000*(int)(Math.abs(difX)/difX));
    }

    public void eightBeam() {
        attackType = 2;
        attackTimer = 40;
        curAttack = new Attack[8];
        int set = 0;
        for (int i = -50; i <= 50; i += 50) {
            for (int j = -50; j <= 50; j += 50) {
                if (i != 0 && i != 0) {
                    curAttack[set] = new Attack();
                    curAttack[set].hitboxes = new Shape[]{setBeam(new Point_ (location.x + i, location.y + j))};
                    set++;
                }
            }
        }
    }

    public void hitResponse() {
        attackType = 3;
        attackTimer = 30;
        curAttack = new Attack[] {new Attack()};
        curAttack[0].hitboxes = new Shape[]{new Circle(location, 1)};
    }

    @Override
    public boolean takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp > this.maxHp) this.hp = this.maxHp;
        hitResponse();
        wanderTimer = 60;
        wandering = false;
        return this.hp > 0;
    }

    public String toString() {
        return "Laserman";
    }
}