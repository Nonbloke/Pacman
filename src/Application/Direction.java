package Application;

/**
 * Created by Sarah on 10/05/2015.
 */
public class Direction {

    public final static int up=0;
    public final static int down=1;
    public final static int left=2;
    public final static int right=3;
    public final static int none=4;

    public static int calculateY(int y, int direction) {
        if (direction == up)
            return y - 1;
        else if (direction == down)
            return y + 1;
        return y; // it might be none.
    }

    public static int calculateX(int x, int direction) {
        if (direction==left)
            return x - 1;
        else if (direction==right)
            return x + 1;
        return x;
    }

    public static String toString(int direction) {
        if (direction==up) return "Up";
        if (direction==down) return "Down";
        if (direction==left) return "Left";
        if (direction==right) return "Right";
        return "None";
    }

    public static int oppositeDirection(int direction) {
        if (direction==up) return down;
        if (direction==down) return up;
        if (direction==left) return right;
        if (direction==right) return left;
        return none;
    }

    public static boolean isBehind(int x, int y, int a, int b, int direction) {
        if (direction==up)
            return ((x==a) && (y>b))?true:false;

        if (direction==down)
            return ((x==a) && (y<b))?true:false;

        if (direction==left)
            return ((y==b) && (x>a))?true:false;

        if (direction==right)
            return ((y==b) & (x<a))?true:false;
        return false;
    }

    public static double getEuclideanDistance(int a, int b, int x, int y) {
        int distanceX=0;
        if (a>=x) {
            distanceX=a-x;
        } else {
            distanceX=x-a;
        }

        int distanceY=0;
        if (b>=y) {
            distanceY=b-y;
        } else {
            distanceY=y-b;
        }

        double distance=Math.sqrt((distanceX*distanceX)+(distanceY*distanceY));
        return distance;
    }

}
