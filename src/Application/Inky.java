package Application;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Inky extends Ghost {

    /**
     * Inky: The light-blue ghost is nicknamed “Inky” and his character is described as one who is bashful. In Japan,
     * he is portrayed as kimagure, meaning “a fickle, moody, or uneven temper”. Perhaps not surprisingly, Inky is
     * the least predictable of the ghosts. Sometimes he chases Pac-Man aggressively like Blinky; other times he
     * jumps ahead of Pac-Man as Pinky would. He might even wander off like Clyde on occasion! In fact, Inky may
     * be the most dangerous ghost of all due to his erratic behavior.
     *
     * Bashful is not a very good translation of kimagure, and misleads the player to assume Inky will shy away
     * from Pac-Man when he gets close which is not always the case.
     */
    public Inky() {
        this.bDebugGhost=false;
        this.color="lightblue";
        this.originalColour=this.color;
        this.startingX=11;
        this.startingY=14;
    }

    /***
     * During chase mode, Clyde's targeting logic changes based on his proximity to Pac-Man. He first calculates the
     * Euclidean distance between his tile and Pac-Man's tile. If the distance between them is eight tiles or more,
     * Clyde targets Pac-Man directly just as Blinky does. If the distance between them is less than eight tiles,
     * however, Clyde switches his target to the tile he normally uses during scatter mode and heads for his corner
     * until he gets far enough away to start targeting Pac-Man again.
     *
     * While occupying any tile completely outside the dashed perimeter, Clyde's target is Pac-Man. Upon entering the
     * area, Clyde changes his mind and heads for his scatter target instead. Once he exits the perimeter, his target
     * will change back to Pac-Man's current tile again. The end result is Clyde circling around and around until
     * Pac-Man moves elsewhere or a mode change occurs. Clyde is fairly easy to avoid once you understand his targeting
     * scheme.
     *
     * Just remember: he is still dangerous if you manage to get in his way as he runs back to his corner or before he
     * can reach an intersection to turn away from you.
     *
     *
     *
     * Inky uses the most complex targeting scheme of the four ghosts in chase mode.  He needs Pac-Man's current
     * tile/orientation and Blinky's current tile to calculate his final target.  To determine Inky's target, we must
     * first establish an intermediate offset two tiles in front of Pac-Man in the direction he is moving. Now imagine
     * drawing a vector from the center of the red ghost's current tile to the center of the offset tile, then double
     * the vector length by extending it out just as far again beyond the offset tile. The tile this new, extendend
     * vector points to is Inky's actual target as shown above.
     *
     * For the same reasons already discussed in Pinky's case, an overflow error occurs with the intermediate offset
     * tile generated for Inky's calculation when Pac-Man is moving up resulting in an offset tile that is two tiles
     * above and two tiles to the left (see above picture). The other three orientations (left, right, down) produce
     * the expected result of an offset two tiles in front of Pac-Man in the direction he is moving.
     *
     * Inky's targeting logic will keep him away from Pac-Man when Blinky is far away from Pac-Man, but as Blinky
     * draws closer, so will Inky's target tile.  This explains why Inky's behavior seems more variable as Pac-Man
     * moves away from Blinky.  Like Pinky, Inky's course can often be altered by Pac-Man changing direction or
     * “head-faking”.  How much or how little effect this will have on Inky's decisions is directly related to where
     * Blinky is at the time.
     */
    @Override
    public void move(Maze maze) {
        //super.move(maze);
    }

    @Override
    public int getDestinationX() {
        Location pacmanLocation=Main.getInstance().getPacmanLocation();
        int pacmanTargetX=Direction.calculateX(Direction.calculateX(pacmanLocation.getX(), pacmanLocation.getDirection()), pacmanLocation.getDirection());
        int blinkyX=Main.getInstance().getBlinkyLocation().getX();
        int deltaX=pacmanTargetX-blinkyX;
        int x=pacmanTargetX+deltaX;
        if (x<0) x=0;
        if (x>27) x=27;
        return x;
    }

    @Override
    public int getDestinationY() {
        Location pacmanLocation=Main.getInstance().getPacmanLocation();
        int pacmanTargetY=Direction.calculateY(Direction.calculateY(pacmanLocation.getY(), pacmanLocation.getDirection()), pacmanLocation.getDirection());
        int blinkyY=Main.getInstance().getBlinkyLocation().getY();
        int deltaY=pacmanTargetY-blinkyY;
        int y=pacmanTargetY+deltaY;
        if (y<0) y=0;
        if (y>29) y=29;
        return y;
    }
}
