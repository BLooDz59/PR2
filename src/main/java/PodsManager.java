package main.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PodsManager {
    private List<Pod> pods;
    private Command command;
    private Graph MAP;
    private int MY_ID;
    private int creationID;


    /**
     * Private Constructor
     */
    private PodsManager() {
        pods = new ArrayList<>();
        command = new Command();
        creationID = 0;
    }

    /**
     * Unique PodsManager instance pre-initialized
     */
    private static PodsManager INSTANCE = new PodsManager();

    /**
     * Get the unique instance of PodsManager
     * @return instance of PodsManager
     */
    public static PodsManager getInstance() { return INSTANCE; }

    public void setPlayerID(int id) { MY_ID = id; }

    public void setGraph(Graph graph) { MAP = graph; }

    /**
     * Create a new Pod manage by the PodsManager
     * @param coord where the Pod will be created
     * @param quantity of unite that the Pod contain
     */
    public void createPod(Node coord, int quantity) {
        if(quantity != 0){
            pods.add(new Pod(coord, quantity, creationID));
            creationID++;
        }
    }

    /**
     * Create a new Pod manage by the PodsManager with a specific path
     * @param coord where the Pod will be created
     * @param quantity of unite that the Pod contain
     * @param path that the Pod will follow
     */
    public void createPod(Node coord, int quantity, ArrayList<Integer> path){
        if(quantity != 0){
            Pod p = new Pod(coord, quantity, creationID);
            p.setPath(path);
            pods.add(p);
            creationID++;
        }
    }

    /**
     * Check if multiple Pods are on the same node in the map and merge them
     * @param map of the game
     */
    public void checkMerge(Graph map) {
        for (Node node : map.getNodesWithPods()) {
            if(getPodsOnNode(node).size() > 1){
                mergePods(getPodsOnNode(node), node);
            }
        }
    }

    /**
     * Check if Pods have fought during the previous turn and update there quantity
     * @param map
     */
    public void checkBattle(Graph map) {
        for (Node node : map.getNodesWithPods()) {
            Pod pod = getPodOnNode(node);
            int mapPodsNumberInfo = node.getPodsNumber();
            if(pod.getQuantity() != mapPodsNumberInfo){
                pod.setQuantity(mapPodsNumberInfo);
                pod.setFighting(true);
            }
        }
    }

    /**
     * Merge multiple Pods to one global Pods on specific Node
     * @param pods the list of pods that will be merge
     * @param node the node where the pods are
     */
    public void mergePods(List<Pod> pods, Node node) {
        int quantity = 0;
        ArrayList<Integer> path = new ArrayList<>();
        for (Pod p : pods) {
            quantity += p.getQuantity();
            path = p.getPath();
            removePod(p);
        }
        createPod(node, quantity, path);
    }

    /**
     * Give the quantity of pods on the HQ
     * @param HQ the node of the HQ
     * @return the quantity of pods on the HQ
     */
    public int getPodQuantityOnQG(Node HQ){
        int ret = 0;
        for (Pod p : pods) {
            if(p.getNodeOn().equals(HQ)){
                ret += p.getQuantity();
            }
        }
        return ret;
    }

    /**
     * Remove pod from the PodsManager
     * @param pod to remove
     */
    public void removePod(Pod pod) {
        pods.remove(pod);
    }

    /**
     * Give the Pod on the specific node
     * @param node to test if it contains Pod
     * @return the Pod if it exist, null if not
     */
    public Pod getPodOnNode(Node node) {
        Pod ret = null;
        for (Pod p : pods) {
            if(p.getNodeOn().equals(node)){
                ret = p;
            }
        }
        return ret;
    }

    /**
     * Give the list of Pods on the specific node
     * @param node that may contains Pods
     * @return the list of pods that the node contains
     */
    private List<Pod> getPodsOnNode(Node node){
        ArrayList<Pod> ret = new ArrayList<>();
        for (Pod p : pods) {
            if(p.getNodeOn().equals(node)){
                ret.add(p);
            }
        }
        return ret;
    }

    /**
     * Move all the pods manage by the PodsManager
     * @param graph
     */
    public void movePods(Graph graph){
        Iterator<Pod> itr = pods.iterator();
        while (itr.hasNext()) {
            Pod p = itr.next();
            movePod(p,graph);
            p.setFighting(false);
        }
    }

    /**
     * Move the pod to its next location according to its path sequence
     * The pods won't move if its path is not set
     * The pods won't move if the next movement it's no valid according to the gamerule
     * @param pod to move
     * @param graph
     */
    public void movePod(Pod pod, Graph graph){
        if(pod.hasPath()){
            Node dest = graph.getNode(pod.getPathNodeId());
            if(command.isValidCommand(pod.getQuantity(), pod.getNodeOn(), dest, MY_ID)){
                command.addCommand(pod.getQuantity(), pod.getNodeOn().getId(), dest.getId());
                pod.setNodeOn(dest);
                pod.removePathNextElement();
            }
        }
    }

    /**
     * Send the command to the game and reset it
     */
    public void sendCommand() {
        System.out.println(command.getCommand());
        command.reset();
    }

    /**
     * Update the PodsManager status according to the game events
     * @param map
     */
    public void update(Graph map){
        checkMerge(map);
        checkBattle(map);
    }

    public List<Pod> getPods() { return pods; }

    public List<Pod> getPodsWithoutTarget() {
        ArrayList<Pod> podsWithoutTarget = new ArrayList<>();
        for (Pod p : pods) {
            if(!p.hasPath()) {
                podsWithoutTarget.add(p);
            }
        }
        return podsWithoutTarget;
    }

    public int getFirstRushPodID() {
        int ret = -1;
        for (Pod p : pods) {
            if(p.getTargetId() == MAP.getEnemyQG().getId()) {
                ret = p.getID();
                break;
            }
        }
        return ret;
    }

    /**
     * DEBUG : Display all pods caracteristics from the pods manage by the PodsManager
     */
    public void debug() {
        for (Pod p : pods) {
            System.err.println(p.toString());
        }
    }


}
