package app;

public class Attack {
    public Circle[] hitboxes;

    public void setShapes(Circle[] s) {
        Circle[] set = new Circle[s.length];
        for (Circle shape: s) {
            set[0] = shape;
        }
        hitboxes = set; 
    }

    public boolean checkHit(Shape check) {

        /*

        here is the time for some ridiculous maths

        going to check every shape in the attack against the checked shape

        if even a single one returns true, return true; otherwise return false;

        */

        return false;
    }

    
}