package app;

public class Line {
    public float m;
    public float b;

    public Line(float m, float b) {
        this.m = m;
        this.b = b;
    }

    /**
     * Returns the y value at an x coordinate
     * @param x x coordinate
     * @return y value
     */
    public float getY(float x) {
        return this.m * x + b;
    }

    /**
     * Returns the x value at a y coordinate
     * @param y y coordinate
     * @return x value
     */
    public float getX(float y) {
        return (y - b)/m;
    }
}