package Application;

/**
 * Created by Sarah on 14/05/2015.
 */
public interface IPositionProvider {
    int getStartingX();
    int getStartingY();

    int getDestinationX();
    int getDestinationY();

    int targetReachedHandler();
}
