package Application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Pacman implements IDrawable, IMoveable, IConsumable {

    public static final int Mode_Normal = 0;
    public static final int Mode_Invinsible = 1;
    public static final int Mode_Dead = 2;

    private final int startingX = 13;
    private final int startingY = 23;
    private final String originalColor = "yellow";
    private final boolean bDebugPacman=false;

    private String color = "yellow";
    private int x = -1;
    private int y = -1;
    private int state = Mode_Normal;
    private int currentDirection = Direction.none;
    private int userInstruction = Direction.none;

    private void debug(String s) {
        if (bDebugPacman==true)
            System.out.println(s);
    }
    @Override
    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setState(int state) {
        this.state = state;
        switch (this.state) {
            case Mode_Invinsible:
                this.color = "DARKMAGENTA";
                break;
            case Mode_Normal:
                this.color = this.originalColor;
                break;
            case Mode_Dead:
                break;
        }
    }

    @Override
    public void notifyStateChange(int gameState) {
        switch (gameState) {
            case Main.Normal:
                this.setState(Mode_Normal);
                break;
            case Main.SuperPillHasBeenEaten:
                this.setState(Mode_Invinsible);
                break;
            case Main.SuperPillIsWearingOff:
            case Main.GhostReachedHome:
                break;
            case Main.SuperPillHasWornOff:
                this.setState(Mode_Normal);
                break;
            case Main.PacmanHasBeenEaten:
                this.setState(Mode_Dead);
                break;
            default:
                System.out.println("Pacman.notifyStateChange(), *error* unknown state change.");
        }
    }

    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public void draw(GraphicsContext gc) {

        switch (state) {
            case Mode_Normal:
                drawPacman(gc);
                break;

            case Mode_Invinsible:
                drawPacman(gc);
                break;

            case Mode_Dead:
                break;
        }
    }

    public void go(int newDirection) {
        userInstruction = newDirection;
    }

    @Override
    public void move(Maze maze) {
        try {

            if (state == Mode_Dead) {
                maze.moveThing(this, x, y, startingX, startingY);
                this.updatePosition(startingX, startingY);
                userInstruction = Direction.none;
                currentDirection = Direction.none;
                state = Mode_Normal;
                return;
            }

            if ((currentDirection == Direction.none) && (userInstruction == Direction.none))
                return;

            if ((currentDirection!=userInstruction) && (Direction.oppositeDirection(currentDirection)!=userInstruction)){
                // see if we can do a zoom round corner thingy!
                int newY = Direction.calculateY(Direction.calculateY(y, currentDirection), userInstruction);
                int newX = Direction.calculateX(Direction.calculateX(x, currentDirection), userInstruction);
                newX=Maze.checkForTunnelAndCalculateX(newX);
                debug("Pacman.move(), moving (" + x + "," + y + ") -> (" + newX + "," + newY + ")");
                if (maze.isWall(newX, newY) == false) {
                    // just wind back first and do the first move (so we can eat whatever;s there!)
                    int tempX= Direction.calculateX(x, currentDirection);
                    int tempY= Direction.calculateY(y, currentDirection);
                    debug("Pacman.move(), skipping the corner (" + x + "," + y + ") -> missing (" + tempX + "," + tempY + ") -> (" + newX + "," + newY + ")");
                    moveMe(maze, x, y, tempX, tempY);
                    debug("Pacman.move(), ... skipping the corner missed (" + x + "," + y + ") -> (" + newX + "," + newY + ")");
                    moveMe(maze, x, y, newX, newY);
                    currentDirection = userInstruction;
                    mouthOpen = !mouthOpen;
                    return;
                }
            }

            // otherwise... just a normal corner.
            int newY = Direction.calculateY(y, userInstruction);
            int newX = Direction.calculateX(x, userInstruction);
            newX=Maze.checkForTunnelAndCalculateX(newX);
            if (maze.isWall(newX, newY) == false) {
                moveMe(maze, x, y, newX, newY);
                currentDirection = userInstruction;
                mouthOpen = !mouthOpen;
                return;
            }

            // the user has pushed us into the wall, so ignore that
            // and carry on in the direction we were going before.
            int userX = Direction.calculateX(x, currentDirection);
            int userY = Direction.calculateY(y, currentDirection);
            userX=Maze.checkForTunnelAndCalculateX(userX);
            if (maze.isWall(userX, userY) == false) {
                moveMe(maze, x, y, userX, userY);
                mouthOpen = !mouthOpen;
                return;
            }
        } catch (Exception ex) {
            System.out.print("Pacman.move(), exceptioned.\n");
            ex.printStackTrace();
            return;
        }
    }

    private boolean moveMe(Maze maze, int fromX, int fromY, int toX, int toY) {
        debug("Pacman.moveMe(), moving Packman (" + fromX + "," + fromY + ") -> (" + toX + "," + toY + ")");
        try {
            maze.moveThing(this, fromX, fromY, toX, toY);
        } catch (WallBashException ex) {
            System.out.println("Pacman.moveMe(), exceptioned!");
            ex.printStackTrace();
            return false;
        }
        this.updatePosition(toX, toY);
        return true;
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public boolean eat(IMoveable eater) {
        // 'eater' can only be a ghost...!
        Ghost ghost=(Ghost)eater;

        if (state == Mode_Invinsible) {
            // ignore this.... grrrr!
        } else if (state == Mode_Normal) {
            // only if ghost is in Normal mode (scaredy, flashing or dead, don't count!)
            if ((ghost.getState()==Ghost.Mode_Chasing) || (ghost.getState()==Ghost.Mode_Scatter)) {
                   // we're dead :( return to starting pos?
                Main.getInstance().PacmanHasBeenEaten();
                return true;
            }
        } else {
            // ignore.
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return currentDirection;
    }

    // -------------------------------------------------------------------------------------------------------------
    // private methods and associated members. (i.e. Internal stuff).
    // -------------------------------------------------------------------------------------------------------------
    private boolean mouthOpen = true;

    private void drawPacman(GraphicsContext gc) {
        int diameter = (int)(1.6 * (double)Maze.cellwidth);
        int actualx = x * Maze.cellwidth;
        int actualy = y * Maze.cellheight;
        int fudgeFactor = 0 - (diameter / 4);   /*Maze.cellwidth /2*/
        gc.setFill(Paint.valueOf("yellow"));
        int mouthStart = mouthPosition[currentDirection];
        int mouthEnd = (mouthOpen==true)?270:360;
        gc.fillArc(actualx + fudgeFactor, actualy + fudgeFactor, diameter, diameter, mouthStart, mouthEnd, ArcType.ROUND);
    }

    private int[] mouthPosition={135, 315, 225, 45, 45};
}
