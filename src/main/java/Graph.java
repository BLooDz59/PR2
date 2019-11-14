package main.java;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private Node[] graph;
    private List<Node> nodeWithPods;
    private Node qg;

    public Graph(int size) {
        graph = new Node[size];
        nodeWithPods = new ArrayList<>();
        qg = null;
    }

    public void addNode(Node node) { graph[node.getId()] = node; }

    public void removeNode(int id) { graph[id] = null; }

    public Node getNode(int id){ return graph[id]; }

    public void addLinkBetweenNodes(Node nodeA, Node nodeB) {
        nodeA.addLinkedNode(nodeB);
        nodeB.addLinkedNode(nodeA);
    }

    public void removeLinkBetweenNodes(Node nodeA, Node nodeB) {
        nodeA.removeLinkedNode(nodeB);
        nodeB.removeLinkedNode(nodeA);
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

    public void removeNodeWithPods(Node node){ nodeWithPods.remove(node); }

    public void setQG(Node node) { qg = node; }

    public Node getQG() { return qg; }
}
