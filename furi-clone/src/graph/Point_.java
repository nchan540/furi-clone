package graph;

import java.awt.Point;
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

    public static float distanceFormula(Point_ p1, Point_ p2) {
        return (float)(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }
    public static float distanceFormula(Point p1, Point_ p2) {
        return (float)(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y-p2.y, 2)));
    }
}