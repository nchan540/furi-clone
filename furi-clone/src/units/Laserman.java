package units;
import shapes.*;
import graph.*;
import projectiles.Projectile;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class Laserman extends Boss {

    public int attackTimer, iFrames = 0;
    public Attack[] curAttack;
    public int attackType = 0;
    public int attackDelay = 120;
    public int rage = 0;

    public int wanderTimer = 150;
    public boolean wandering;

    public Laserman(int x, int y, Player p) {
        super(constants.Laserman.HEALTH, 0, 1, x, y, 150, 0.5f, p);
        alive = true;
        ID = 2;
    }

    public void spawn() {
        hp = maxHp;
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {
        if (attackTimer > 0) {
            g2.setStroke(new BasicStroke(40, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            if (attackType == 1) {
                if (attackTimer > 25) {
                    g.setColor(HITBOXCOLOURS[3]);
                    constants.Display.drawLine(g2, setBeam(player.hitbox.p1).forDrawLaser());
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
                    g2.setStroke(new BasicStroke(40, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
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
                constants.Display.drawCircle(g, new Circle(curAttack[0].hitboxes[0].getLocation(), curAttack[0].hitboxes[0].getRadius()*2));
                //draw
            }
        }
        if (iFrames > 0) {
            g.setColor(HITBOXCOLOURS[3]);
        } else if (rage > 0) {
            g.setColor(HITBOXCOLOURS[4]);
        }  else {
            g.setColor(HITBOXCOLOURS[2]);
        }
        constants.Display.drawCircle(g, this.hitbox);
    }

    public void update() {
        if (iFrames > 0) --iFrames;
        if (rage > 0) --rage;
        if (--attackDelay == 0) {
            attack();
        }
        wander();
        if (attackTimer > 0) --attackTimer;
        if (attackTimer != 0) {
            if ((attackType == 1)) {
                if (attackTimer <= 15 && attackTimer > 10) {
                    if(curAttack[0].checkHit(new Circle(player.hitbox.p1, player.getRadius()))) {
                        player.takeDamage(1);
                    }
                } else if (attackTimer <= 10 && attackTimer > 5) {
                    if(curAttack[1].checkHit(new Circle(player.hitbox.p1, player.getRadius()/2))) {
                        player.takeDamage(1);
                    }
                } else if (attackTimer <= 5) {
                    if(curAttack[2].checkHit(new Circle(player.hitbox.p1, player.getRadius()/2)) ) {
                        player.takeDamage(1);
                    }
                }
            } else if (attackTimer <= 5) {
                for (Attack a : curAttack) {
                    if(a.checkHit(new Circle(player.hitbox.p1, player.getRadius()/2))) {
                        player.takeDamage(1);
                    }
                }
            }
            if (attackType == 1) {
                if (attackTimer == 30 || attackTimer == 25 || attackTimer == 20) {
                    curAttack[(30-attackTimer)/5].hitboxes = new Shape[]{setBeam(player.hitbox.p1)};
                } 
            } else if (attackType == 3) {
                if (attackTimer > 5) {
                    curAttack[0].hitboxes[0].setRadius(2);
                }
            }
        }
        if (attackTimer == 0) {
            attackType = 0;
            move(); 
        }

        if (hp < 0 && this.alive) {kill();player.killedBoss = true;--player.bossesToKill;--player.bossesAlive;}
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

                if (hitbox.p1.x > constants.Display.WIDTH - hitbox.diameter*3) dir[0] = -0.5f;
                if (hitbox.p1.x < hitbox.diameter*3) dir[0] = 0.3f;
                if (hitbox.p1.y > constants.Display.HEIGHT - hitbox.diameter*3/2) dir[1] = -0.3f;
                if (hitbox.p1.y < hitbox.diameter*3/2) dir[1] = 0.3f;

                wanderTimer = 150;
            }
            wandering = true;
        }
        --wanderTimer;
    }

    public void attack() {
        if ((Math.round(Math.random()) == 0)) {
            tripleBeam();
        } else {
            eightBeam();
        }
        if (rage > 0) attackDelay = 50;
        else attackDelay = 120;
    }

    public void tripleBeam() {
        attackType = 1;
        attackTimer = 45;
        curAttack = new Attack[] {new Attack(), new Attack(), new Attack()};
    }

    public Rectangle setBeam(Point_ p) {
        float difX = (p.x-this.hitbox.p1.x);
        float difY = (p.y-this.hitbox.p1.y);
        // float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));

        Line path = new Line(difY/difX, hitbox.p1);

        // Point_ end = new Point_(
        //     hitbox.p1.x + difX * hitbox.diameter * 5, 
        //     hitbox.p1.y + difY * hitbox.diameter * 5
        // );
        return new Rectangle(hitbox.p1, path, 20, 1000*(int)(Math.abs(difX)/difX));
    }

    public void eightBeam() {
        attackType = 2;
        attackTimer = 40;
        curAttack = new Attack[8];
        int set = 0;
        for (int i = -50; i <= 50; i += 50) {
            for (int j = -50; j <= 50; j += 50) {
                if (!(i == 0 && j == 0)) {
                    curAttack[set] = new Attack();
                    curAttack[set].hitboxes = new Shape[]{setBeam(new Point_ (hitbox.p1.x + i + 1, hitbox.p1.y + j + 1))};
                    set++;
                }
            }
        }
    }

    public void hitResponse() {
        attackDelay = 50;
        attackType = 3;
        attackTimer = 50;
        curAttack = new Attack[] {new Attack()};
        curAttack[0].hitboxes = new Shape[]{new Circle(hitbox.p1, 200)};
    }

    @Override
    public boolean takeDamage(int damage) {
        if (iFrames == 0) {
            if (rage > 0) this.hp -= damage * 3 / 2;
            else this.hp -= damage;
            iFrames = 170;
            rage = 300;
            if (this.hp > this.maxHp) this.hp = this.maxHp;
            hitResponse();
            wanderTimer = 60;
            wandering = false;
        }
        return this.hp > 0;
    }

    public String toString() {
        return "Laserman";
    }

    @Override
    public void kill() {
        curAttack = new Attack[] {};
        attackTimer = 0;
        alive = false;
    }

    public Projectile[] getBullets() {
        return null;
    }

}