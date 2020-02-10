package app;

public abstract class Add extends Unit {
    
    public Add(int dmg, int x, int y, int radius, float spd) {
        // Ads die in one hit
        super(1, dmg, x, y, radius, spd);
    }

    public void Attack() {
        
    }
}