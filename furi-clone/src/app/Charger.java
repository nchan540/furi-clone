package app;

public class Charger extends Boss {

    public Attack curAttack;
    public int speedUp = 150;
    public int bonked = 0;
    public int bonkTimer = 0;

    Charger(int h, int x, int y, Unit p) {
        super(h, 1, x, y, 100, 1f, p);
    }

    public void update() {
        changeDir();
        move();
        if (location.y > Constants.Graphics.HEIGHT - radius + 10) {
            location.y = Constants.Graphics.HEIGHT - radius + 10; 
            dir[1]  = -dir[1] * Constants.Charger.STUNSPD; 
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = Constants.Charger.STUNSPD; 
                this.bonked = Constants.Charger.STUNTIME + (int)spd * 50;
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (location.y < radius/2) {
            location.y = radius/2; 
            dir[1]  = -dir[1] * Constants.Charger.STUNSPD; 
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = Constants.Charger.STUNSPD; 
                this.bonked = Constants.Charger.STUNTIME + (int)spd * 50;
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (location.x > Constants.Graphics.WIDTH - radius + 35) {
            location.x = Constants.Graphics.WIDTH-radius + 35; 
            dir[0] = -dir[0] * Constants.Charger.STUNSPD;
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = Constants.Charger.STUNSPD; 
                this.bonked = Constants.Charger.STUNTIME + (int)spd * 50;
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (location.x < radius/2) {
            location.x = radius/2; 
            dir[0] = -dir[0] * Constants.Charger.STUNSPD; 
            if (this.bonked == 0 && bonkTimer == 0) {
                this.spd = Constants.Charger.STUNSPD; 
                this.bonked = Constants.Charger.STUNTIME + (int)spd * 50;
                this.bonkTimer = 60;
            }
            speedUp = 150;
        }
        if (--speedUp <= 0 && this.spd <= 1.8f) {this.spd += Constants.Charger.SPEEDUP; speedUp = 150;}

        if (this.bonked > 0) {
            --bonked;
        } else if (spd == Constants.Charger.STUNSPD && this.bonked == 0){
            spd = 1f;
        } else if (bonkTimer > 0) {
            --bonkTimer;
        }
    }

    public void changeDir() {
        if (bonked == 0){
            if (!(Math.abs(player.getX() - this.getX()) < 3 && Math.abs(player.getY()-this.getY()) < 3)) {
            float difX = (player.location.x-this.location.x);
            float difY = (player.location.y-this.location.y);

            float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
            float[] changeSpeed = new float[]{(difX*spd/r), difY*spd/r};
            dir[0] += changeSpeed[0]*(0.35 - (spd/15)) ;
            dir[1] += changeSpeed[1]*(0.35 - (spd/15));

            dir[0] *= 0.97;
            dir[1] *= 0.97;
            }
        }
    }

    public String toString() {
        return "The Charger";
    }
}