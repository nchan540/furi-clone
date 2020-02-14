package app;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

public class Brawler extends Boss {

    public Attack curAttack = new Attack();
    public int wanderTimer = 150;
    public int chaseTime, stunTime, attackTime = 0;
    public boolean wandering, chasing, attacking, hitPlayer, blink = false;

    Brawler(int x, int y, Player p) {
        super(Constants.Brawler.HEALTH, 0, 1, x, y, 125, 0.5f, p);
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
                this.location = curAttack.hitboxes[0].getLocation();
            }
            if (attackTime > 0 && attackTime <= 10) {
                if(curAttack.checkHit(new Circle(player.location, player.getRadius()/2)) && !hitPlayer) {
                    player.hit();
                    if (blink) {player.takeDamage(1); blink = false;}
                    hitPlayer = true;
                }
            }
        }

        if (location.y > Constants.Graphics.HEIGHT - radius + 10) {
            location.y = Constants.Graphics.HEIGHT - radius + 10; 
        }
        if (location.y < radius/2) {
            location.y = radius/2; 
        }
        if (location.x > Constants.Graphics.WIDTH - radius + 35) {
            location.x = Constants.Graphics.WIDTH-radius + 35; 
        }
        if (location.x < radius/2) {
            location.x = radius/2; 
        }
    }

    public void draw(Graphics g, Graphics2D g2, Color[] HITBOXCOLOURS) {
        if (attackTime > 0) {
            if (attackTime > 10) g.setColor(HITBOXCOLOURS[3]);
            else g.setColor(HITBOXCOLOURS[2]);
            
            Constants.drawCircle(g, new Circle(curAttack.hitboxes[0].getLocation(), curAttack.hitboxes[0].getRadius()*2));
        }
        Constants.drawCircle(g, new Circle(location, getRadius()));
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

                if (location.x > Constants.Graphics.WIDTH - radius*3) dir[0] = -1.5f;
                if (location.x < radius*3) dir[0] = 1.5f;
                if (location.y > Constants.Graphics.HEIGHT - radius*3/2) dir[1] = -1.0f;
                if (location.y < radius*3/2) dir[1] = 1.0f;

                wanderTimer = 150;
            }
            wandering = !wandering;
        }
        --wanderTimer;
    }

    public void chase() {
        if (Constants.distanceFormula(location, player.location) < (radius * 3)) {
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
        if (Math.floor(Math.random() * 2) == 0)  {
            blinkAttack();
        } else {
            tantrum();
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

        if (newY > Constants.Graphics.HEIGHT - radius + 10) {
            newY = Constants.Graphics.HEIGHT - radius + 10; 
        }
        if (newY < radius/2) {
            newY = radius/2; 
        }
        if (newX > Constants.Graphics.WIDTH - radius + 35) {
            newX = Constants.Graphics.WIDTH-radius + 35; 
        }
        if (newX < radius/2) {
            newX = radius/2; 
        }

        blink = true;
        curAttack.hitboxes = new Circle[]{new Circle (new Point_(newX, newY),(int)radius/2)};
    }

    public void lineAttack() {
        
    }

    public void tantrum() {
        curAttack.hitboxes = new Circle[]{new Circle(location, getRadius()*3/2)};
    }


    public String toString() {
        return "The Brawler";
    }
}