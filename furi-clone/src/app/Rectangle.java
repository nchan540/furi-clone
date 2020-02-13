package app;

public class Rectangle implements Shape {
    
    // [0] = top, [1] = left, [2] = right, [3] = bottom;
    public LineSegment[] lines = new LineSegment[4];

    /**
     * Constructor for a rectangle
     * @param topLeft point of top left corner
     * @param topRight point of top right corner
     * @param bottomLeft point of bottom left corner
     * @param bottomRight point of bottom right corner
     */
    public Rectangle(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        // Copy points
        Point tL = new Point(topLeft.x, topLeft.y);
        Point tR = new Point(topRight.x, topRight.y);
        Point bL = new Point(bottomLeft.x, bottomLeft.y);
        Point bR = new Point(bottomRight.x, bottomRight.y);

        this.lines[0] = new LineSegment(tL, tR);
        this.lines[1] = new LineSegment(tL, bL);
        this.lines[2] = new LineSegment(tR, bR);
        this.lines[3] = new LineSegment(bL, bR);
    }

    /**
     * Constructor for a rectangle
     * @param topLeft point of top left corner
     * @param width width of rectangle
     * @param height height of rectangle
     */
    public Rectangle(Point topLeft, float width, float height) {
        // Make points
        Point tL = new Point(topLeft.x, topLeft.y);
        Point tR = new Point(tL.x + width, tL.y);
        Point bL = new Point(tL.x, tL.y + height);
        Point bR = new Point(tL.x + width, tL.y + height);

        this.lines[0] = new LineSegment(tL, tR);
        this.lines[1] = new LineSegment(tL, bL);
        this.lines[2] = new LineSegment(tR, bR);
        this.lines[3] = new LineSegment(bL, bR);
    }

    /**
     * Checks if overlap occurs with a circle
     * @param target circle being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(Circle target) {
        for (int i = 0; i < lines.length; ++i) {
            if (target.checkOverlap(this.lines[i])) return true;
        }
        return false;
    }

    /**
     * Checks if overlap occurs with a line segment
     * @param target line segment being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(LineSegment target) {
        for (int i = 0; i < lines.length; ++i) {
            if (target.checkOverlap(lines[i])) return true;
        }
        return false;
    }

    /**
     * Gets the location of the top left corner
     * @return point of top left corner
     */
    public Point getLocation() { return this.lines[0].getLocation(); }
    
    /**
     * Gets the area of the rectangle
     * @return area of the rectangle
     */
    public double getArea() { return this.lines[0].getArea() * this.lines[1].getArea(); }

    /**
     * Not implemented
     */
    public float[] getXatY(int y) {
        return new float[] {0f};
    }

    /**
     * Not implemented
     */
    public float[] getYatX(int x) {
        return new float[] {0f};
    }
}