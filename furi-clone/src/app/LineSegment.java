package app;

public class LineSegment {
    public Line equation;
    
    // Points for a line segment AB if p1 is A and p2 is B
    private Point p1, p2;

    public LineSegment(float m, float b, float x1, float x2) {
        this.equation = new Line(m, b);
        this.p1 = new Point(x1, this.equation.getY(x1));
        this.p2 = new Point(x2, this.equation.getY(x2));
    }

    public LineSegment(Point p1, Point p2) {
        this.p1 = new Point(p1.x, p1.y);
        this.p2 = new Point(p2.x, p2.y);
        float m = (this.p2.y - this.p1.y) / (this.p2.x - this.p1.x);
        float b = this.p1.y - m*this.p1.x;
        this.equation = new Line(m, b);
    }

    public int getX() { return Math.round(this.p1.x); }

    public int getY() { return Math.round(this.p1.y); }

    public Point getIntersection(Line line) {
        if (this.equation.m == line.m) {
            if (this.equation.b == line.b) return new Point(-1, -1);
            return null;
        }

        float x = (line.b - this.equation.b) / (this.equation.m - line.m);
        float y = this.equation.getY(x);

        return new Point(x, y);
    }

    public Point getIntersection(LineSegment segment) {
        Line line = segment.equation;
        return this.getIntersection(line);
    }

    public boolean doesIntersect(Line line) {
        Point intersection = this.getIntersection(line);
        if (intersection == null) return false;
        if (intersection.x == -1 && intersection.y == -1) return true;
        if (intersection.x > this.p1.x + this.p2.x || intersection.x < this.p1.x
            || intersection.y > this.p1.y + this.p2.y || intersection.y < this.p1.y) return false;
        return true;
    }

    public boolean doesIntersect(LineSegment segment) {
        return this.doesIntersect(segment.equation);
    }
}