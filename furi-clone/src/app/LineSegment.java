package app;

public class LineSegment {
    public Line equation;
    private Point loc1, loc2;

    public LineSegment(float m, float b, float x1, float x2) {
        this.equation = new Line(m, b);
        this.loc1.x = x1;
        this.loc1.y = this.equation.GetY(x1);
        this.loc2.x = x2;
        this.loc2.y = this.equation.GetY(x2);
    }

    public int GetX() { return Math.round(this.loc1.x); }

    public int GetY() { return Math.round(this.loc1.y); }

    public Point GetIntersection(Line line) {
        if (this.equation.m == line.m) {
            if (this.equation.b == line.b) return new Point(-1, -1);
            return null;
        }

        float x = (line.b - this.equation.b) / (this.equation.m - line.m);
        float y = this.equation.GetY(x);

        return new Point(x, y);
    }

    public Point GetIntersection(LineSegment segment) {
        Line line = segment.equation;
        return this.GetIntersection(line);
    }

    public boolean DoesIntersect(Line line) {
        Point intersection = this.GetIntersection(line);
        if (intersection == null) return false;
        if (intersection.x == -1 && intersection.y == -1) return true;
        if (intersection.x > this.loc1.x + this.loc2.x || intersection.x < this.loc1.x
            || intersection.y > this.loc1.y + this.loc2.y || intersection.y < this.loc1.y) return false;
        return true;
    }

    public boolean DoesIntersect(LineSegment segment) {
        return this.DoesIntersect(segment.equation);
    }
}