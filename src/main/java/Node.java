package main.java;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int ID;
    private List<Node> linkedNodes;
    private int pods;
    private int enemyPods;
    private int platinumProductionAmount;
    private int visible;
    private int ownerID;
    private boolean BFSdiscovered;
    private Node BFSparent;
    private float interest;
    private boolean visited;
    private boolean targeted;

    public Node(int id, int platinumProductionAmount){
        this.ID = id;
        this.platinumProductionAmount = platinumProductionAmount;
        ownerID = -1;
        visible = 0;
        linkedNodes = new ArrayList<>();
        pods = 0;
        enemyPods = 0;
        BFSdiscovered = false;
        BFSparent = null;
        targeted = false;
        interest=0;
    }


    //Getters

    public int getId() { return ID; }

    public int getPlatinumProduction() { return platinumProductionAmount; }

    public int isVisible() { return visible; }

    public int getOwnerID() { return ownerID; }

    public int getEnemyPodsNumber() { return enemyPods; }

    public int getPodsNumber() { return pods; }

    public List<Node> getLinkedNodes() { return linkedNodes; }

    public boolean isBFSdiscovered() { return BFSdiscovered; }

    public Node getBFSparent(){ return BFSparent; }

    public boolean isTargeted() { return targeted; }

    public float getInterest() { return interest; }

    public boolean isVisited() { return visited; }

    public boolean isNeutral() { return ownerID < 0; }

    public List<Node> getLinkedNodesWithPlatinium() {
        ArrayList<Node> ret = new ArrayList<>();
        for(Node n : linkedNodes) {
            if (n.getPlatinumProduction() > 0) {
                ret.add(n);
            }
        }
        return ret;
    }


    //Setters

    public void setVisibility(int isVisible) { visible = isVisible; }

    public void setOwner(int ownerID) { this.ownerID = ownerID; }

    public void setPlatinumProduction(int amount) { platinumProductionAmount = amount; }

    public void setEnemyPodsNumber(int n) { enemyPods = n; }

    public void setPodsNumber(int n) { this.pods = n; }

    public void setBFSdiscovered(boolean BFSdiscovered) { this.BFSdiscovered = BFSdiscovered; }

    public void setBFSparent(Node node){ BFSparent = node; }

    public void setTargeted(boolean val) { targeted = val; }

    public void setInterest(float interest) { this.interest = interest; }

    public void setVisited(boolean val) { visited = val; }

    /**
     * Add the node given in parameters to the list of neighbours
     * @param node to add in neighbours list
     */
    public void addLinkedNode(Node node) { linkedNodes.add(node); }

    /**
     * Remove the node given in parameters from the list of neighbours
     * @param node to remove from the neighbours list
     */
    public void removeLinkedNode(Node node) { linkedNodes.remove(node); }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) { return true; }
        if(obj == null || obj.getClass() != this.getClass()) { return false; }
        Node node = (Node) obj;
        return this.ID == node.ID;
    }
}
