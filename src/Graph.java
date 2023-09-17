import java.util.*;

public class Graph {

    private HashMap<String,HashMap<String,Edge>> villages = new HashMap<>();
    private final HashMap<Integer, Vertex> vertices = new HashMap<>();

    public HashMap<Integer, Vertex> getVertices() {
        return vertices;
    }

    public void addEdge(Vertex source, Vertex target, Properties properties){
        Edge edge = new Edge(source, target, properties);
        // add edge to village.
        if(!villages.containsKey(properties.village)){
            HashMap<String,Edge> streets = new HashMap<>();
            streets.put(properties.street,edge);
            villages.put(properties.village,streets);
        }else{
            villages.get(properties.village).put(properties.street,edge);
        }
        // connect vertices.
        source.addOutgoingEdge(edge);
        target.addIncomingEdge(edge);

        // add vertices to graph.
        if (vertices.containsKey(source.getVertexId())) {
            vertices.get(source.getVertexId()).addOutgoingEdge(edge);
        } else {
            vertices.put(source.getVertexId(), source);
        }
    }

    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Vertex vertex : vertices.values()) {
            edges.addAll(vertex.getEdges(false));
        }
        return edges;
    }
    public Edge getStreet(String village, String street){
        return villages.get(village).get(street);
    }

}
