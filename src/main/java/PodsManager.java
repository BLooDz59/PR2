package main.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PodsManager {
    private List<Pod> pods;
    private List<Pod> podsBuffer;
    private List<Pod> removedPodsBuffer;
    private Command command;
    private Graph MAP;
    private int MY_ID;
    private int creationID;


    /**
     * Private Constructor
     * PodsManager is use to manage multiple Pods.
     */
    private PodsManager() {
        pods = new ArrayList<>();
        podsBuffer = new ArrayList<>();
        removedPodsBuffer = new ArrayList<>();
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

    //Getters

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
            if(p.getTarget() == MAP.getEnemyQG()) {
                ret = p.getID();
                break;
            }
        }
        return ret;
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

    //Setters

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
     * Check if multiple Pods are on the same node in the map and merge them
     * @param node
     */
    public void checkMerge(Node node) {
        if(getPodsOnNode(node).size() > 1){
            mergePods(getPodsOnNode(node), node);
        }
    }

    /**
     * Check if Pods have fought during the previous turn and update there quantity
     * @param node
     */
    public void checkBattle(Node node) {
        Pod pod = getPodOnNode(node);
        int mapPodsNumberInfo = node.getPodsNumber();
        if(pod.getQuantity() != mapPodsNumberInfo){
            pod.setQuantity(mapPodsNumberInfo);
            pod.setFighting(true);
        }
    }

    /**
     * Merge multiple Pods to one global Pods on specific Node
     * @param pods the list of pods that will be merge
     * @param node the node where the pods are
     */
    public void mergePods(List<Pod> pods, Node node) {
        int quantity = 0;
        for (Pod p : pods) {
            quantity += p.getQuantity();
            removePod(p);
        }
        createPod(node, quantity);
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
     * Return the biggest Pod
     * @return Pod with biggest quantity
     */
    public Pod getMaxQuantityPod() {
        int quantity = 0;
        Pod pod = null;
        for(Pod p : pods) {
            if(p.getQuantity() > quantity) {
                quantity = p.getQuantity();
                pod = p;
            }
        }
        return pod;
    }

    /**
     * Remove pod from the PodsManager
     * @param pod to remove
     */
    public void removePod(Pod pod) {
        pods.remove(pod);
    }

    /**
     * Move all the pods manage by the PodsManager
     */
    public void movePods(){
        Iterator<Pod> itr = pods.iterator();
        while (itr.hasNext()) {
            Pod p = itr.next();
            movePod(p);
            p.setFighting(false);
        }
    }

    /**
     * Move the pod to its next location according to its path sequence
     * The pods won't move if its path is not set
     * The pods won't move if the next movement it's no valid according to the gamerule
     * @param pod to move
     */
    public void movePod(Pod pod){
        if(pod.hasPath()){
            Node dest = MAP.getNode(pod.getPathNodeId());
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
     */
    public void update(){
        for (Node node : MAP.getNodesWithPods()) {
            checkMerge(node);
            checkBattle(node);
        }
    }

    /**
     * Split the specified pod into n pods after postUpdate()
     * @param pod
     * @param n
     */
    public void splitPod(Pod pod, int n) {
        if (n != 0) {
            int newQuantity = pod.getQuantity() / n;
            Node coord = pod.getNodeOn();
            if(pod.getQuantity() % n != 0) {
                bufferAddPod(coord, pod.getQuantity() % n);
            }
            for (int i = 0; i < n; i++) {
                bufferAddPod(coord, newQuantity);
            }
            addPodToRemove(pod);
        }
    }

    public void postUpdate() {
        pods.addAll(podsBuffer);
        pods.removeAll(removedPodsBuffer);
        podsBuffer.clear();
        removedPodsBuffer.clear();
    }

    /**
     * Add a pod the add buffer
     * @param coord
     * @param quantity
     */
    public void bufferAddPod(Node coord, int quantity) {
        if(quantity != 0){
            podsBuffer.add(new Pod(coord, quantity, creationID));
            creationID++;
        }
    }

    /**
     * Add a pod to remove in the remove buffer
     * @param p
     */
    public void addPodToRemove(Pod p) {
        removedPodsBuffer.add(p);
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
