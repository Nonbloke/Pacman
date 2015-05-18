package Application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Sarah on 08/05/2015.
 */
public abstract class Ghost implements IDrawable, IConsumable, IMoveable, IPositionProvider {

    protected static final int Mode_Starting = 0;
    protected static final int Mode_Scatter = 1;
    protected static final int Mode_Chasing = 2;
    protected static final int Mode_Scaredy = 3;    // running away, not towards PacMan.
    protected static final int Mode_Flashing = 4;   // warns the end of Scaredy mode.
    protected static final int Mode_GoingHome = 5;  // just the eyes got back to the gaol.
    protected static final int Mode_Restarting = 6;
    protected boolean bDebugGhost=true;

    // these shouldn't change between ghosts.
    private int score = 200;

    // this will change between ghost and should be set in the sub class's constructor.
    protected String color = "green";
    protected String originalColour = "";

    // these will just be useful to the sub-classes.
    protected int state = Mode_Starting;
    protected int startingX=0;
    protected int startingY=0;
    protected int x = -1;
    protected int y = -1;
    protected int direction = 0;
    private IPathFinder shortestPath;
    private IPathFinder randomPath;
    private boolean visible = true;

    protected void debug(String s) {
        if (bDebugGhost==true)
            System.out.println(s);
    }

    @Override
    public void setState(int state) {
        this.state = state;
        switch (state) {
            case Mode_Restarting:
                this.state=Mode_Chasing;
            case Mode_Starting:
                if (shortestPath==null)
                    shortestPath=new ShortestPath(bDebugGhost);
                shortestPath.reset();
                if (randomPath==null)
                    randomPath=new RandomPath(bDebugGhost);
                x=this.getStartingX();
                y=this.getStartingY();
                break;

            case Mode_Scatter:  // also Mode_Starting
            case Mode_Chasing:
                shortestPath.reset();
                this.color = this.originalColour;
                break;

            case Mode_Scaredy:
            case Mode_Flashing:
                this.color = "darkblue";
                break;

            case Mode_GoingHome:
                shortestPath.reset();   // this removes pacman as the target!
                this.color="black";
                break;
        }
    }

    @Override
    public void notifyStateChange(int gameState) {
        switch (gameState) {
            case Main.Go:
                this.setState(Mode_Starting);
                this.setState(Mode_Chasing);
                break;
            case Main.Normal:
                this.setState(Mode_Chasing);
                break;
            case Main.SuperPillHasBeenEaten:
                if (state!=Mode_GoingHome)
                    this.setState(Mode_Scaredy);
                break;
            case Main.SuperPillIsWearingOff:
                if (state==Mode_Scaredy)
                    this.setState(Mode_Flashing);
                break;
            case Main.SuperPillHasWornOff:
                if (state==Mode_Flashing)
                    this.setState(Mode_Chasing);
            case Main.PacmanHasBeenEaten:
                setState(Mode_Restarting);
                break;
            case Main.GhostReachedHome:
                this.setState(Mode_Chasing);
                break;
            default:
                System.out.println("Ghost.notifyStateChange(), *error* unknown state change.");
        }
    }

    @Override
    public int targetReachedHandler() {
        if (state==Mode_GoingHome)
            notifyStateChange(Main.GhostReachedHome);
        return 0;
    }

    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public void draw(GraphicsContext gc) {
        switch (state) {
            case Mode_Starting:
            case Mode_Scatter:
            case Mode_Chasing:
            case Mode_GoingHome:
            case Mode_Scaredy:
                drawGhost(gc, x, y);
                break;
            case Mode_Flashing:
                visible = !visible;
                drawGhost(gc, x, y);
                break;
        }
    }

    @Override
    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
        if (direction == -1) { // i.e. this has been called from board setup.
            int random = (int) (Math.random() * 4 + 1);
            direction = random;
        }
    }

    public Location getLocation() {
        return new Location(x, y, direction);
    }

    private void drawGhost(GraphicsContext gc, int x, int y) {
        if (visible == false)
            return;

        x = x * Maze.cellwidth;
        y = y * Maze.cellheight;

        int diameter = (int)((double)Maze.cellwidth * 1.6);
        int fudgeFactor = 0 - (diameter / 4);

        if (color.equals("black")==false) {
            gc.setFill(Paint.valueOf(color));
            gc.fillOval(x + fudgeFactor, y + fudgeFactor, diameter, diameter);
            //gc.setFill(Paint.valueOf("white"));
            gc.fillRect(x + fudgeFactor, y - fudgeFactor+2, diameter, diameter / 2);
        }

        gc.setFill(Paint.valueOf("white"));
        gc.fillOval(x + fudgeFactor + 3, y + fudgeFactor + 5, 7, 7);
        gc.fillOval(x + fudgeFactor + (diameter - 3 - 7), y + fudgeFactor + 5, 7, 7);

        gc.setFill(Paint.valueOf("black"));
        gc.fillOval(x + fudgeFactor + 3 + 3, y + fudgeFactor + 7, 3, 3);
        gc.fillOval(x + fudgeFactor + (diameter - 3 - 7 + 3), y + fudgeFactor + 7, 3, 3);
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public boolean eat(IMoveable eater) {
        if ((eater instanceof Pacman)==false)
            return false;

        if ((state == Mode_Scaredy) || (state == Mode_Flashing)) {
            this.setState(Mode_GoingHome);
            return true;
        }
        return false;
    }

    /*
     * in Pacman all of the ghost had a different chasing algorithm
     *     * Blinky -> Chases. Will usually take the shortest route to you, and tends to follow.
     *     * Pinky -> Ambushes. Tends to take a more roundabout way to pac-man. Deadly. (pinky and blinky tend to make
     *                different choice when choosing a direction , often caging the player in a corner)
     *     * Inky -> Freak. This dude acts strangely. He moves about the board fairly randomly, but sometimes chases
     *               when he gets in close.
     *     * Clyde -> Idiot. Moves randomly. Not much of a threat.
     */
    public void move(Maze maze) {
        IPathFinder pathFinder = null;

        switch (state) {
            case Mode_Starting:
                return;

            case Mode_Restarting:
            case Mode_Scatter:
            case Mode_Chasing:
                pathFinder = shortestPath;
                if (pathFinder.hasDestination() == false) {
                    pathFinder.setDestination(this);    //.setDestinationFollowPacman();
                    pathFinder.firstStep(x, y);
                }
                break;

            case Mode_Scaredy:      // aka frightened.
            case Mode_Flashing:     // still frightened.
                pathFinder = randomPath;
                break;

            case Mode_GoingHome:    // (has been eaten) going back to the den.
                pathFinder = shortestPath;
                pathFinder.setDestination(this);  // todo - fix all starting positions for the ghosts and pacman.
                pathFinder.firstStep(x, y);
                break;
        }

        if (pathFinder.hasDestination() == true) {
            direction = pathFinder.nextStep(x, y, direction);
            if (direction==Direction.none) {
                // we may have gotten to where we wanted to go! and we're not in chase pacman mode
                // (i.e. it might be that the ghost's eye's are going home!)
                setState(Mode_Chasing);
                return;
            }
            int newX = Direction.calculateX(x, direction);
            int newY = Direction.calculateY(y, direction);
            newX=Maze.checkForTunnelAndCalculateX(newX);
            debug("moving " + Direction.toString(direction) + " to (" + newX + "," + newY + ")");
            try {
                makeMove(maze, newX, newY);
            } catch (WallBashException ex) {
                System.out.println("Ghost.move(), caught WallBashException!");
                ex.printStackTrace();
            }
        }
    }

    private void makeMove(Maze maze, int newX, int newY) throws WallBashException {
        maze.moveThing(this, x, y, newX, newY);
        this.updatePosition(newX, newY);
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    @Override
    public int getStartingX() {
        return startingX;
    }

    @Override
    public int getStartingY() {
        return startingY;
    }

}


