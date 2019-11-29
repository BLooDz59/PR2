package main.java;

import java.util.ArrayList;
import java.util.Random;

public class Pod {
    private final int ID;
    private Node coord;
    private Node target;
    private int quantity;
    private ArrayList<Integer> path;
    private ArrayList<Node> historicPath;
    private boolean fighting;
    private boolean reachedPath;
    private boolean canBeMerged;

    private Node lastTarget;
    private State state;

    public enum State { EXPLORER, RUSHER, SPAWNED, NONE }

    public Pod(Node coord, int quantity, int id) {
        ID = id;
        this.coord = coord;
        this.quantity = quantity;
        path = new ArrayList<>();
        historicPath = new ArrayList<>();
        reachedPath = false;
        canBeMerged = true;
        reachedPath = false;
        state = State.SPAWNED;
        lastTarget = null;
    }

    public Pod(Node coord, int quantity, int id, State state) {
        this(coord, quantity, id);
        this.state = state;
    }

    //Getters

    public Node getNodeOn() { return coord; }

    public int getID() { return ID; }

    public int getQuantity() { return quantity; }

    public boolean hasPath() { return path.size() > 0; }

    public int getPathNodeId() { return path.get(0); }

    public ArrayList<Node> getHistoricPath() { return historicPath; }

    public Node getTarget() { return target; }

    public Node getLastTarget() { return lastTarget; }

    public State getState() { return state; }

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

    public void setState(State state){this.state = state;}

    public void setHistoricPath(ArrayList<Node> historicPath) { this.historicPath = historicPath; }

    public void setLastTarget(Node lastTarget) { this.lastTarget = lastTarget; }

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

    public void addHistoricElement(Node element) { historicPath.add(element); }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) { return true; }
        if(obj == null || obj.getClass() != this.getClass()) { return false; }
        Pod pod = (Pod) obj;
        return this.ID == pod.ID;
    }

    public void run() {
        switch (state) {
            case RUSHER:
                rush();
                break;
            case EXPLORER:
                explore();
                break;
        }
    }

    private void explore() {
        Node bestTarget = selectBestTarget();
        if (bestTarget == null) bestTarget = StrategyManager.getInstance().getMap().getEnemyQG();
        setTarget(bestTarget);
    }

    private void rush() {
        StrategyManager strategyManager = StrategyManager.getInstance();
        for (Node node : getNodeOn().getLinkedNodes()){
            if(node.getOwnerID() == (strategyManager.getPlayerID()^1) && node.getPlatinumProduction() !=0 ){
                setTarget(node);
                break;
            }
        }
        if(!hasPath()) setTarget(strategyManager.getMap().getEnemyQG());
    }

    private Node selectBestTarget() {
        Node ret = null;
        int bestInterest = 0;
        for (Node n : getNodeOn().getLinkedNodes()) {
            if(n.getInterest() > bestInterest) {
                ret = n;
                bestInterest = n.getInterest();
            }
        }
        return ret;
    }

    @Override
    public String toString() { return quantity + " pods on node " + coord.getId(); }
}
