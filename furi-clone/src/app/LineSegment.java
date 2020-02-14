package app;

public class LineSegment implements Shape {
    public Line equation;
    
    // Point_s for a line segment AB if p1 is A and p2 is B
    public Point_ p1, p2;

    /**
     * Constructor for a line segment
     * @param equation equation the segment is on
     * @param p1 point A for a segment AB
     * @param length length for the segment
     */
    public LineSegment(Line equation, Point_ p1, float length) {
        this.equation = new Line(equation.m, equation.b);
        this.p1 = new Point_(p1.x, p1.y);

        float deltaY = equation.getY(1) - equation.getY(0);
        float deltaX = 1;

        // 1 (deltaX) squared is 1, and deltaX will always be 1, so the value can be put it to improve speed
        // the ratio of speed : hypotenuse is the same as the ratio of xLength : deltaX and yLength : deltaY
        float ratio = length / (float)(Math.abs(Math.sqrt(1 + Math.pow(deltaY, 2))));
        // speed / xSpeed = deltaX / ratio
        float xLength = deltaX * ratio;
        float y = equation.getY(this.p1.x + xLength);
        if (y / length >= 0) {
            this.p2 = new Point_(this.p1.x + xLength, y);
        } else {
            xLength = -1 * xLength;
            y = equation.getY(this.p1.x + xLength);
            this.p2 = new Point_(this.p1.x + xLength, y);
        }
    }

    /**
     * Constructor for a line segment
     * @param m slope of the line
     * @param b vertical shift of the line
     * @param x1 x coordinate of point A for a segment AB
     * @param x2 x coordinate of point B for a segment AB
     */
    public LineSegment(float m, float b, float x1, float x2) {
        this.equation = new Line(m, b);
        this.p1 = new Point_(x1, this.equation.getY(x1));
        this.p2 = new Point_(x2, this.equation.getY(x2));
    }

    /**
     * Constructor for a line segment
     * @param p1 point A for a segment AB
     * @param p2 point B for a segment AB
     */
    public LineSegment(Point_ p1, Point_ p2) {
        this.p1 = new Point_(p1.x, p1.y);
        this.p2 = new Point_(p2.x, p2.y);
        if (this.p1.x == this.p2.x) this.p2.x += 0.00001f;
        float m = (this.p2.y - this.p1.y) / (this.p2.x - this.p1.x);
        float b = this.p1.y - m*this.p1.x;
        this.equation = new Line(m, b);
    }

    /**
     * Get a rounded coordinate for x
     * @return rounded x coordinate
     */
    public int getX() { return Math.round(this.p1.x); }

    /**
     * Get a rounded coordinate for y
     * @return rounded y coordinate
     */
    public int getY() { return Math.round(this.p1.y); }

    /**
     * Get the point of intersection with a line equation
     * @param segment
     * @return point of intersection
     *         {@code (-1, -1)} if the line equations are the same
     *         {@code null} if a point of intersection does not exist
     */
    public Point_ getIntersection(Line line) {
        if (this.equation.m == line.m) {
            if (this.equation.b == line.b) return new Point_(-1, -1);
            return null;
        }

        float x = (line.b - this.equation.b) / (this.equation.m - line.m);
        float y = this.equation.getY(x);

        return new Point_(x, y);
    }

    /**
     * Get the point of intersection with a line segment equation
     * @param segment
     * @return point of intersection
     *         {@code (-1, -1)} if the line equations are the same
     *         {@code null} if a point of intersection does not exist
     */
    public Point_ getIntersection(LineSegment segment) {
        Line line = segment.equation;
        return this.getIntersection(line);
    }

    /**
     * Checks if overlap occurs with a circle
     * @param target circle being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(Circle target) {
        return target.checkOverlap(this);
    }

    /**
     * Checks if overlap occurs with a line
     * @param target line being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(Line line) {
        Point_ intersection = this.getIntersection(line);
        if (intersection == null) return false;
        if (intersection.x == -1 && intersection.y == -1) return true;
        if (this.p1.x <= intersection.x && intersection.x <= this.p2.x) return true;
        return false;
    }

    /**
     * Checks if overlap occurs with a line segment
     * @param target line segment being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(LineSegment segment) {
        Point_ intersection = this.getIntersection(segment);
        if (intersection == null) return false;
        if (intersection.x == -1 && intersection.y == -1) return true;
        if (segment.p1.x <= intersection.x && intersection.x <= segment.p2.x) {
            if (this.p1.x <= intersection.x && intersection.x <= this.p2.x) return true;
        }
        return false;
    }

    /**
     * Gets the location of the segment's first point
     * @return location of point 1
     */
    public Point_ getLocation() { return this.p1; }

    /**
     * Gets the length of the line segment
     * @return length of the segment
     */
    public double getArea() { return Constants.distanceFormula(p1, p2); }

    /**
     * Gets the x coordinate at a y coordinate
     * @param y y coordinate being checked
     * @return x coordinate at the y coordinate
     */
    public float[] getXatY(float y) {
        if (!((p1.y > y && y > p2.y) || (p2.y > y && p1.y > y))) return new float[]{-1000};

        return new float[]{equation.getX(y)};
    }

    /**
     * Gets the y coordinate at an x coordinate
     * @param x x coordinate being checked
     * @return y coordinate at the x coordinate
     */
    public float[] getYatX(float x) {
        if (!((p1.x > x && x > p2.x) || (p2.x > x && p1.x > x))) return new float[]{-1000};

        return new float[]{equation.getY(x)};
    }
}