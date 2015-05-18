package Application;

/**
 * Created by Sarah on 11/05/2015.
 */
public class RandomPath implements IPathFinder {

    private boolean bDebugAlgorithm=false;
    public RandomPath(boolean bDebug) {
        bDebugAlgorithm=bDebug;
    }

    private void debug(String s) {
        if (bDebugAlgorithm==true)
            System.out.println(s);
    }

    @Override
    public void setDestination(IPositionProvider positionProvider) {

    }

    @Override
    public boolean hasDestination() {
        return true;
    }

    @Override
    public boolean firstStep(int currentX, int currentY) {
        return false;
    }

    @Override
    public int nextStep(int currentX, int currentY, int currentDirection) {
        // random choice at junctions = frightened behaviour!
        // check to see if we can carry on in same direction.
        if (Maze.getInstance().isJunction(currentX, currentY) == true) {
            Location d = randomChoice(Maze.getInstance(), Direction.oppositeDirection(currentDirection), currentX, currentY);
            debug(">> junction! yay! we're going this way: (" + Direction.toString(d.getDirection()) + ")");
            return d.getDirection();
        } else {
            int newX = Direction.calculateX(currentX, currentDirection);
            int newY = Direction.calculateY(currentY, currentDirection);
            if (Maze.getInstance().isWall(newX, newY) == false) {
                debug(">> going the same way! (" + Direction.toString(currentDirection) + ")");
                return currentDirection;
            } else {
                // throw the dice and choose which way to go!
                Location d = randomChoice(Maze.getInstance(), Direction.oppositeDirection(currentDirection), currentX, currentY);
                debug(">> going a different way! (" + Direction.toString(d.getDirection()) + ")");
                return d.getDirection();
            }
        }
    }

    @Override
    public void reset() {

    }

    private Location randomChoice(Maze maze, int oppositeDirection, int x, int y) {
        int newX = 0;
        int newY = 0;
        int random;
        do {
            random = (int) (Math.random() * 4);         // indexed from 0..3!
            if (random == oppositeDirection)
                continue;
            newX = Direction.calculateX(x, random);
            newY = Direction.calculateY(y, random);
        } while ((newX < 0) || (newX >= Maze.width)
                || (newY < 0) || (newY >= Maze.height)
                || (maze.isWall(newX, newY) == true));
        return new Location(newX, newY, random);
    }
}
