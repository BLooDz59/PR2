package main.java;

import java.util.ArrayList;

public class Pod {
    Node coord;
    int id;
    int quantity;
    boolean dead;
    ArrayList<Integer> path;

    public Pod(Node coord, int id, int quantity) {
        this.coord = coord;
        this.id = id;
        this.quantity = quantity;
        dead = false;
        path = new ArrayList<>();
    }

    public void setNodeOn(Node coord) { this.coord = coord; }

    public void setTarget(Node target) { path = PathFinding.BFS(target, coord); }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Node getNodeOn() { return coord; }

    public int getId() { return id; }

    public int getQuantity() { return quantity; }

    public boolean isDead() { return dead; }

    public int getPathNodeId() {
        int dest = 0;
        if(path.size() != 0){
            dest = path.get(0);
            path.remove(0);
        }
        return dest;
    }

    public boolean getTarget() { return path.size() != 0; }

    @Override
    public String toString() { return quantity + " pods on node " + coord.getId(); }
}
