package Application;

/**
 * Created by Sarah on 10/05/2015.
 */
public class Location {

    private int x=0;
    private int y=0;
    private int direction=Direction.none;

    public Location(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public Location(int x, int y, int direction) {
        this.x=x;
        this.y=y;
        this.direction=direction;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getDirection() { return direction; }

    public void setX(int value) { this.x=value; }
    public void setY(int value) { this.y=value; }
    public void setDirection(int value) { this.direction=value; }

}
