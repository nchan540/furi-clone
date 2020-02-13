package app;

public interface Shape {

    public Point getLocation();

    public double getArea();

    public float[] getXatY(int y);

    public float[] getYatX(int x);

    public boolean checkOverlap(Circle target);
    
    public boolean checkOverlap(LineSegment target);

}