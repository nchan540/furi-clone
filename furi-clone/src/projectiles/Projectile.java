package projectiles;
import graph.*;
import shapes.*;

public abstract class Projectile {
    Shape hitbox;
    float xSpeed;
    float ySpeed;
    Line direction;

    public Projectile(Shape hitbox, float speed, Line direction) {
        this.hitbox = hitbox;
        
        this.setSpeed(speed, direction);
    }

    public void setSpeed(float speed, Line direction) {
        this.direction = direction;
        
        float deltaY = direction.getY(1) - direction.getY(0);
        float deltaX = 1;

        // 1 (deltaX) squared is 1, and deltaX will always be 1, so the value can be put it to improve speed
        // the ratio of speed : hypotenuse is the same as the ratio of xSpeed : deltaX and ySpeed : deltaY
        float ratio = speed / (float)(Math.abs(Math.sqrt(1 + Math.pow(deltaY, 2))));
        // speed / xSpeed = deltaX / ratio
        this.xSpeed = deltaX * ratio;
        this.ySpeed = deltaY * ratio;
    }

    public void move() {
        this.hitbox.getLocation().x += xSpeed;
        this.hitbox.getLocation().y += ySpeed;
    }

    public void move(Point_ destination) {
        this.hitbox.getLocation().x = destination.x;
        this.hitbox.getLocation().y = destination.y;
    }

    public abstract boolean hitDetect(Shape target);
}