package app;
import java.util.HashSet;

public class Player extends Unit {

    float difX, difY, difR;

    public int iFrames, attackFrames, score = 0;
    public boolean dramaticPause, dashQueued = false;
    public int[] queueXY = {0, 0};
    public HashSet<Unit> attacked = new HashSet<>();

    public Attack curAttack = new Attack();

    public final int[] ATTACK_LENGTHS = {40, 65, 87, 106, 123};

    Player(int x, int y, int r) {
        super (Constants.Player.HEALTH, Constants.Player.DAMAGE, x, y, r, Constants.Player.SPEED);
        curAttack.hitboxes = new Circle[5];
    }

    public void update() {
        if (iFrames > 0) --iFrames;
        if (location.y > Constants.Graphics.HEIGHT - radius - 15) location.y = Constants.Graphics.HEIGHT - radius - 15;
        if (location.y < radius/2) location.y = radius/2;
        if (location.x > Constants.Graphics.WIDTH - radius + 10) location.x = Constants.Graphics.WIDTH-radius+10;
        if (location.x < radius/2) location.x = radius/2;

        System.out.println(dashQueued);

        setAttack();
        if (attackFrames > 0 && attackFrames-- <= 1) {
            if (!attacked.isEmpty()) {
                for (Unit u : attacked) {
                    if (!u.takeDamage(this.dmg) && u instanceof Boss && hp < 3) {
                        ++hp;
                        ++score;
                    }
                }
            }
            spd = Constants.Player.SPEED;
            attacked.clear();
            if (dashQueued) {
                dash(queueXY[0], queueXY[1]);
                dashQueued = false;
            }
        }
    }

    public void attack() {
        if (attackFrames <= 0) {
            spd = 0;
            attackFrames = 30;
        }
    }

    public void dash(int x, int y) {
        if (attackFrames <= 5 || attackFrames > 10) {
            float prevSpd = spd;
            spd = Constants.Player.DASH_DISTANCE;
            move(x, y);
            spd = prevSpd;
        } else {
            dashQueued = true;
            queueXY = new int[]{x, y};
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
}