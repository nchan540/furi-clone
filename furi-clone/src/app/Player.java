package app;

public class Player extends Unit {
    Player(int x, int y, int r) {
            super (Constants.Player.HEALTH, Constants.Player.DAMAGE, x, y, r, Constants.Player.SPEED);
        }

    public void dash(int x, int y) {
        spd = Constants.Player.DASH_DISTANCE;
        move(x, y);
        spd = Constants.Player.SPEED;
    }
    
}