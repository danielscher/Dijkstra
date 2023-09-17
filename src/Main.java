public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        Dijkstra dijkstra = new Dijkstra();
        Vertex v0 = new Vertex(0, 0, 3);
        Vertex v1 = new Vertex(1, -1, -1);
        Vertex v2 = new Vertex(2, 2, 3);
        Vertex v3 = new Vertex(3, -1, -1);

        graph.addEdge(v0, v1, new Properties("A","01str",2));
        graph.addEdge(v1, v0, new Properties("A","01str",2));
        graph.addEdge(v0, v2, new Properties("A","02str",10));
        graph.addEdge(v2, v0, new Properties("A","02str",10));
        graph.addEdge(v1, v2, new Properties("A","12str",3));
        graph.addEdge(v1, v3, new Properties("A","13str",8));
        graph.addEdge(v3, v1, new Properties("A","13str",8));
        graph.addEdge(v2, v3, new Properties("A","23str",2));

        /*
        *
        *   0 <---> 2
        *   |     / ^
        *   |   /   |
        *   v /     |
        *   1 <---> 3
        *
        *  0 -> 1,2
        *  1 -> 0,2,3
        *  2 -> 3
        *  3 -> 1
        *
        * */

        // should print 0.
        System.out.println(dijkstra.getNearestBaseToEdge(graph, graph.getStreet("A","13str"), 3));

    }
}