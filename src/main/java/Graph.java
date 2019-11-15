package main.java;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private Node[] graph;
    private List<Node> nodeWithPods;
    private Node qg;
    private Node enemyQG;

    public Graph(int size) {
        graph = new Node[size];
        nodeWithPods = new ArrayList<>();
        qg = null;
    }

    public void addNode(Node node) { graph[node.getId()] = node; }

    public Node getNode(int id){ return graph[id]; }

    public void addLinkBetweenNodes(Node nodeA, Node nodeB) {
        nodeA.addLinkedNode(nodeB);
        nodeB.addLinkedNode(nodeA);
    }

    public void addLinkBetweenNodesFromId(int nodeIdA, int nodeIdB){
        addLinkBetweenNodes(this.getNode(nodeIdA), this.getNode(nodeIdB));
    }

    public List<Node> getNodesWithPods() {
        return nodeWithPods;
    }

    public void addNodeWithPods(Node node){
        if (!nodeWithPods.contains(node)){
            nodeWithPods.add(node);
        }
    }

    public void updatePods(int playerID, Node node, int pods, int enemyPods){
        if (playerID == 0) {
            node.setPodsNumber(pods);
            node.setEnemyPodsNumber(enemyPods);
        } else {
            node.setPodsNumber(enemyPods);
            node.setEnemyPodsNumber(pods);
        }
        if(node.getPodsNumber() > 0){
            addNodeWithPods(node);
        }
        else {
            removeNodeWithPods(node);
        }
    }

    public void removeNodeWithPods(Node node){ nodeWithPods.remove(node); }

    public void setQG(Node node) { qg = node; }

    public Node getQG() { return qg; }

    public void setEnemyQG(Node node) { enemyQG = node; }

    public Node getEnemyQG() { return enemyQG; }
}
