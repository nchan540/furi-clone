package units;

public abstract class Add extends Unit {
    
    public Add(int dmg, int x, int y, int radius, float spd) {
        // Ads die in one hit
        super(constants.Add.HEALTH, constants.Add.HEALTH, dmg, x, y, radius, spd);
    }

    public void attack() {

    }

    public void kill() {}
}