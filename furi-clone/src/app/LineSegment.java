package app;

public class LineSegment implements Shape {
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
        if (this.p1.x <= intersection.x && intersection.x <= this.p2.x) return true;
        return false;
    }

    public boolean doesIntersect(LineSegment segment) {
        Point intersection = this.getIntersection(segment);
        if (intersection == null) return false;
        if (intersection.x == -1 && intersection.y == -1) return true;
        if (segment.p1.x <= intersection.x && intersection.x <= segment.p2.x) {
            if (this.p1.x <= intersection.x && intersection.x <= this.p2.x) return true;
        }
        return false;
    }

    public float[] getXatY(int y) {
        if (!((p1.y > y && y > p2.y) || (p2.y > y && p1.y > y))) return new float[]{-1000};

        return new float[]{equation.getX(y)};
    }

    public float[] getYatX(int x) {
        if (!((p1.x > x && x > p2.x) || (p2.x > x && p1.x > x))) return new float[]{-1000};

        return new float[]{equation.getY(x)};
    }
}