package app;

public class Circle implements Shape {

    public Point_ p;
    public int diameter;

    Circle(Point_ p, int r) {
        this.p = new Point_(p.x, p.y);
        this.diameter = r;
    }

    Circle(float x, float y, int r) {
        this.p = new Point_(x, y);
        this.diameter = r;
    }

    public float[] getXatY(int y) {
        if(Math.abs(y-p.y) > diameter) return new float[]{-1000};
        float[] coords = new float[2];
        coords[0] = (float)(p.x + Math.sqrt((diameter^2) - (Math.pow((y - p.y), 2))));
        coords[1] = (float)(p.x - Math.sqrt((diameter^2) - (Math.pow((y - p.y), 2))));
        return coords;
    }

    public float[] getYatX(int x) {

        if(Math.abs(x-p.x) > diameter) return new float[]{-1000};

        float[] coords = new float[2];
        coords[0] = (float)(p.y + Math.sqrt((diameter^2) - (Math.pow((x - p.x), 2))));
        coords[1] = (float)(p.y - Math.sqrt((diameter^2) - (Math.pow((x - p.x), 2))));
        return coords;
    }
}