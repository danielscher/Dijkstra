import java.util.*;
import java.util.stream.Collectors;

class Dijkstra {


    public List<Integer> getShortestPath (Graph graph, final int source, final int target){
        HashMap<Integer, Integer> parentMap = new HashMap<>();
        HashMap<Integer, Integer> distancesFromSource = dijkstra(graph, source, false, parentMap);
        return reconstructShortestPath(source, target, distancesFromSource, parentMap);
    }


    /**
     * @return the shortest distance between two vertices.
     * */
    public int getShortestDistance(final int source, final int target, Graph graph) {
        HashMap<Integer,Integer> distancesFromSource = dijkstra(graph, source, false, new HashMap<>());
        return distancesFromSource.get(target);
    }

    /**
     * @return the nearest base of a certain type to the edge.
     * */
    public int getNearestBaseToEdge(Graph graph, final Edge targetEdge, final int baseType) {
        HashMap<Integer, Integer> nearestBasesToVertexA = findNearestBases(graph, targetEdge.getSource().getVertexId(), baseType);
        HashMap<Integer, Integer> nearestBasesToVertexB = findNearestBases(graph, targetEdge.getSource().getVertexId(), baseType);

        int BaseA = nearestBasesToVertexA.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get().getKey();
        int BaseB = nearestBasesToVertexB.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .get().getKey();

        return (nearestBasesToVertexA.get(BaseA) < nearestBasesToVertexB.get(BaseB) ? BaseA : BaseB);
    }

    // returns a map of all bases of a given type and their distance from the source.
    private HashMap<Integer, Integer> findNearestBases(Graph graph, int vertexId, int baseType) {
        HashMap<Integer, Integer> nearestBases = new HashMap<>();

        HashMap<Integer,Integer> distancesFromSource = dijkstra(graph, vertexId, true, new HashMap<>());

        Set<Integer> filteredVertexIds = distancesFromSource.keySet().stream()
                .filter(vId -> graph.getVertices().get(vId).getBaseType() == baseType)
                .collect(Collectors.toSet());

        for (Integer vId : filteredVertexIds) {
            nearestBases.put(vId, distancesFromSource.get(vId));
        }

        return nearestBases;
    }

    private HashMap<Integer,Integer> dijkstra(Graph graph, final int source, boolean reverse, HashMap<Integer,Integer> parentMap) {
        HashMap<Integer,Boolean> visited = new HashMap<>();
        HashMap<Integer,Integer> distancesFromSource = new HashMap<>();

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
            updateParent(current, distancesFromSource, reverse, parentMap);
        }
        return distancesFromSource;
    }

    private void updateParent(Vertex vertex, HashMap<Integer, Integer> distancesFromSource,
                              boolean reverse, HashMap<Integer, Integer> parentMap) {
        for (Edge edge : vertex.getEdges(reverse)) {
            int newDistance = distancesFromSource.get(vertex.getVertexId()) + edge.getWeight();
            int oldDistance = distancesFromSource.get(edge.getTarget().getVertexId());
            if (newDistance < oldDistance) {
                distancesFromSource.replace(edge.getTarget().getVertexId(), newDistance);
                parentMap.put(edge.getTarget().getVertexId(), vertex.getVertexId()); // Update parent
            }
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

    private List<Integer> reconstructShortestPath(int source, int target,
                                                  HashMap<Integer, Integer> distancesFromSource,
                                                  HashMap<Integer, Integer> parentMap) {
        List<Integer> path = new ArrayList<>();
        int current = target;

        while (current != source) {
            path.add(current);
            current = parentMap.get(current);
        }

        path.add(source);
        Collections.reverse(path); // Reverse the list to get the correct order.
        return path;
    }

}
