package app;

public class Circle implements Shape {

    public Point p;
    public int radius;

    Circle(Point p, int r) {
        this.p = new Point(p.x, p.y);
        this.radius = r;
    }

    Circle(float x, float y, int r) {
        this.p = new Point(x, y);
        this.radius = r;
    }

    public float[] getXatY(int y) {
        if(Math.abs(y-p.y) > radius) return new float[]{-1000};
        float[] coords = new float[2];
        coords[0] = (float)(p.x + Math.sqrt((radius^2) - (Math.pow((y - p.y), 2))));
        coords[1] = (float)(p.x - Math.sqrt((radius^2) - (Math.pow((y - p.y), 2))));
        return coords;
    }

    public float[] getYatX(int x) {

        if(Math.abs(x-p.x) > radius) return new float[]{-1000};

        float[] coords = new float[2];
        coords[0] = (float)(p.y + Math.sqrt((radius^2) - (Math.pow((x - p.x), 2))));
        coords[1] = (float)(p.y - Math.sqrt((radius^2) - (Math.pow((x - p.x), 2))));
        return coords;
    }
}