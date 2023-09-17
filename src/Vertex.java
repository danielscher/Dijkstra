import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public class Vertex {
    private int vertexId;
    private List<Edge> outgoingEdges = new ArrayList<>();
    private List<Edge> incomingEdges = new ArrayList<>();
    private int baseId;
    private int baseType;

    public Vertex(int vertexId, int baseId, int baseType) {
        this.vertexId = vertexId;
        this.baseId = baseId;
        this.baseType = baseType;
    }


    public void addOutgoingEdge(Edge edge) {
        outgoingEdges.add(edge);
    }

    public void addIncomingEdge(Edge edge) {
        incomingEdges.add(edge);
    }

    public Integer getVertexId() {
        return this.vertexId;
    }

    public Collection<? extends Edge> getEdges(boolean reverse) {
        if (reverse) {
            return this.incomingEdges;
        }
        return this.outgoingEdges;
    }

    public int getBaseId() {
        return baseId;
    }

    public int getBaseType() {
        return baseType;
    }
}
