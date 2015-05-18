package Application;

import Dijkstra.PathFinderDijkstra;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int Normal=0;
    public static final int Go=1;
    public static final int SuperPillHasBeenEaten=2;
    public static final int SuperPillIsWearingOff=3;
    public static final int SuperPillHasWornOff=4;
    public static final int PacmanHasBeenEaten=5;
    public static final int GhostReachedHome=6;
    public static final int tickLength=200;
    private int tickLengthScardy=10000;
    private int tickLengthFlashing=2000;
    private static Main instance=null;
    private GraphicsContext gc=null;
    private Maze maze=null;
    private Pacman pacman=null;
    private Inky inky=null;
    private Pinky pinky=null;
    private Blinky blinky=null;
    private Clyde clyde=null;
    private Score score=null;
    private LivesIndicator lives=null;
    private Label label=null;
    private EventThread eventThread=new EventThread();
    private PathFinderDijkstra dijkstra=null;

    public Main() {
        instance=this;
    }

    public static void main(String[] args) {
        launch(args);
    }

    static public Main getInstance() {
        return instance;
    }

    @Override
    public void init() throws Exception {
        try {
            System.out.println("Main.init(), hello!");

            maze = new Maze();
            pacman = new Pacman();
            inky = new Inky();
            pinky = new Pinky();
            blinky = new Blinky();
            clyde = new Clyde();
            score = new Score();
            lives = new LivesIndicator();
            label = new Label("Ready!!!");
            dijkstra = new PathFinderDijkstra();        // this makes the graph and helps setup the maze!!

            maze.addToBoard(pacman, 13, 23);

            // the red one that sits at the entrance to the pen/gaol.
            maze.addToBoard(blinky, blinky.getStartingX(), blinky.getStartingY());  //14, 11);        // 13,11!
            blinky.setState(Ghost.Mode_Starting);

            maze.addToBoard(inky, inky.getStartingX(), inky.getStartingY());  // light blue, starting at 11,14.
            inky.setState(Ghost.Mode_Starting);

            maze.addToBoard(pinky, pinky.getStartingX(), pinky.getStartingY()); // the pink one, starting at 13,14. (or 14,15?)
            pinky.setState(Ghost.Mode_Starting);

            maze.addToBoard(clyde, clyde.getStartingX(), clyde.getStartingY()); // the orange one, starting 15, 14.
            clyde.setState(Ghost.Mode_Starting);

            maze.addToBoard(score, 0, 31);
            maze.addToBoard(lives, Maze.width-7, 31);
            maze.addToBoard(label, 12, 17);
            maze.smoothWalls();

        } catch (Exception ex) {
            System.out.println("Main.Main(), exceptioned!");
            ex.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Pac-Man");
        Group root=new Group();
        Canvas canvas=new Canvas(Maze.width*Maze.cellwidth, Maze.height*Maze.cellheight);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene myScene=new Scene(root);
        primaryStage.setScene(myScene);
        primaryStage.show();

        // add something to capture the keypresses...
        myScene.addEventHandler(KeyEvent.KEY_PRESSED, keyPress_Handler);

        // draw the maze...
        maze.drawMaze(gc);

        // start the tick timer ...
        timingThread.start();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Main.stop(), done.");
        timingThread.interrupt();
        eventThread.stop();
    }

    Thread timingThread=new Thread() {
        @Override
        public void run() {
            System.out.println("MainThread.run(), starting...");
            while (true) {
                try {
                    this.sleep(tickLength);
                } catch (InterruptedException intEx) {
                    System.out.println("MainThread.run(), interrupted.");
                    break;
                }

                //Do some stuff in another thread
                Platform.runLater(new Runnable() {
                    public void run() {
                        moveAndUpdate();
                    }
                });
            }
            System.out.println("MainThread.run(), done.");
        }
    };

    EventHandler<KeyEvent> keyPress_Handler=new EventHandler<KeyEvent>() {
        public void handle(final KeyEvent keyEvent) {
            processKeyPress(keyEvent.getCode());
        }
    };

    /***
     * this touches the UI, so needs to be called in main UI thread.
     * @return
     */
    private void moveAndUpdate() {
        try {
            //System.out.println("Main.moveAndUpdate(), doing a move...");
            inky.move(maze);
            pinky.move(maze);
            blinky.move(maze);
            clyde.move(maze);
            pacman.move(maze);
            maze.drawMaze(gc);
        } catch (Exception ex) {
            System.out.println("Main.moveAndUpdate(), exceptioned!");
            ex.printStackTrace();
        }
    }

    public Location getPacmanLocation() { return new Location(pacman.getX(), pacman.getY(), pacman.getDirection()); }
    public Location getBlinkyLocation() { return blinky.getLocation(); }

    private boolean started=false;
    private void processKeyPress(KeyCode code) {

        if (started==false) {
            if (code==KeyCode.ENTER) {
                started = true;
                Go();
            }
            return;
        }

        if (code==KeyCode.ESCAPE) {
            timingThread.interrupt();
            return;
        }

        int direction=0;
        if (code==KeyCode.UP) {
            direction=Direction.up;
        } else if (code==KeyCode.DOWN) {
            direction=Direction.down;
        } else if (code==KeyCode.LEFT) {
            direction=Direction.left;
        } else if (code==KeyCode.RIGHT) {
            direction=Direction.right;
        }
        pacman.go(direction);
    }

    /***
     * called when a super pill has been eaten (called from a move function!)
     */
    private int ghostStateBeforeSuperPill=Ghost.Mode_Starting;

    private void Go(){
        System.out.println("*****************************Go************************************");
        //inky.setState(Ghost.Mode_Scatter);
        //pinky.setState(Ghost.Mode_Scatter);
        //blinky.setState(Ghost.Mode_Scatter);
        //clyde.setState(Ghost.Mode_Scatter);
        //pacman.setState(Pacman.Mode_Normal);
        inky.notifyStateChange(Go);
        pinky.setState(Go);
        blinky.setState(Go);
        clyde.setState(Go);
        pacman.setState(Go);
        label.setMessage("");
    }

    /***
     * Called directly from SuperPill.eat().
     */
    public void SuperPillHasBeenEaten() {
        System.out.println("*****************************SuperPillHasBeenEaten************************************");
        ghostStateBeforeSuperPill=inky.getState();
        inky.notifyStateChange(SuperPillHasBeenEaten);
        pinky.notifyStateChange(SuperPillHasBeenEaten);
        blinky.notifyStateChange(SuperPillHasBeenEaten);
        clyde.notifyStateChange(SuperPillHasBeenEaten);
        pacman.notifyStateChange(SuperPillHasBeenEaten);
        eventThread.setParams(tickLengthScardy, EventThread.Event_SuperPillEaten);
        eventThread.start();
    }

    /**
     * called from EventThread when time is up on the super pill effects!
     */
    public void SuperPillEffectIsWearingOff() {
        System.out.println("**************************SuperPillEffectIsWearingOff********************************");
        inky.notifyStateChange(SuperPillIsWearingOff);
        pinky.notifyStateChange(SuperPillIsWearingOff);
        blinky.notifyStateChange(SuperPillIsWearingOff);
        clyde.notifyStateChange(SuperPillIsWearingOff);
        eventThread.setParams(tickLengthFlashing, EventThread.Event_SuperPillFading);
        eventThread.start();
    }

    /***
     * SuperPillEffectHasWornOff called from the eventThread (via RunLater).
     */
    public void SuperPillEffectHasWornOff() {
        System.out.println("***************************SuperPillEffectHasWornOff*********************************");
        inky.notifyStateChange(SuperPillHasWornOff);
        pinky.notifyStateChange(SuperPillHasWornOff);
        blinky.notifyStateChange(SuperPillHasWornOff);
        clyde.notifyStateChange(SuperPillHasWornOff);
        pacman.notifyStateChange(SuperPillHasWornOff);
    }

    /***
     * Called directly from Pacman.eat() function.
     */
    public void PacmanHasBeenEaten() {
        System.out.println("***************************PacmanHasBeenEaten*********************************");
        pacman.notifyStateChange(PacmanHasBeenEaten);

        int remainingLives=lives.removeOneLife();
        if (remainingLives==0) {
            // game over.
            timingThread.interrupt();
        }

        inky.notifyStateChange(PacmanHasBeenEaten);
        pinky.notifyStateChange(PacmanHasBeenEaten);
        blinky.notifyStateChange(PacmanHasBeenEaten);
        clyde.notifyStateChange(PacmanHasBeenEaten);
        pacman.notifyStateChange(PacmanHasBeenEaten);
    }

    public void UpdateScore(int value) {
        score.add(value);
        if (score.isComplete()==true) {
            // game won.
            timingThread.interrupt();
        }
    }

    private Highlight highlight=null;
    public void highlightLocation(int x, int y) {
        if (highlight==null) {
            highlight = new Highlight();
            maze.addToBoard(highlight, x, y);
        }
        highlight.updatePosition(x,y);
    }

}
