package app;

public interface Shape {

    /**
     * Gets the location of the shape
     * @return location of the shape
     */
    public Point_ getLocation();

    /**
     * Gets the area of the shape
     * @return area of the shape
     */
    public double getArea();


    public int getRadius();

    /**
     * Gets the x corrdinate(s) at a y coordinate
     * @param y y coordinate being checked
     * @return x coordinate(s) at the y coordinate
     */
    public float[] getXatY(float y);

    /**
     * Gets the y coordinate(s) at an x coordinate
     * @param x x coordinate being checked
     * @return y coordinate(s) at the x coordinate
     */
    public float[] getYatX(float x);

    /**
     * Checks if overlap occurs with a circle
     * @param target circle being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(Circle target);
    
    /**
     * Checks for overlap with a line segment
     * @param target line segment being checked against
     * @return true or false for overlap
     */
    public boolean checkOverlap(LineSegment target);

}