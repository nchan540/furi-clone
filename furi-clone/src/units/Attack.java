package units;
import shapes.*;

public class Attack {
    // Targets that can be hit
    public Shape[] hitboxes;

    public void setShapes(Circle[] s) {
        Circle[] set = new Circle[s.length];
        for (Circle shape: s) {
            // I don't know why it's like this, ask Damien
            set[0] = shape;
        }
        hitboxes = set; 
    }

    /**
     * Checks if any of the targets have been hit
     */
    public boolean checkHit(Circle check) {
        for (Shape s: hitboxes) {
            if (s.checkOverlap(check)) return true;
        }
        return false;
    }
}