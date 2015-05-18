package Application;

/**
 * Created by Sarah on 11/05/2015.
 */
public interface IPathFinder {

    void setDestination(IPositionProvider positionProvider);
    boolean hasDestination();
    boolean firstStep(int currentX, int currentY);
    int nextStep(int currentX, int currentY, int currentDirection);
    void reset();

}
