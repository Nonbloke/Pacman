package Application;

import Dijkstra.PathFinderDijkstra;
import Dijkstra.Vertex;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by Sarah on 11/05/2015.
 */
public class ShortestPath implements IPathFinder {

    private boolean bDebugAlgorithm=false;
    public ShortestPath(boolean bDebug) {
        this.bDebugAlgorithm=bDebug;
    }

    private void debug(String s) {
        if (bDebugAlgorithm==true)
            System.out.println(s);
    }

    private Vertex v1=null;
    private Vertex v2=null;
    private int targetX=0;
    private int targetY=0;
    private boolean followingPacman=false;
    private IPositionProvider provider=null;

    public void setDestination(IPositionProvider positionProvider) {
        this.provider = positionProvider;
        debug("ShortestPath.setDestination(), setting provider: "+positionProvider.toString());
        //followingPacman=true;   // as opposed to returning to one location i.e. home.
        updateDestination();
    }

    private void updateDestination() {
        this.targetX=provider.getDestinationX();
        this.targetY=provider.getDestinationY();
        debug("ShortestPath.updateDestination(), setting target location: x=" + targetX + ", y=" + targetY + ".");
        if (bDebugAlgorithm==true)
            Main.getInstance().highlightLocation(targetX, targetY);
    }

    @Override
    public boolean hasDestination() {
        return (v1!=null);
    }

    @Override
    public boolean firstStep(int currentX, int currentY) {
        LinkedList<Vertex> path = PathFinderDijkstra.getInstance().getNextStepOnShortestPath(currentX, currentY, targetX, targetY);
        if (path==null)
            return false;
        v1 = path.get(0);
        v2 = path.get(1);
        debug("ShortestPath.firstStep(" + currentX + "," + currentY + "), dijkstra's returned first node:" + v1 + ", second node:" + v2 + " rest of path: " + pathToString(path));
        return true;
    }

    private String pathToString(LinkedList<Vertex> path) {
        String trail="";
        for (Vertex v:path) {
            trail+=v.getId()+" ";
        } trail+=".";
        return trail;
    }

    @Override
    public int nextStep(int currentX, int currentY, int unused_direction) {
        if (hasDestination()==false) {
            debug("ShortestPath.nextStep(" + currentX + "," + currentY + "), we don't have a destination.");
            return Direction.none;
        }

        if ((currentX==targetX) && (currentY==targetY)) {
            debug("ShortestPath.nextStep(" + currentX + "," + currentY + "), we've met our target!.");
            this.provider.targetReachedHandler();
            //updateDestination();
            return Direction.none;
        }

        do {
            int direction = calcDirectionFromNodes(v1, v2, currentX, currentY);
            if (direction == -1) {
                debug("ShortestPath.nextStep(" + currentX + "," + currentY + "), calcDirectionFromNodes returned -1 *error*.");
                // @todo - should probably throw an exception!
                return Direction.none;
            }

            if (direction == Direction.none) {
                updateDestination();
                debug("ShortestPath.nextStep(" + currentX + "," + currentY + "), chasing pacman, new co-ords (" + targetX + "," + targetY + ")");
                debug("ShortestPath.nextStep(" + currentX + "," + currentY + "), calcDirectionFromNodes returned none, we must be on a node, calculating path again (target="+targetX+","+targetY+").");
                LinkedList<Vertex> path = PathFinderDijkstra.getInstance().getNextStepOnShortestPath(currentX, currentY, targetX, targetY);
                if (path==null) {
                    v1=null;        // reset the destination.
                    return -1;
                }
                v1 = path.get(0);
                v2 = path.get(1);
                debug("ShortestPath.firstStep(" + currentX + "," + currentY + "), dijkstra's returned first node:" + v1 + ", second node:" + v2 + " rest of path: " + pathToString(path));
                continue;
            }
            return direction;
        } while (true);
    }

    @Override
    public void reset() {
        this.v1=null;
        this.v2=null;
        this.targetX=0;
        this.targetY=0;
        this.followingPacman=false;
    }

    private int calcDirectionFromNodes(Vertex vertex1, Vertex vertex2, int x, int y) {

        if (vertex1.getY()==y) {
            if (vertex1.getY() == vertex2.getY()) {
                // pacman, v1 and v2 are all on the same row.
                int pos=x;
                int v1=vertex1.getX();
                int v2=vertex2.getX();

                if ((pos < v1) && (pos < v2)) {
                    // head for the first node.
                    return Direction.right;
                } else if (pos == v1) {
                    // we're sitting on the first node, head for the second one.
                    return (pos<v2)? Direction.right: Direction.left;
                } else if ( ((v1 < pos) && (pos < v2)) || (v2 < pos) && (pos < v1)) {
                    // we're between the two, [v1]==[v2] or [v2]==[v1] head for the second node.
                    return (pos<v2)? Direction.right: Direction.left;
                } else if (pos == v2) {
                    // we're sitting on the second node (we need to run Dijkstra again!)
                    return Direction.none;
                } else if ((v1 < pos) && (v2 < pos)) {
                    // we're out past the second node (which shouldn't really happen!)
                    return Direction.left;  //-1;
                }
            } else {
                // pacman is on the same row as the first node, but the second node is either above or below, on a vertical edge.
                if (x<v1.getX())
                    return Direction.right;
                else if (x==v1.getX()) {
                    // this means we're on the first node... now we need to figure out up or down to v2!!!
                    if (y<v2.getY())
                        return Direction.down;
                    else
                        return Direction.up;
                }
                else
                    return Direction.left;
            }
        } else if (vertex1.getX()==x) {
            if (vertex1.getX() == vertex2.getX()) {
                // pacman, and the two nodes, are all in a vertical line.
                int pos = y;
                int v1 = vertex1.getY();
                int v2 = vertex2.getY();

                if ((pos < v1) && (pos < v2)) {
                    // head for the first node.
                    return Direction.down;
                } else if (pos == v1) {
                    // we're sitting on the first node, head for the second one.
                    return (pos < v2) ? Direction.down : Direction.up;
                } else if (((v1 < pos) && (pos < v2)) || (v2 < pos) && (pos < v1)) {
                    // we're between the two, [v1]==[v2] or [v2]==[v1] head for the second node.
                    return (pos < v2) ? Direction.down : Direction.up;
                } else if (pos == v2) {
                    // we're sitting on the second node (we need to run Dijkstra again!)
                    return Direction.none;
                } else if ((v1 < pos) && (v2 < pos)) {
                    // we're out past the second node (which shouldn't really happen!)
                    return Direction.up; // -1;
                }
            } else {
                // pacman and the first node are in the save vertical line, but the 2nd node is either to the left or right of the first.
                if (y<v1.getY())
                    return Direction.down;
                else if (y==v1.getY()) {
                    if (x<v1.getX())
                        return Direction.right;
                    else
                        return Direction.left;
                } else
                    return Direction.up;
            }
        }
        return -1;
    }

}
