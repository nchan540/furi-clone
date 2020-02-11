package app;

public class Circle {

    public Point p;
    public int radius;

    Circle(Point p, int r) {
        this.p = new Point(p.x, p.y);
        this.radius = r;
    }

    Circle(float x, float y, int r) {
        this.p = new Point(x, y);
        this.radius = r;
    }
}