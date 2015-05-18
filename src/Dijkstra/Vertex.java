package Dijkstra;

public class Vertex {
    final private String id;
    final private int nodeIdWasname;

    private int x=0;    //sjm
    private int y=0;    //sjm

    public Vertex(String id, int nodeId, int x, int y) {
        this.id = id;
        this.nodeIdWasname = nodeId;
        this.x=x; // sjm
        this.y=y; // sjm
    }
    public String getId() {
        return id;
    }

    public int getNodeId() {
        return nodeIdWasname;
    }

    public int getX() { return x; } // sjm
    public int getY() { return y; } // sjm

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return id+"(x="+x+","+y+")";
    }

}
