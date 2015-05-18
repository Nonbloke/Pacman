package Application;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Blinky extends Ghost {

    /**
     * Blinky: The red ghost's character is aptly described as that of a shadow and is best-known as “Blinky”.
     *         In Japan, his character is represented by the word oikake, which means “to run down or pursue”.
     *         Blinky seems to always be the first of the ghosts to track Pac-Man down in the maze. He is by
     *         far the most aggressive of the four and will doggedly pursue Pac-Man once behind him.
     */
    public Blinky() {
        this.bDebugGhost=true;
        this.color="red";
        this.originalColour=this.color;
        this.startingX=13;
        this.startingY=11;
    }

    /**
     * Of all the ghosts' targeting schemes for chase mode, Blinky's is the most simple and direct, using Pac-Man's
     * current tile as his target.  Targeting Pac-Man directly in this way results in a very determined and tenacious
     * ghost who is tough to shake when he's right behind you.
     *
     * All ghosts move at the same rate of speed when a level begins, but Blinky will increase his rate of speed twice
     * each round based on the number of dots remaining in the maze (if Pac-Man dies this is not necessarily true -
     * more on this in a moment).  While in this accelerated state, Blinky is commonly called “Cruise Elroy”, yet no
     * one seems to know where this custom was originated or what it means.  On the first level, for example, Blinky
     * becomes Elroy when there are 20 dots remaining in the maze, accelerating to be at least as fast as Pac-Man. More
     * importantly, his scatter mode behavior is also modified at this time to keep targeting Pac-Man's current tile
     * in lieu of his typical fixed target tile for any remaining scatter periods in the level (he will still reverse
     * direction when entering/exiting a scatter period).  This results in Elroy continuing to chase Pac-Man while the
     * other three ghosts head for their corners as normal.  As if that weren't bad enough, when only 10 dots remain,
     * Elroy speeds up again to the point where he is now moving faster than Pac-Man. As the levels progress, Blinky
     * will turn into Elroy with more dots remaining in the maze than in previous rounds.
     *
     * Determining when Blinky turns into Elroy can become more complicated if Pac-Man is killed. The ghosts and
     * Pac-Man are reset to their starting positions whenever a life is lost and, when play continues, Blinky's
     * “Cruise Elroy” abilities are temporarily suspended until the orange ghost (Clyde) stops bouncing up and down
     * inside the ghost house and moves toward the door to exit. Until this happens, Blinky's speed and scatter
     * behavior will remain normal regardless of the number of dots remaining in the maze.  Once this temporary
     * restriction is lifted, however, Blinky will resume changing his behavior based on the dot count.
     *
     * @param maze
     */
    @Override
    public void move(Maze maze) {
        super.move(maze);
    }

    @Override
    public int getDestinationX() {
        if (state==Mode_GoingHome)
            return this.startingX;
        else
            return Main.getInstance().getPacmanLocation().getX();
    }

    @Override
    public int getDestinationY() {
        if (state==Mode_GoingHome)
            return this.startingY;
        else
            return Main.getInstance().getPacmanLocation().getY();
    }
}
