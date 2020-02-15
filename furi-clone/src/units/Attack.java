package units;
import shapes.*;

public class Attack {
    public Shape[] hitboxes;

    public void setShapes(Circle[] s) {
        Circle[] set = new Circle[s.length];
        for (Circle shape: s) {
            set[0] = shape;
        }
        hitboxes = set; 
    }

    public boolean checkHit(Circle check) {
        for (Shape s: hitboxes) {
            if (s.checkOverlap(check)) return true;
        }
        return false;
    }
}