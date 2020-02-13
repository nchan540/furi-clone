package app;

public class Rectangle implements Shape {
    
    public LineSegment top, left, right, bottom;

    public Rectangle(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.top = new LineSegment(topLeft, topRight);
        this.left = new LineSegment(topLeft, bottomLeft);
        this.right = new LineSegment(topRight, bottomRight);
        this.bottom = new LineSegment(bottomLeft, bottomRight);
    }

    public boolean doesOverlap(Point target) {
        if (bottom.equation.getY(target.x) <= target.y && target.y <= top.equation.getY(target.x)) return true;
        return false;
    }

    // TODO: implement
    public boolean doesOverlap(Shape target) {
        // if (target.doesOverlap(this.top) || target.doesOverlap(this.left) || target.doesOverlap(this.right) || target.doesOverlap(this.bottom)) return true;
        return false;
    }
    
    public float[] getXatY(int y) {
        return new float[] {0f};
    }

    public float[] getYatX(int x) {
        return new float[] {0f};
    }
}