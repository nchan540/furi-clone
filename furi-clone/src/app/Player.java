package app;

public class Player extends Unit {

    public int iFrames = 0;
    public boolean dramaticPause = false;

    Player(int x, int y, int r) {
            super (Constants.Player.HEALTH, Constants.Player.DAMAGE, x, y, r, Constants.Player.SPEED);
        }

    public void update() {
        if (iFrames > 0) --iFrames;
        if (location.y > Constants.Graphics.HEIGHT - radius/2) location.y = Constants.Graphics.HEIGHT - radius/2;
        if (location.y < radius/2) location.y = radius/2;
        if (location.x > Constants.Graphics.WIDTH - radius/2) location.x = Constants.Graphics.WIDTH-radius/2;
        if (location.x < radius/2) location.x = radius/2;
    }

    public void dash(int x, int y) {
        spd = Constants.Player.DASH_DISTANCE;
        move(x, y);
        spd = Constants.Player.SPEED;
    }
    
    public void hit () {
        if(iFrames == 0) {hp -= 1;iFrames = 60;dramaticPause = true;}
    }
}