package graph;

import java.awt.Point;
public class Point_ {
    public float x, y;

    /**
     * Constructor for a point
     * @param x x coordinate of point
     * @param y y coordinate of point
     */
    public Point_(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for a point
     * @param x x coordinate of point
     * @param y y coordinate of point
     */
    public Point_(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Updates x and y coordinates
     * @param c Array containing coordinates
     */
    public void set(float[] c) {
        x = c[0];
        y = c[1];
    }

    /**
     * Gets the distance between two points
     * @param p1 Start point
     * @param p2 End point
     * @return Distance between the start and end points
     */
    public static float distanceFormula(Point_ p1, Point_ p2) {
        return (float)(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }

    /**
     * Gets the distance between two points
     * @param p1 Start point
     * @param p2 End point
     * @return Distance between the start and end points
     */
    public static float distanceFormula(Point p1, Point_ p2) {
        return (float)(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }

    public boolean equals(Point_ p) {
        return (int)this.x == (int)p.x && (int)this.y == (int)p.y;
    }
}