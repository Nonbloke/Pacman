package Application;

/**
 * Created by Sarah on 12/05/2015.
 */
public class WallBashException extends Exception {
    public WallBashException(IDrawable thing, int fromX, int fromY, int toX, int toY) {
        super("WallBashException raised(), "+thing.toString()+" from ("+fromX+","+fromY+") -> ("+toX+","+toY+")");
    }
}
