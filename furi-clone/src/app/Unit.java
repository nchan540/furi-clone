package app;

public abstract class Unit {
    
    protected int maxHp;
    protected int hp;
    protected int dmg;
    protected Point location;
    protected float radius;
    protected float spd;

    public Unit(int maxHp, int dmg, int x, int y, int radius, float spd) {
        this.maxHp = maxHp;
        this.hp = this.maxHp;
        this.dmg = dmg;
        this.location.x = x;
        this.location.y = y;
        this.radius = radius;
        this.spd = spd;
    }

    public int GetX() { return Math.round(this.location.x); }

    public int GetY() { return Math.round(this.location.y); }

    public int GetRadius() { return Math.round(this.radius); }

    public int GetMaxHP() { return this.maxHp; }
    
    public int GetHP() { return this.hp; }

    public int GetDmg() { return this.dmg; }

    public float GetSpeed() { return this.spd; }

    /**
     * Moves the unit by its speed value
     * @param moveX -1 if moving left, 0 if not moving, 1 if moving right.
     * @param moveY -1 if moving up, 0 if not moving, 1 if moving down.
     * @return if move was successful (parameters within bounds)
     */
    public boolean Move(int moveX, int moveY) {
        if (moveX < -1 || moveX > 1 || moveY < -1 || moveY > 1) return false;
        
        float moveSpd = this.spd;
        if (moveX != 0 && moveY != 0) {
            moveSpd = (float)Math.pow(Math.pow(this.spd, 2) / 2, 0.5);
        }

        this.location.x += moveSpd * moveX;
        this.location.y += moveSpd * moveY;
        return true;
    }

    /**
     * @param damage value being subtracted from hp 
     * @return true if piece has positive hp, false if piece has 0 or negative hp
     */
    public boolean takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp > this.maxHp) this.hp = this.maxHp;
        return this.hp > 0;
    }
}