package Application;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Pinky extends Ghost {

    /**
     * Pinky: Nicknamed “Pinky”, the pink ghost's character is described as one who is speedy. In Japan, he is
     * characterized as machibuse, meaning “to perform an ambush”, perhaps because Pinky always seems to be able
     * to get ahead of you and cut you off when you least expect it.
     *
     * He always moves at the same speed as Inky and Clyde, however, which suggests speedy is a poor translation
     * of the more appropriate machibuse.
     *
     * Pinky and Blinky often seem to be working in concert to box Pac-Man in, leaving him with nowhere to run.
     */
    public Pinky() {
        this.bDebugGhost=false;
        this.color="pink";
        this.originalColour=this.color;
        this.startingX=13;
        this.startingY=14;
    }


    /**
     * In chase mode, Pinky behaves as he does because he does not target Pac-Man's tile directly. Instead, he selects
     * an offset four tiles away from Pac-Man in the direction Pac-Man is currently moving (with one exception).
     *
     * If Pac-Man is moving left, Pinky's target tile will be four game tiles to the left of Pac-Man's current tile.
     * If Pac-Man is moving right, Pinky's tile will be four tiles to the right.
     * If Pac-Man is moving down, Pinky's target is four tiles below.
     * Finally, if Pac-Man is moving up, Pinky's target tile will be four tiles up and four tiles to the left.  This
     * interesting outcome is due to a subtle error in the logic code that calculates Pinky's offset from Pac-Man.
     * This piece of code works properly for the other three cases but, when Pac-Man is moving upwards, triggers an
     * overflow bug that mistakenly includes a left offset equal in distance to the expected up offset (we will see
     * this same issue in Inky's logic later).
     *
     * Don Hodges' website has an excellent article giving a thorough, code-level analysis of this bug, including the
     * source code and a proposed fix—click here to go there now.
     *
     * SJM: I'm not putting a bug in my code!
     */
    @Override
    public void move(Maze maze) {
        //super.move(maze);
    }

    @Override
    public int getDestinationX() {
        Location location=Main.getInstance().getPacmanLocation();
        if (location.getDirection()==Direction.left)
            return ((location.getX()-4)<0)?0:(location.getX()-4);
        if (location.getDirection()==Direction.right)
            return ((location.getX()+4)>27)?27:(location.getX()+4);
        return location.getX();
    }

    @Override
    public int getDestinationY() {
        Location location=Main.getInstance().getPacmanLocation();
        if (location.getDirection()==Direction.up)
            return ((location.getY()-4)<0)?0:(location.getY()-4);
        if (location.getDirection()==Direction.down)
            return ((location.getY()+4)>27)?27:(location.getY()+4);
        return location.getY();
    }
}
