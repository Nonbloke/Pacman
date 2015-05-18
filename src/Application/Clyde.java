package Application;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Clyde extends Ghost {

    /***
     * Clyde: The orange ghost is nicknamed “Clyde” and is characterized as one who is pokey. In Japan, his character is
     * described as otoboke, meaning “pretending ignorance”, and his nickname is “Guzuta”, meaning “one who lags behind”.
     *
     * In reality, Clyde moves at the same speed as Inky and Pinky so his character description is a bit misleading.
     *
     * Clyde is the last ghost to leave the pen and tends to separate himself from the other ghosts by shying away from
     * Pac-Man and doing his own thing when he isn't patrolling his corner of the maze. Although not nearly as dangerous
     * as the other three ghosts, his behavior can seem unpredictable at times and should still be considered a threat.
     */
    public Clyde() {
        this.color="orange";
        this.originalColour=this.color;
        this.startingX=15;
        this.startingY=14;
    }

    /**
     * During chase mode, Clyde's targeting logic changes based on his proximity to Pac-Man.  He first calculates the
     * Euclidean distance between his tile and Pac-Man's tile.  If the distance between them is eight tiles or more,
     * Clyde targets Pac-Man directly just as Blinky does.  If the distance between them is less than eight tiles,
     * however, Clyde switches his target to the tile he normally uses during scatter mode and heads for his corner
     * until he gets far enough away to start targeting Pac-Man again.  In the picture above, Clyde is stuck in an
     * endless loop (as long as Pac-Man stays where he is) thanks to this scheme.  While occupying any tile completely
     * outside [this] perimeter, Clyde's target is Pac-Man.  Upon entering the area, Clyde changes his mind and heads
     * for his scatter target instead.  Once he exits the perimeter, his target will change back to Pac-Man's current
     * tile again.
     *
     * The end result is Clyde circling around and around until Pac-Man moves elsewhere or a mode change occurs. Clyde
     * is fairly easy to avoid once you understand his targeting scheme.  Just remember: he is still dangerous if you
     * manage to get in his way as he runs back to his corner or before he can reach an intersection to turn away from
     * you.
     *
     * @param maze
     */
    @Override
    public void move(Maze maze) {
        //super.move(maze);
    }

    @Override
    public int getDestinationX() {
        return 0;
    }

    @Override
    public int getDestinationY() {
        return 0;
    }
}
