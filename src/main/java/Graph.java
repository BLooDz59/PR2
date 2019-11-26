package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Graph {

    private Node[] graph;
    private List<Node> nodeWithPods;
    private Node qg;
    private Node enemyQG;

    public Graph(int size) {
        graph = new Node[size];
        nodeWithPods = new ArrayList<>();
        qg = null;
        enemyQG = null;
    }


    //Getters

    public Node getNode(int id){ return graph[id]; }

    public List<Node> getNodesWithPods() { return nodeWithPods; }

    public Node getQG() { return qg; }

    public Node getEnemyQG() { return enemyQG; }

    public List<Node> getVisibleNode() {
        ArrayList<Node> ret = new ArrayList<>();
        for(Node n : graph) {
            if (n.isVisible() == 1) {
                ret.add(n);
            }
        }
        return ret;
    }

    //Setters

    public void setQG(Node node) { qg = node; }

    public void setEnemyQG(Node node) { enemyQG = node; }

    /**
     * Add node in the graph
     * @param node to add in the graph
     */
    public void addNode(Node node) { graph[node.getId()] = node; }

    /**
     * Create a link between nodeA and nodeB
     * @param nodeA
     * @param nodeB
     */
    private void addLinkBetweenNodes(Node nodeA, Node nodeB) {
        nodeA.addLinkedNode(nodeB);
        nodeB.addLinkedNode(nodeA);
    }

    /**
     * Create a link between nodeA and nodeB from their id
     * @param nodeIdA
     * @param nodeIdB
     */
    public void addLinkBetweenNodesFromId(int nodeIdA, int nodeIdB){
        addLinkBetweenNodes(this.getNode(nodeIdA), this.getNode(nodeIdB));
    }

    /**
     * Add node to the list of node which contains pods
     * @param node
     */
    public void addNodeWithPods(Node node){
        if (!nodeWithPods.contains(node)){
            nodeWithPods.add(node);
        }
    }

    /**
     * Update the pods number for the given node and update the list of node with pods in consequence
     * @param playerID
     * @param node
     * @param pods
     * @param enemyPods
     */
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
            PodsManager.getInstance().removePod(PodsManager.getInstance().getPodOnNode(node));
        }
    }

    /**
     * Remove the given node from the list of node with pods
     * @param node
     */
    public void removeNodeWithPods(Node node){ nodeWithPods.remove(node); }

    public Node getNeighbourWithMaxPlatinium(Node node){
        Node ret = null;
        int quantity = 0;
        for(Node n : node.getLinkedNodes()) {
            if(n.getPlatinumProduction() > quantity) {
                ret = n;
                quantity = n.getPlatinumProduction();
            }
        }
        return ret;
    }

    /**
     * TEST METHOD : WILL BE DELETE
     * Give a random node from the graph
     * @return random Node from the graph
     */
    public Node getRandomNode() {
        Random r = new Random();
        return graph[r.nextInt(graph.length)];
    }
}
