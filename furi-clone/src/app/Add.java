package app;

public abstract class Add extends Unit {
    
    public Add(int dmg, int x, int y, int radius, float spd) {
        // Ads die in one hit
        super(Constants.Add.HEALTH, Constants.Add.HEALTH, dmg, x, y, radius, spd);
    }

    public void attack() {

    }
}