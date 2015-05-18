package Dijkstra;

import Application.*;

import java.util.*;


/**
 * Created by Sarah on 10/05/2015.
 */
public class PathFinderDijkstra {

    private List<Vertex> nodes;
    private List<Edge> edges;
    private Graph graph=null;

    //                    0  1   2   3   4   5  6  7  8   9  10  11  12  13 14 15 16  17  18  19  20  21  22  23  24  25  26  27  28  29  30  31  32  33  34  35  36  37  38  39  40  41  42  43  44  45  46  47  48  49  50  51  52  53  54  55  56  57  58  59  60  61  62  63  64  65!!!            ->69!
    private int nodeXs[]={1, 6, 12, 15, 21, 26, 1, 6, 9, 12, 15, 18, 21, 26, 1, 6, 9, 12, 15, 18, 21, 26,  9, 12, 15, 18,  0,  6,  9, 18, 21, 28,  9, 18,  1,  6,  9, 12, 15, 18, 21, 26,  1,  3,  6,  9, 12, 15, 18, 21, 24, 26,  1,  3,  6,  9, 12, 15, 18, 21, 24, 26,  1, 12, 15, 26,  13, 13, 12, 15};
    private int nodeYs[]={1, 1,  1,  1,  1,  1, 5, 5, 5,  5,  5,  5,  5,  5, 8, 8, 8,  8,  8,  8,  8,  8, 11, 11, 11, 11, 14, 14, 14, 14, 14, 14, 17, 17, 20, 20, 20, 20, 20, 20, 20, 20, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 29, 29, 29, 29,  11, 14, 14, 14};

    private static PathFinderDijkstra instance=null;
    public static PathFinderDijkstra getInstance() {
        return instance;
    }

    public PathFinderDijkstra() {
        instance=this;

        nodes = new ArrayList<Vertex>();
        for (int ii=0; ii<=69; ii++) {
            Vertex location = new Vertex(""+ii, ii, nodeXs[ii], nodeYs[ii]);
            nodes.add(location);
        }

        edges = new ArrayList<Edge>();
        List<Edge> withPills=new ArrayList<>();
        List<Edge> withoutPills=new ArrayList<>();

        addEdges(withPills, 0, 1, 6);       addEdges(withPills, 1, 2, 7);       addEdges(withPills, 3, 4, 7);
        addEdges(withPills, 4, 5, 6);       addEdges(withPills, 0, 6, 5);       addEdges(withPills, 6, 14, 4);
        addEdges(withPills, 1, 7, 5);       addEdges(withPills, 7, 15, 4);      addEdges(withPills, 15, 27, 7);
        addEdges(withPills, 27, 35, 7);     addEdges(withPills, 35, 44, 4);     addEdges(withPills, 44, 54, 4);
        addEdges(withPills, 2, 9, 5);       addEdges(withPills, 3, 10, 5);      addEdges(withPills, 4, 12, 5);
        addEdges(withPills, 12, 20, 4);     addEdges(withPills, 20, 30, 7);     addEdges(withPills, 30, 40, 7);
        addEdges(withPills, 40, 49, 4);     addEdges(withPills, 49, 59, 4);     addEdges(withPills, 5, 13, 5);
        addEdges(withPills, 13, 21, 4);     addEdges(withPills, 6, 7, 6);       addEdges(withPills, 7, 8, 4);
        addEdges(withPills, 8, 9, 4);       addEdges(withPills, 9, 10, 4);      addEdges(withPills, 10, 11, 4);
        addEdges(withPills, 11, 12, 4);     addEdges(withPills, 12, 13, 6);     addEdges(withPills, 8, 16, 4);
        addEdges(withPills, 11, 19, 4);     addEdges(withPills, 14, 15, 6);     addEdges(withPills, 16, 17, 4);
        addEdges(withPills, 18, 19, 4);     addEdges(withPills, 20, 21, 6);     addEdges(withPills, 34, 35, 6);
        addEdges(withPills, 35, 36, 4);     addEdges(withPills, 36, 37, 4);     addEdges(withPills, 38, 39, 4);
        addEdges(withPills, 39, 40, 4);     addEdges(withPills, 40, 41, 6);     addEdges(withPills, 34, 42, 4);
        addEdges(withPills, 41, 51, 4);     addEdges(withPills, 42, 43, 3);     addEdges(withPills, 50, 51, 3);
        addEdges(withPills, 37, 46, 4);     addEdges(withPills, 38, 47, 4);     addEdges(withPills, 43, 53, 4);
        addEdges(withPills, 50, 60, 4);     addEdges(withPills, 44, 45, 4);     addEdges(withPills, 45, 46, 4);
        addEdges(withPills, 47, 48, 4);     addEdges(withPills, 48, 49, 4);     addEdges(withPills, 45, 55, 4);
        addEdges(withPills, 48, 58, 4);     addEdges(withPills, 52, 53, 3);     addEdges(withPills, 53, 54, 4);
        addEdges(withPills, 55, 56, 4);     addEdges(withPills, 57, 58, 4);     addEdges(withPills, 59, 60, 4);
        addEdges(withPills, 60, 61, 3);     addEdges(withPills, 52, 62, 4);     addEdges(withPills, 56, 63, 4);
        addEdges(withPills, 57, 64, 4);     addEdges(withPills, 61, 65, 4);     addEdges(withPills, 61, 65, 4);
        addEdges(withPills, 62, 63, 12);    addEdges(withPills, 63, 64, 4);     addEdges(withPills, 64, 65, 12);

        addEdges(withoutPills, 17, 23, 4);  addEdges(withoutPills, 18, 24, 4);  addEdges(withoutPills, 22, 23, 4);
        addEdges(withoutPills, 23, 24, 4);  addEdges(withoutPills, 24, 25, 4);  addEdges(withoutPills, 22, 28, 4);
        addEdges(withoutPills, 25, 29, 4);  addEdges(withoutPills, 26, 27, 7);  addEdges(withoutPills, 27, 28, 4);
        addEdges(withoutPills, 29, 30, 4);  addEdges(withoutPills, 30, 31, 7);  addEdges(withoutPills, 28, 32, 4);
        addEdges(withoutPills, 29, 33, 4);  addEdges(withoutPills, 32, 33, 10); addEdges(withoutPills, 32, 36, 4);
        addEdges(withoutPills, 33, 39, 4);  addEdges(withoutPills, 23, 66, 2);  addEdges(withoutPills, 24, 66, 3);
        addEdges(withoutPills, 66, 67, 4);  addEdges(withoutPills, 67, 68, 2);  addEdges(withoutPills, 67, 69, 3);
        addEdges(withoutPills, 46, 47, 4);

        edges.addAll(withoutPills);
        edges.addAll(withPills);
        graph = new Graph(nodes, edges);
        Maze.getInstance().assignGraph(graph, nodes, withPills, withoutPills);
    }

    private void addEdges(List<Edge> list, int sourceLocNo, int destLocNo, int duration) {
        String laneId="Edge"+sourceLocNo+"-"+destLocNo;
        list.add(new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration));
        laneId="Edge"+destLocNo+"-"+sourceLocNo;
        list.add(new Edge(laneId, nodes.get(destLocNo), nodes.get(sourceLocNo), duration));
    }

    public LinkedList<Vertex> getNextStepOnShortestPath(int x, int y, int targetX, int targetY) {
        int nearestFrom = findNearestNode(x, y);
        int nearestTo = findNearestNode(targetX, targetY);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(nearestFrom));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(nearestTo));
        return path;
    }

    private int findNearestNode(int x, int y) {
        HashMap<Double, Vertex> distances=new HashMap<>();

        Iterator iterator=nodes.iterator();
        while (iterator.hasNext()) {
            Vertex vertex=(Vertex)iterator.next();
            Double distance= Direction.getEuclideanDistance(x, y, vertex.getX(), vertex.getY());
            distances.put(distance, vertex);
        }

        ArrayList<Double> list=new ArrayList();
        list.addAll(distances.keySet());
        Collections.sort(list);

        Vertex v=distances.get(list.get(0));
        return Integer.valueOf(v.getId());

    }
}



