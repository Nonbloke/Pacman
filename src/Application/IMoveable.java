package Application;

/**
 * Created by Sarah on 08/05/2015.
 */
public interface IMoveable {
    void move(Maze maze);
    void setState(int state);
    void notifyStateChange(int gameState);
    int getState();
}
