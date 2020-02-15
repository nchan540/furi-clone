package app;

public class Rectangle implements Shape {
    
    // [0] = top, [1] = left, [2] = right, [3] = bottom;
    public LineSegment[] lines = new LineSegment[4];

    // [0] = top left, [1] = top right, [2] = bottom right, [3] = bottom left
    public Point_[] points = new Point_[4];

    /**
     * Constructor for a rectangle
     * @param topLeft point of top left corner
     * @param topRight point of top right corner
     * @param bottomLeft point of bottom left corner
     * @param bottomRight point of bottom right corner
     */
    public Rectangle(Point_ topLeft, Point_ topRight, Point_ bottomLeft, Point_ bottomRight) {
        // Copy points
        Point_ tL = new Point_(topLeft.x, topLeft.y);
        Point_ tR = new Point_(topRight.x, topRight.y);
        Point_ bL = new Point_(bottomLeft.x, bottomLeft.y);
        Point_ bR = new Point_(bottomRight.x, bottomRight.y);

        this.points[0] = tL;
        this.points[1] = tR;
        this.points[2] = bR;
        this.points[3] = bL;

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
    public Rectangle(Point_ topLeft, float width, float height) {
        // Make points
        Point_ tL = new Point_(topLeft.x, topLeft.y);
        Point_ tR = new Point_(tL.x + width, tL.y);
        Point_ bL = new Point_(tL.x, tL.y + height);
        Point_ bR = new Point_(tL.x + width, tL.y + height);
        
        this.points[0] = tL;
        this.points[1] = tR;
        this.points[2] = bR;
        this.points[3] = bL;

        this.lines[0] = new LineSegment(tL, tR);
        this.lines[1] = new LineSegment(tL, bL);
        this.lines[2] = new LineSegment(tR, bR);
        this.lines[3] = new LineSegment(bL, bR);
    }

    public Rectangle(Point_ p, Line centre, int width, int length) {
        LineSegment mid = new LineSegment(centre, p, length);
        // Line for half the rectangle to get bottom right corner
        LineSegment temp = new LineSegment(new Line((-1f / centre.m), p), p, width);
        Point_ bR = temp.p2;
        float x = p.x + (p.x - temp.p2.x);
        Point_ bL = new Point_(x, temp.equation.getY(x)); 

        System.out.println("1: (" + mid.p1.x + ", " + mid.p1.y + ")");
        System.out.println("2: (" + temp.p2.x + ", " + temp.p2.y + ")");

        this.lines[3] = new LineSegment(bL, bR);
        this.lines[2] = new LineSegment(new Line(centre.m, bR), bR, -length);
        this.lines[1] = new LineSegment(new Line(centre.m, bL), bL, -length);
        this.lines[0] = new LineSegment(lines[1].p2, lines[2].p2);

        this.points[0] = lines[1].p2;
        this.points[1] = lines[2].p2;
        this.points[2] = bR;
        this.points[3] = bL;

        System.out.println("Top Left: (" + points[0].x + ", " + points[0].y + ")");
        System.out.println("Top Right: (" + points[1].x + ", " + points[1].y + ")");
        System.out.println("Bottom Right: (" + points[2].x + ", " + points[2].y + ")");
        System.out.println("Bottom Left: (" + points[3].x + ", " + points[3].y + ")");
    }

    /**
     * Checks if overlap occurs with a point
     * @param target point being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(Point_ target) {
        float topY = lines[0].getYatX(target.x)[0];
        float bottomY = lines[3].getYatX(target.x)[0];
        if (topY != -1000 && bottomY != -1000) {
            return this.lines[0].equation.getY(target.x) > target.y && target.y > this.lines[3].equation.getY(target.x);
        }
        return false;
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
        return this.checkOverlap(target.p1);
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
        return this.checkOverlap(target.p1);
    }

    /**
     * Gets the location of the top left corner
     * @return point of top left corner
     */
    public Point_ getLocation() { return this.lines[0].getLocation(); }
    
    /**
     * Gets the area of the rectangle
     * @return area of the rectangle
     */
    public double getArea() { return this.lines[0].getArea() * this.lines[1].getArea(); }

    /**
     * Not implemented
     */
    public float[] getXatY(float y) {
        return new float[] {0f};
    }

    /**
     * Not implemented
     */
    public float[] getYatX(float x) {
        return new float[] {0f};
    }

    public int getRadius() {
        return 0;
    }

    public LineSegment[] getLines() {
        return lines;
    }
}