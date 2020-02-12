package app;

public class Charger extends Boss {

    public Attack curAttack;

    Charger(int h, int x, int y, Unit p) {
        super(h, 1, x, y, 100, 1f, p);
    }

    public void update() {
        changeDir();
        move();
        if (location.y > Constants.Graphics.HEIGHT - radius + 10) {location.y = Constants.Graphics.HEIGHT - radius + 10;}
        if (location.y < radius/2) {location.y = radius/2; dir[1]  = -dir[1] * 0.1f;}
        if (location.x > Constants.Graphics.WIDTH - radius + 35) {location.x = Constants.Graphics.WIDTH-radius + 35;}
        if (location.x < radius/2) {location.x = radius/2; dir[0] = -dir[0] * 0.1f;}
    }

    public void changeDir() {

        if (!(Math.abs(player.getX() - this.getX()) < 3 && Math.abs(player.getY()-this.getY()) < 3)) {
        float difX = (player.location.x-this.location.x);
        float difY = (player.location.y-this.location.y);

        float r = (float)(Math.abs(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2))));
        float[] changeSpeed = new float[]{(difX*spd/r), difY*spd/r};
        dir[0] += changeSpeed[0]*0.35;
        dir[1] += changeSpeed[1]*0.35;

        dir[0] *= 0.97;
        dir[1] *= 0.97;
        }
    }
    
}