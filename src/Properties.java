public class Properties {
    protected String village;
    protected String street;
    protected int weight;
    protected int height;
    protected int factor;

    public Properties(String village, String street, int weight) {
        this.village = village;
        this.street = street;
        this.weight = weight;
        this.height = 1;
        this.factor = 1;
    }
}
