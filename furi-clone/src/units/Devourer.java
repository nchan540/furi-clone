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
    public Circle targeting;
    
    //always wandering; 0 = passive, 1 = performing an attack, 2 = some hitboxes are active
    public int phase = 0;
    public int wanderTimer = 30;
    //0 == nothing, 1 == shoot(), 2 == sweep();
    public int attack = 0;

    public int ID = 3;

    public ReturningBounce[] bullets = new ReturningBounce[constants.Devourer.MAXBULLETS];
    public int bulletIndex = 0;
    public boolean first = true;
    public boolean done = true;
    public int queued = 0;

    public Devourer(int x, int y, Player p) {
        super(constants.Devourer.HEALTH, 0, 1, x, y, 70, 1f, p);
        curAttack = new Attack();
        curAttack.hitboxes = new Shape[1];
        targeting = new Circle(x, y, 30);
    }

    public void update() {

        if (--wanderTimer == 0) {
            wanderTimer = 120;
            wander();
        }
        
        move();
        target();

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
            } else if (attack == 2){
                sweep();
                if (attackTimer > 0 && attackTimer < 90) {
                    if(curAttack.checkHit(player.hitbox)) {
                        player.takeDamage(1);
                    }
                }
            } else if (attack == 3) {
                focus();
                if (attackTimer > 0 && attackTimer < 150) {
                    if(curAttack.checkHit(player.hitbox)) {
                        player.takeDamage(1);
                    }
                }
            }
            if (--attackTimer == 0) {
                attackDelay = 60;
                phase = 0;
                bulletIndex += queued;
                queued = 0;
            } 
        }

        System.out.println("bullets left: " + bulletIndex);
        System.out.println("expected loss: " + queued);

        clearBullets();

        for (int j = 0; j < bullets.length; ++j) {
            if (bullets[j] != null) {
                bullets[j].move();
                bullets[j].update();
                boolean[] hits = bullets[j].hitDetect();
                boolean hit = false;
                for (int i = 0; i < hits.length; ++i) {
                    if (hits[i]) {
                        bullets[j].targets[i].takeDamage(bullets[j].damage);
                        hit = true;
                    }
                }
                if (hit) {
                    bullets[j] = null;
                    if (!done) --queued; 
                    else --bulletIndex;
                }
            }
        }

    }

    public void target() {
            float difX = (player.hitbox.p1.x-targeting.p1.x);
            float difY = (player.hitbox.p1.y-targeting.p1.y);

            float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
            float[] changeSpeed = new float[]{(difX*8/r), difY*8/r};

            targeting.p1.x += changeSpeed[0];
            targeting.p1.y += changeSpeed[1];
    }

    public float[] refocus() {
        float difX = (targeting.p1.x-hitbox.p1.x);
        float difY = (targeting.p1.y-hitbox.p1.y);

        float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
        return new float[] {hitbox.p1.x+difX*2000/r, hitbox.p1.y+difY*2000/r};
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
        if (bulletIndex == 0) {attack = 1; attackTimer = 45;done = false;}
        else if (Math.round(Math.random()) == 0) attack = 2;
        else attack = 3;
    }

    public void shoot() {
        if (--attackTimer <= 0) {
            Point_ target = new Point_ (player.hitbox.p1.x, player.hitbox.p1.y);
            bullets[bulletIndex] = new ReturningBounce(new Unit[] {player}, new Circle(hitbox.p1, constants.Add.BULLETSIZE + 5), constants.Add.BULLETSPEED, new Line(hitbox.p1, target), this);
            if (target.x < hitbox.p1.x) bullets[bulletIndex].setSpeed(-constants.Add.BULLETSPEED);
            if(++bulletIndex != constants.Devourer.MAXBULLETS) attackTimer = 10;
            else {attackTimer = 1; done = true;}
        }
    }

    public void focus() {
        if (phase == 1) {
            attackTimer = 210;
            phase = 2;
            curAttack.hitboxes[0] = new Rectangle(this.hitbox.getLocation(), new Line(this.hitbox.getLocation(), targeting.p1), 10, 2000);
        } else if (phase == 2 && attackTimer < 150) {
        }
        curAttack.hitboxes[0].forDrawLaser().p1 = hitbox.p1;
        curAttack.hitboxes[0].forDrawLaser().p2.set(refocus());
        curAttack.hitboxes[0].refresh();
    }

    public void sweep() {
        if (phase == 1) {
            attackTimer = 120;
            phase = 2;
            curAttack.hitboxes[0] = new Rectangle(this.hitbox.getLocation(), new Line(this.hitbox.getLocation(), new Point_(this.hitbox.getLocation().x+1, this.hitbox.getLocation().y-100)), 10, 2000);
        } else if (phase == 2 && attackTimer < 90) {
            curAttack.hitboxes[0].forDrawLaser().p2.x = hitbox.p1.x + (float)Math.sin((90-attackTimer-1)*Math.PI/44)*2000;
            curAttack.hitboxes[0].forDrawLaser().p2.y = hitbox.p1.y + (float)-Math.cos((90-attackTimer-1)*Math.PI/44)*2000;
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

        if (attack > 1) {
            if (attackTimer > 0) {
                if ((attackTimer >= 90 && attack == 2) || (attackTimer >= 150 && attack == 3)) {
                    g.setColor(HITBOXCOLOURS[3]);
                } else {
                    g.setColor(HITBOXCOLOURS[2]);
                }
                g2.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                constants.Display.drawLine(g2, curAttack.hitboxes[0].forDrawLaser());
                if (attack == 3) {
                    g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                    constants.Display.drawLine(g2, (int)(targeting.p1.x -20), (int)(targeting.p1.y -20), (int)(targeting.p1.x +20), (int)(targeting.p1.y +20));
                    constants.Display.drawLine(g2, (int)(targeting.p1.x -20), (int)(targeting.p1.y +20), (int)(targeting.p1.x +20), (int)(targeting.p1.y -20));
                }
            }
        }

        
        for (ReturningBounce b : bullets) {
            if (b != null) {
                if (b.returning()) g.setColor(Color.RED);
                else if (b.isAlly()) g.setColor(HITBOXCOLOURS[0]);
                else g.setColor(Color.PINK);
                
                constants.Display.drawCircle(g, b.hitbox);
            }
        }

        if (attack != 1 || bulletIndex != 0)g.setColor(HITBOXCOLOURS[2]);
        else g.setColor(Color.GREEN);
        constants.Display.drawCircle(g, this.hitbox);

    }

    public String toString() {
        return "The Devourer";
    }

    public Projectile[] getBullets() {
        return bullets;
    }

}