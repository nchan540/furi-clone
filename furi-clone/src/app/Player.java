package app;

public class Player extends Unit {

    public int iFrames = 0;
    public int attackFrames = 0;
    public boolean dramaticPause = false;

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

        if (attackFrames > 0) {
            if (attackFrames > 10 && attackFrames <= 20) {
                //active frames, check for impact
            }
            if (attackFrames-- <= 1) spd = Constants.Player.SPEED;
        }

    }

    public void attack() {
        if (attackFrames <= 0) {
            spd = 0;
            attackFrames = 40;
        }
    }

    public void dash(int x, int y) {
        float prevSpd = spd;
        spd = Constants.Player.DASH_DISTANCE;
        move(x, y);
        spd = prevSpd;
    }

    public void setAttack(Point_ p) {
        if (!(Math.abs(p.x - this.getX()) < 1 && Math.abs(p.y-this.getY()) < 1)) {

            float difX = (p.x-this.location.x);
            float difY = (p.y-this.location.y);
            float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));

            for (int i = 0; i < 5; ++i) {
                curAttack.hitboxes[i] = new Circle(new Point_(location.x + difX * ATTACK_LENGTHS[i] / r, location.y + difY * ATTACK_LENGTHS[i] / r), (25 - 3*i));
            }
        }
    }


    
    public void hit () {
        if(iFrames == 0) {hp -= 1;iFrames = 60;dramaticPause = true;}
    }
}