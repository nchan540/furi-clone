package app;

public abstract class Boss extends Unit {

    public Attack curAttack;
    public float dir[] = new float[]{0, 0};
    public Player player;
    
    Boss(int maxHp, int hp, int dmg, int x, int y, int rad, Float s, Player p) {
        super(maxHp, hp, dmg, x, y, rad, s);
        player = p;
    }

    public abstract void update();

    public void move() {
        this.location.x += dir[0]*spd;
        this.location.y += dir[1]*spd;
    }

    public void spawn() {
        hp = maxHp;
    }

    //Attacks for each boss will be methods that set curAttack shapes[] based on player coords and boss coords.
    
}