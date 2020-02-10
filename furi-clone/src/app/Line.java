package app;

public class Line {
    public float m;
    public float b;

    public Line(float m, float b) {
        this.m = m;
        this.b = b;
    }

    public float GetY(float x) {
        return this.m * x + b;
    }
}