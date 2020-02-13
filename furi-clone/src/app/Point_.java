package app;

public class Point_ {
    public float x, y;

    public Point_(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point_(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }
}