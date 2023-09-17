import java.util.Locale;

public class Edge {
    private Vertex source;
    private Vertex target;
    private Properties properties;

    public Edge(Vertex source, Vertex target, Properties properties) {
        this.source = source;
        this.target = target;
        this.properties = properties;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    public int getWeight() {
        return this.properties.weight;
    }
}
