import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class Dijkstra {

    HashMap<Integer,Boolean> visited = new HashMap<>();
    HashMap<Integer,Integer> distancesFromSource = new HashMap<>();

    /**
     * @return the shortest distance between two vertices.
     * */
    public int getShortestDistance(final int source, final int target, Graph graph) {
        dijkstra(graph, source, false);
        return distancesFromSource.get(target);
    }

    /**
     * @return the nearest base of a certain type to the edge.
     * */
    public int getNearestBaseToEdge(Graph graph, final Edge targetEdge, final int baseType) {
        HashMap<Integer, Integer> nearestBasesToVertexA = findNearestBases(graph, targetEdge.getSource().getVertexId(), baseType, false);
        HashMap<Integer, Integer> nearestBasesToVertexB = findNearestBases(graph, targetEdge.getSource().getVertexId(), baseType, true);

        int BaseA = nearestBasesToVertexA.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get().getKey();
        int BaseB = nearestBasesToVertexB.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get().getKey();

        return (nearestBasesToVertexA.get(BaseA) < nearestBasesToVertexB.get(BaseB) ? BaseA : BaseB);
    }

    // returns a map of all bases of a given type and their distance from the source.
    private HashMap<Integer, Integer> findNearestBases(Graph graph, int vertexId, int baseType, boolean reverse) {
        HashMap<Integer, Integer> nearestBases = new HashMap<>();

        dijkstra(graph, vertexId, reverse);

        Set<Integer> filteredVertexIds = distancesFromSource.keySet().stream()
                .filter(vId -> graph.getVertices().get(vId).getBaseType() == baseType)
                .collect(Collectors.toSet());

        for (Integer vId : filteredVertexIds) {
            nearestBases.put(vId, distancesFromSource.get(vId));
        }

        return nearestBases;
    }

    private void dijkstra(Graph graph, final int source, boolean reverse) {
        visited = new HashMap<>();
        distancesFromSource = new HashMap<>();

        for (Vertex vertex : graph.getVertices().values()) {
            visited.put(vertex.getVertexId(), false);
            distancesFromSource.put(vertex.getVertexId(), Integer.MAX_VALUE);
        }

        distancesFromSource.put(source, 0);
        while (visited.containsValue(false)){
            Vertex current = getMinDistance(graph.getVertices(),distancesFromSource, visited);
            assert current != null;
            visited.put(current.getVertexId(), true);

            // iterate through neighbors and relax edges.
            relaxEdges(current, distancesFromSource, reverse);
        }
    }

    // TODO: refactor this method to use a priority queue instead of iterating through all vertices.
    // returns an unvisited vertex with the minimum distance from the source.
    private Vertex getMinDistance(HashMap<Integer,Vertex>vertices, HashMap<Integer,Integer> distances, HashMap<Integer,Boolean> visited){

        Optional<Map.Entry<Integer, Integer>> closestNodeOptional = distances.entrySet().stream()
                .filter(entry -> !visited.get(entry.getKey())) // Filter out visited nodes
                .min(Map.Entry.comparingByValue()); // Find the minimum distance

        if (closestNodeOptional.isPresent()) {
            Map.Entry<Integer, Integer> closestNodeEntry = closestNodeOptional.get();
            int closestNode = closestNodeEntry.getKey();
            return vertices.get(closestNode);
        }
        return null;
    }

    // updates the tentative distance of all edges connected to the vertex.
    private void relaxEdges(Vertex vertex, HashMap<Integer,Integer> distancesFromSource, boolean reverse) {
        for (Edge edge : vertex.getEdges(reverse)) {
            int newDistance = distancesFromSource.get(vertex.getVertexId()) + edge.getWeight();
            int oldDistance = distancesFromSource.get(edge.getTarget().getVertexId());
            if (newDistance < oldDistance) {
                distancesFromSource.replace(edge.getTarget().getVertexId(), newDistance);
            }
        }
    }

}
