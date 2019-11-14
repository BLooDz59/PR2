package main.java;

public class Pod {
    Node coord;
    Node target;
    int id;
    int quantity;

    public Pod(Node coord, int id, int quantity) {
        this.coord = coord;
        this.id = id;
        this.quantity = quantity;
        target = null;
    }

    public void setNodeOn(Node coord) { this.coord = coord; }

    public void setTarget(Node target) { this.target = target; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Node getNodeOn() { return coord; }

    public Node getTarget() { return target; }

    public int getId() { return id; }

    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return quantity + " pods on node " + coord.getId();
    }
}
