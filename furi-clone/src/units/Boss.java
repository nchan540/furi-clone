package units;
import java.awt.Color;
import java.awt.Graphics;
public abstract class Boss extends Unit {

    public Attack curAttack;
    public float dir[] = new float[]{0, 0};
    public Player player;
    public int NUM;
    public boolean alive;
    
    public Boss(int maxHp, int hp, int dmg, int x, int y, int rad, Float s, Player p) {
        super(maxHp, hp, dmg, x, y, rad, s);
        player = p;
    }

    public abstract void update();

    public abstract void kill();

    public void move() {
        this.hitbox.p1.x += dir[0]*spd;
        this.hitbox.p1.y += dir[1]*spd;
    }

    public void spawn() {
        hp = maxHp;
        alive = true;
    }
    //Attacks for each boss will be methods that set curAttack shapes[] based on player coords and boss coords.
    
    public void drawUI(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.fillRect(x+10, y+5, 470, 40);
        g.setColor(Color.GRAY);
        g.fillRect(x, y, 470, 40);
        g.setColor(Color.BLACK);
        g.fillRect(x+5, y+5, 200, 30);
        g.fillRect(x+215, y, 10, 40);
        if (this.maxHp > 0) {
            g.setColor(Color.WHITE);
            g.drawString(this.toString(), x+10, y+30);
            g.setColor(Color.BLACK);
            g.fillRect(x+235, y+5, 225, 30);
            g.setColor(Color.RED);
            g.fillRect(x+235, y+5, (int)(225 * ((float)this.hp / this.maxHp)), 30);
        }
    }
}