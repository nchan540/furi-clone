package shapes;
import graph.*;
import java.awt.Point;

public class Circle implements Shape {

    public Point_ p1;
    public int diameter;

    public Circle(Point_ p, int r) {
        this.p1 = new Point_(p.x, p.y);
        this.diameter = r;
    }

    /**
     * Constructor for a circle of radius r located at P(x, y)
     * @param x x coordinate of circle
     * @param y y coordinate of circle
     * @param r radius of circle
     */
    public Circle(float x, float y, int r) {
        this.p1 = new Point_(x, y);
        this.diameter = r;
    }

    /**
     * Checks if overlap occurs with another circle
     * @param target circle being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(Circle target) {
        if (this.getDistance(target.p1) <= target.diameter/2) return true;
        return false;
    }

    /**
     * Checks if overlap occurs with a line segment
     * @param target line segment being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(LineSegment target) {
        /*LineSegment tangent1 = new LineSegment(new Line(-1 / target.equation.m, this.p1), this.p1, this.diameter/2f);
        LineSegment tangent2 = new LineSegment(new Line(-1 / target.equation.m, this.p1), this.p1, -1 * this.diameter/2f);

        return (tangent1.checkOverlap(target)) || (tangent2.checkOverlap(target));*/

        // parameters: ax ay bx by cx cy r
        float p1x = target.p1.x - p1.x;
        float p1y = target.p1.y - p1.y;
        float p2x = target.p2.x - p1.x;
        float p2y = target.p2.y - p1.y;
        double a = Math.pow((p2x - p1x), 2) + Math.pow((p2y - p1y),2);
        float b = 2*(p1x*(p2x - p1x) + p1y*(p2y - p1y));
        double c = Math.pow(p1x, 2) + Math.pow(p1y, 2) - Math.pow((diameter/2),2);
        double disc = Math.pow(b, 2) - 4*a*c;
        if(disc <= 0) return false;
        double sqrtdisc = Math.sqrt(disc);
        double t1 = (-b + sqrtdisc)/(2*a);
        double t2 = (-b - sqrtdisc)/(2*a);
        if((0 < t1 && t1 < 1) || (0 < t2 && t2 < 1)) return true;
        return false;
    }

    /**
     * Finds the distance between a point P and the point on the circle closest to point P
     * @param p point being checked
     * @return distance between the point and the circle's closest point
     */
    public float getDistance(Point_ p) {
        return (float)(Math.sqrt(Math.pow(this.p1.x - p.x, 2) + Math.pow(this.p1.y-p.y, 2))) - this.diameter/2;
    }

    /**
     * Gets the location of the circle
     * @return location of the circle
     */
    public Point_ getLocation() { return this.p1; }

    /**
     * Gets the area of the circle
     * @return area of the circle
     */
    public double getArea() { return Math.pow(this.diameter/2, 2) * Math.PI;}

    public int getRadius() {return diameter;}

    /**
     * Gets the x coordinates at a y location
     * @return array of the 2 x coordinates
     */
    public float[] getXatY(float y) {
        if(Math.abs(y-p1.y) > diameter) return new float[]{-1000};
        float[] coords = new float[2];
        coords[0] = (float)(p1.x + Math.sqrt((diameter^2) - (Math.pow((y - p1.y), 2))));
        coords[1] = (float)(p1.x - Math.sqrt((diameter^2) - (Math.pow((y - p1.y), 2))));
        return coords;
    }

    /**
     * Gets the y coordinates at an x location
     * @return array of the 2 y coordinates
     */
    public float[] getYatX(float x) {

        if(Math.abs(x-p1.x) > diameter) return new float[]{-1000};

        float[] coords = new float[2];
        coords[0] = (float)(p1.y + Math.sqrt((diameter^2) - (Math.pow((x - p1.x), 2))));
        coords[1] = (float)(p1.y - Math.sqrt((diameter^2) - (Math.pow((x - p1.x), 2))));
        return coords;
    }

    public LineSegment[] getLines() {
        return null;
    }
    public void increaseRadius(int add) {
        diameter += add;
    }
    public LineSegment forDrawLaser() {
        return null;
    }

    public boolean checkBounds(Point check) {
        return (Point_.distanceFormula(check, this.getLocation()) < this.getRadius());
    } 

    public void refresh() {}
}