package app;

public class Player extends Unit {
    Player(int x, int y, int r) {
            super (3, 100000, x, y, r, 10);
        }

    public void dash(int x, int y) {
        spd *= 10;
        move(x, y);
        spd /= 10;
    }
    
}