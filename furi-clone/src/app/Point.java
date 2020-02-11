package app;

public class Point {
    public float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }
}