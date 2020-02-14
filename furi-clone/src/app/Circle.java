package app;

public class Circle implements Shape {

    public Point_ p;
    public int diameter;

    Circle(Point_ p, int r) {
        this.p = new Point_(p.x, p.y);
        this.diameter = r;
    }

    /**
     * Constructor for a circle of radius r located at P(x, y)
     * @param x x coordinate of circle
     * @param y y coordinate of circle
     * @param r radius of circle
     */
    Circle(float x, float y, int r) {
        this.p = new Point_(x, y);
        this.diameter = r;
    }

    /**
     * Checks if overlap occurs with another circle
     * @param target circle being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(Circle target) {
        double targetRadius = Math.pow(target.getArea() / Math.PI, 1.0/2);
        if (this.getDistance(target.p) <= targetRadius) return true;
        return false;
    }

    /**
     * Checks if overlap occurs with a line segment
     * @param target line segment being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(LineSegment target) {
        LineSegment tangent = new LineSegment(new Line(-1 / target.equation.m, this.p), this.p, this.diameter/2);

        return (tangent.checkOverlap(target));
    }

    /**
     * Finds the distance between a point P and the point on the circle closest to point P
     * @param p point being checked
     * @return distance between the point and the circle's closest point
     */
    public float getDistance(Point_ p) {
        return (float)(Math.sqrt(Math.pow(this.p.x - p.x, 2) + Math.pow(this.p.y-p.y, 2))) - this.diameter;
    }

    /**
     * Gets the location of the circle
     * @return location of the circle
     */
    public Point_ getLocation() { return this.p; }

    /**
     * Gets the area of the circle
     * @return area of the circle
     */
    public double getArea() { return Math.pow(this.diameter/2, 2) * Math.PI;}

    /**
     * Gets the x coordinates at a y location
     * @return array of the 2 x coordinates
     */
    public float[] getXatY(float y) {
        if(Math.abs(y-p.y) > diameter) return new float[]{-1000};
        float[] coords = new float[2];
        coords[0] = (float)(p.x + Math.sqrt((diameter^2) - (Math.pow((y - p.y), 2))));
        coords[1] = (float)(p.x - Math.sqrt((diameter^2) - (Math.pow((y - p.y), 2))));
        return coords;
    }

    /**
     * Gets the y coordinates at an x location
     * @return array of the 2 y coordinates
     */
    public float[] getYatX(float x) {

        if(Math.abs(x-p.x) > diameter) return new float[]{-1000};

        float[] coords = new float[2];
        coords[0] = (float)(p.y + Math.sqrt((diameter^2) - (Math.pow((x - p.x), 2))));
        coords[1] = (float)(p.y - Math.sqrt((diameter^2) - (Math.pow((x - p.x), 2))));
        return coords;
    }
}