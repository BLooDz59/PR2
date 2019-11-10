package main.java;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private Node[] graph;

    public Graph(int size) { graph = new Node[size]; }

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
        ArrayList<Node> ret = new ArrayList<>();
        for (Node node : graph) {
            if (node.getPodsNumber() != 0){
                ret.add(node);
            }
        }
        return ret;
    }
}
