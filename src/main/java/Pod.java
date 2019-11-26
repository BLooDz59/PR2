package main.java;

import java.util.ArrayList;

public class Pod {
    private final int ID;
    private Node coord;
    private Node target;
    private int quantity;
    private ArrayList<Integer> path;
    private boolean fighting;
    private boolean reachedPath;
    private boolean canBeMerged;

    public Pod(Node coord, int quantity, int id) {
        ID = id;
        this.coord = coord;
        this.quantity = quantity;
        path = new ArrayList<>();
        reachedPath = false;
        canBeMerged = true;
    }

    //Getters

    public Node getNodeOn() { return coord; }

    public int getID() { return ID; }

    public int getQuantity() { return quantity; }

    public boolean hasPath() { return path.size() > 0; }

    public int getPathNodeId() { return path.get(0); }

    public Node getTarget() { return target; }

    //Setters

    public void setFighting(boolean val) { fighting = val; }

    public void setNodeOn(Node coord) { this.coord = coord; }

    public void setTarget(Node target) {
        this.target = target;
        target.setTargeted(true);
        path = PathFinding.BFS(target, coord);
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setCanBeMerged(boolean val) { canBeMerged = val; }

    /**
     * Remove the first elements of the path sequence
     * If the path sequence length equal to zero,
     * put the value of reachedPath to true, and target targeted status to false.
     */
    public void removePathNextElement() {
        if(hasPath()) {
            path.remove(0);
        }
        else {
            target.setTargeted(false);
            reachedPath = true;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) { return true; }
        if(obj == null || obj.getClass() != this.getClass()) { return false; }
        Pod pod = (Pod) obj;
        return this.ID == pod.ID;
    }

    @Override
    public String toString() { return quantity + " pods on node " + coord.getId() + " dest" + target.getId(); }
}
