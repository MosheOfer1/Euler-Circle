import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class graph {
    private List<line> Edges;
    private List<dot> Vertices;

    public graph(List<line> Edges,List<dot> Vertices){
        this.Edges=Edges;
        this.Vertices=Vertices;
    }


    public List<line> getEdges() {
        return Edges;
    }

    public void setEdges(List<line> edges) {
        this.Edges = edges;
    }

    public List<dot> getVertices() {
        return Vertices;
    }

    public void setVertices(List<dot> vertices) {
        this.Vertices = vertices;
    }

    public boolean isEvenDegAndOneConnectedGraph() {
        if (Euler.connectedGraphs!=1 || Euler.dots.size()==1)
            return false;
        for (int i = 0; i < getVertices().size(); i++) {
            if (getVertices().get(i).getPointers().length%2!=0)
                return false;
        }
        return true;
    }


}
