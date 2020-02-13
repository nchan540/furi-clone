package app;

public class Rectangle implements Shape {
    
    // [0] = top, [1] = left, [2] = right, [3] = bottom;
    public LineSegment[] lines = new LineSegment[4];

    public Rectangle(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.lines[0] = new LineSegment(topLeft, topRight);
        this.lines[1] = new LineSegment(topLeft, bottomLeft);
        this.lines[2] = new LineSegment(topRight, bottomRight);
        this.lines[3] = new LineSegment(bottomLeft, bottomRight);
    }

    public boolean checkOverlap(Circle target) {
        for (int i = 0; i < lines.length; ++i) {
            if (target.checkOverlap(this.lines[i])) return true;
        }
        return false;
    }

    public boolean checkOverlap(LineSegment target) {
        for (int i = 0; i < lines.length; ++i) {
            if (target.checkOverlap(lines[i])) return true;
        }
        return false;
    }

    public Point getLocation() { return this.lines[0].getLocation(); }
    
    public double getArea() { return this.lines[0].getArea() * this.lines[1].getArea(); }

    public float[] getXatY(int y) {
        return new float[] {0f};
    }

    public float[] getYatX(int x) {
        return new float[] {0f};
    }
}