package Application;

import Dijkstra.Edge;
import Dijkstra.Graph;
import Dijkstra.Vertex;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Maze {

    ArrayList<ArrayList<ArrayList<IDrawable>>> board=new ArrayList<>();
    public static int width=28;
    public static int height=32; // +1 for the score line.
    public static int boardHeight=31;
    public static int cellwidth=12;
    public static int cellheight=12;

    private static Maze instance=null;
    public Maze() throws Exception {
        instance=this;
        setupMaze();
    }
    public static Maze getInstance() { return instance; }

    private Graph graph=null;

    private void setupMaze() throws Exception {

        // create the 2D array, of ArrayLists, where each element is an ArrayList of IDrawables.
        // fill it all with walls! and then dig the tunnels.
        for (int ii = 0; ii < width; ii++) {
            ArrayList<ArrayList<IDrawable>> row = new ArrayList<>();
            board.add(row);
            for (int jj = 0; jj < height; jj++) {
                ArrayList<IDrawable> list = new ArrayList<>();
                row.add(list);
                if (jj<boardHeight)
                    putItemOntoBoard(ii, jj, new Wall());
            }
        }

        putItemOntoBoard(1, 3, new SuperPill());
        putItemOntoBoard(1, 23, new SuperPill());
        putItemOntoBoard(26, 3, new SuperPill());
        putItemOntoBoard(26, 23, new SuperPill());
    }

    public void assignGraph(Graph graph, List<Vertex> nodes, List<Edge> withPills, List<Edge> withoutPills) {
        this.graph=graph;

        for (Edge edge : withPills) {
            this.addLaneToBoard(edge.getSource().getNodeId(), edge.getDestination().getNodeId(), true);
        }
        for (Edge edge : withoutPills) {
            this.addLaneToBoard(edge.getSource().getNodeId(), edge.getDestination().getNodeId(), false);
        }

        printOutCoordsForNode(25);
        printOutCoordsForNode(30);
        printOutNodeAtCoords(21, 5);
        printOutNodeAtCoords(1, 20);
    }

    public void smoothWalls() {
        for (int ii = 0; ii < width; ii++) {
            for (int jj = 0; jj < boardHeight; jj++) {
                List<IDrawable> stack=board.get(ii).get(jj);
                for (IDrawable thing:stack) {
                    if ((thing instanceof Wall)==true) {
                        ((Wall)thing).smooth(this);
                    }
                }
            }
        }
    }

    //                    0  1   2   3   4   5  6  7  8   9  10  11  12  13 14 15 16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31  32  33  34  35  36  37  38  39  40  41  42  43  44  45  46  47  48  49  50  51  52  53  54  55  56  57  58  59  60  61  62  63  64  65!!!            ->69!
    private int nodeXs[]={1, 6, 12, 15, 21, 26, 1, 6, 9, 12, 15, 18, 21, 26, 1, 6, 9, 12, 15, 18, 21, 26,  9, 12, 15, 18,  0,  6,  9, 18, 21, 28,  9, 18,  1,  6,  9, 12, 15, 18, 21, 26,  1,  3,  6,  9, 12, 15, 18, 21, 24, 26,  1,  3,  6,  9, 12, 15, 18, 21, 24, 26,  1, 12, 15, 26,  13, 13, 12, 15};
    private int nodeYs[]={1, 1,  1,  1,  1,  1, 5, 5, 5,  5,  5,  5,  5,  5, 8, 8, 8,  8,  8,  8,  8,  8, 11, 11, 11, 11, 14, 14, 14, 14, 14, 14, 17, 17, 20, 20, 20, 20, 20, 20, 20, 20, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 29, 29, 29, 29,  11, 14, 14, 14};

    public void addLaneToBoard(int fromNode, int toNode, boolean withPills) {
        int fromX=nodeXs[fromNode];
        int fromY=nodeYs[fromNode];
        int toX=nodeXs[toNode];
        int toY=nodeYs[toNode];

        if (fromX==toX) {
            for (int ii=fromY; ii<=toY; ii++) {

                // remove any walls first.
                if (isWall(fromX, ii) == true) {
                    for (IDrawable item: board.get(fromX).get(ii)) {
                        if (item instanceof Wall) {
                            board.get(fromX).get(ii).remove(item);
                            break;
                        }
                    }
                }

                // now if we need to, add a pill, but just one! ;)
                if ((withPills==true) && (isPill(fromX, ii) == false)) {
                    putItemOntoBoard(fromX, ii, new Pill());
                }
            }
            return;
        }

        if (fromY==toY) {
            for (int ii=fromX; ii<=toX; ii++) {

                // remove any walls first.
                if (isWall(ii, fromY) == true) {
                    for (IDrawable item: board.get(ii).get(fromY)) {
                        if (item instanceof Wall) {
                            board.get(ii).get(fromY).remove(item);
                            break;
                        }
                    }
                }
                // now if we need to, add a pill, but just one! ;)
                if ((withPills == true) && (isPill(ii, fromY) == false)) {
                    putItemOntoBoard(ii, fromY, new Pill());
                }
            }
            return;
        }

        System.out.println("Error in addLaneToBoard(" + fromNode + "," + toNode + "), from(" + fromX + "," + fromY + ") to(" + toX + "," + toY + ")  :(");
    }

    public void addToBoard(IDrawable thing, int x, int y) {
        putItemOntoBoard(x, y, thing);
        thing.updatePosition(x, y);
    }

    public void drawMaze(GraphicsContext gc) {
        gc.setFill(Paint.valueOf("black"));
        gc.fillRect(0, 0, (width * cellwidth), (height * cellheight));

        ArrayList<IDrawable> moveables=new ArrayList<>();
        for (int ii=0; ii<width; ii++) {
            ArrayList<ArrayList<IDrawable>> row=board.get(ii);
            for (int jj=0; jj<height; jj++) {
                ArrayList<IDrawable> list=row.get(jj);
                Iterator it=list.iterator();
                while (it.hasNext()) {
                    IDrawable thing=(IDrawable)it.next();
                    if (thing instanceof IMoveable)
                        moveables.add(thing);
                    else
                        thing.draw(gc);
                }
            }
        }

        Iterator iterator=moveables.iterator();
        while (iterator.hasNext()) {
            ((IDrawable)iterator.next()).draw(gc);
        }
    }

    private void putItemOntoBoard(int x, int y, IDrawable thing) {
        ArrayList<IDrawable> list=board.get(x).get(y);
        thing.updatePosition(x, y);
        list.add(thing);
    }

    private ArrayList<IDrawable> getItemStack(int x, int y) {
        return board.get(x).get(y);
    }

    public boolean isWall(int x, int y) {
        if ((x<0) || (x>=width) || (y<0) || (y>=boardHeight))
            return false;

        ArrayList<IDrawable> list=getItemStack(x, y);
        if (list!=null) {
            Iterator iterator=list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() instanceof Wall) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPill(int x, int y) {
        ArrayList<IDrawable> list=getItemStack(x, y);
        if (list!=null) {
            Iterator iterator=list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() instanceof Pill) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isJunction(int x, int y) {
        int count=0;
        if (isWall(x-1, y  )==false) count++;
        if (isWall(x  , y-1)==false) count++;
        if (isWall(x  , y+1)==false) count++;
        if (isWall(x+1, y  )==false) count++;
        if (count>2)
            return true;
        return false;
    }

    public void moveThing(IDrawable thing, int x, int y, int newX, int newY) throws WallBashException {
        int score=0;
        ArrayList<IDrawable> currentPos=getItemStack(x, y);
        currentPos.remove(thing);
        ArrayList<IDrawable> newPos=getItemStack(newX, newY);
        Iterator it=newPos.iterator();
        while (it.hasNext()) {
            IDrawable item=(IDrawable)it.next();
            if (item instanceof Wall) {
                throw new WallBashException(thing, x, y, newX, newY);
            }
            if (item instanceof IConsumable) {
                if (((IConsumable)item).eat((IMoveable)thing)==true)
                    score+=((IConsumable)item).getScore();
            }
            if (item instanceof IMoveable) {
                ((IConsumable)thing).eat((IMoveable)item);
                score+=((IConsumable)thing).getScore();
            }
        }
        newPos.add(thing);
        Main.getInstance().UpdateScore(score);
    }

    public static int checkForTunnelAndCalculateX(int pos) {
        return (pos==28)?0:(pos==-1)?27:pos;
    }

    public void printOutCoordsForNode(int nodeId) {
        System.out.println("node "+nodeId+" is at location x="+graph.getVertexes().get(nodeId).getX()+", y="+graph.getVertexes().get(nodeId).getY());
    }

    public void printOutNodeAtCoords(int x, int y) {
        Vertex node=null;
        for (Vertex v:graph.getVertexes()) {
            if ((v.getX()==x) && (v.getY()==y))
                node=v;
        }
        if (node==null) {
            System.out.println("there is no node at location x="+x+", y="+y);
        } else {
            System.out.println("node " + node.getId() + " is at location x=" + x + ", y=" + y);
        }
    }
}
