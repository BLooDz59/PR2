package main.java;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private List<Node> linkedNodes;
    private int pods;
    private int enemyPods;
    private int platinumProductionAmount;
    private int visible;
    private int ownerID;
    private boolean BFSdiscovered = false;
    private Node BFSparent;

    public Node(int id, int platinumProductionAmount){
        this.id = id;
        this.platinumProductionAmount = platinumProductionAmount;
        ownerID = -1;
        visible = 0;
        linkedNodes = new ArrayList<>();
        pods = 0;
        enemyPods = 0;
        BFSparent = null;
    }

    public int getId() { return id; }

    public int getPlatinumProduction() { return platinumProductionAmount; }

    public void setVisibility(int isVisible) { visible = isVisible; }

    public int isVisible() { return visible; }

    public void setOwner(int ownerID) { this.ownerID = ownerID; }

    public int getOwnerID() { return ownerID; }

    public void addLinkedNode(Node node) { linkedNodes.add(node); }

    public void removeLinkedNode(Node node) { linkedNodes.remove(node); }

    public void setPlatinumProduction(int amount) { platinumProductionAmount = amount; }

    public void setEnemyPodsNumber(int n) { enemyPods = n; }

    public int getEnemyPodsNumber() { return enemyPods; }

    public int getPodsNumber() { return pods; }

    public void setPodsNumber(int n) { this.pods = n; }

    public List<Node> getLinkedNodes() { return linkedNodes; }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) { return true; }
        if(obj == null || obj.getClass() != this.getClass()) { return false; }
        Node node = (Node) obj;
        return this.id == node.id;
    }

    public boolean isBFSdiscovered() { return BFSdiscovered; }

    public void setBFSdiscovered(boolean BFSdiscovered) { this.BFSdiscovered = BFSdiscovered; }

    public void setBFSparent(Node node){ BFSparent = node; }

    public Node getBFSparent(){ return BFSparent; }
}
