package graph;

public class Line {
    public float m;
    public float b;

    /**
     * Constructor for a line
     * @param m slope of the line
     * @param b vertical shift of the line
     */
    public Line(float m, float b) {
        this.m = m;
        if (0 <= this.m && this.m < 0.00001f) this.m = 0.00001f;
        else if (-0.00001f < this.m && this.m < 0f) this.m = 0.00001f;
        else if (this.m > 10000) this.m = 10000;
        else if (this.m < -10000) this.m = -10000;
        this.b = b;
    }

    /**
     * Constructor for a line
     * @param m slope of the line
     * @param p point the line passes through
     */
    public Line(float m, Point_ p) {
        this.m = m;
        if (0 <= this.m && this.m < 0.00001f) this.m = -0.00001f;
        else if (-0.00001f < this.m && this.m < 0f) this.m = -0.00001f;
        else if (this.m > 10000) this.m = 10000;
        else if (this.m < -10000) this.m = -10000;
        this.b = p.y - this.m * p.x;
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