package app;

public class Charger extends Boss {

    public Attack curAttack;

    Charger(int x, int y, Unit p) {
        super(300, 1, x, y, 30, 2f, p);
    }

    public void update() {
        changeDir();
        move();
    }

    public void changeDir() {

        if (!(player.location.x == this.location.x && player.location.y == this.location.y)) {
        float difX = (player.location.x-this.location.x);
        System.out.println(difX);
        float difY = (player.location.y-this.location.y);
        System.out.println(difY);

        float r = Math.round(Math.sqrt(Math.pow(difX, 2) + Math.pow(difY, 2)));
        System.out.println(r);
        float[] changeSpeed = new float[]{(difY / (r / spd)), (difX / (r/spd))};
        dir[0] += changeSpeed[0];
        dir[1] += changeSpeed[1];

        dir[0] *= 0.5;
        dir[1] *= 0.5;
        }
    }
    
}