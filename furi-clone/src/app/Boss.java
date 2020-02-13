package app;

public abstract class Boss extends Unit {

    public Attack curAttack;
    public float dir[] = new float[]{0, 0};
    public Unit player;
    
    Boss(int hp, int dmg, int x, int y, int rad, Float s, Unit p) {
        super(hp, dmg, x, y, rad, s);
        player = p;
    }

    public abstract void update();

    public void move() {
        this.location.x += dir[0]*spd;
        this.location.y += dir[1]*spd;
    }

    //Attacks for each boss will be methods that set curAttack shapes[] based on player coords and boss coords.
    
}