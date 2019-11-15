package main.java;

import java.util.ArrayList;
import java.util.List;

public class PodsManager {
    List<Pod> pods;
    int creationId;
    Command command;
    int MY_ID;

    public PodsManager(int playerID) {
        pods = new ArrayList<>();
        creationId = 0;
        command = new Command();
        MY_ID = playerID;
    }

    public void createPod(Node coord, int quantity) {
        if(quantity != 0){
            pods.add(new Pod(coord, creationId, quantity));
            creationId++;
        }
    }

    public void checkMerge(Graph map) {
        for (Node node : map.getNodesWithPods()) {
            if(getPodsOnNode(node).size() > 1){
                mergePods(getPodsOnNode(node), node);
            }
        }
    }

    //Need debug because it's fail when a fight occur and we want to move to an enemy node, so the pod value is null
    public void checkBattle(Graph map) {
        for (Node node : map.getNodesWithPods()) {
            Pod pod = getPodOnNode(node);
            int mapPodsNumberInfo = node.getPodsNumber();
            if(pod.getQuantity() != mapPodsNumberInfo){
                pod.setQuantity(mapPodsNumberInfo);
            }
        }
    }

    public void mergePods(List<Pod> pods, Node node) {
        int quantity = 0;
        for (Pod p : pods) {
            quantity += p.getQuantity();
            removePod(p);
        }
        createPod(node, quantity);
    }

    public int getPodQuantityOnQG(Node QG){
        int ret = 0;
        for (Pod p : pods) {
            if(p.getNodeOn().equals(QG)){
                ret += p.getQuantity();
            }
        }
        return ret;
    }

    public void removePod(Pod pod) {
        pods.remove(pod);
    }

    public Pod getPodOnNode(Node node) {
        Pod ret = null;
        for (Pod p : pods) {
            if(p.getNodeOn().equals(node)){
                ret = p;
            }
        }
        return ret;
    }

    public List<Pod> getPodsOnNode(Node node){
        ArrayList<Pod> ret = new ArrayList<>();
        for (Pod p : pods) {
            if(p.getNodeOn().equals(node)){
                ret.add(p);
            }
        }
        return ret;
    }

    public void movePod(Pod pod, Graph g){
        Node dest = g.getNode(pod.getPathNodeId());
        movePod(pod, dest);
    }

    public void movePod(Pod pod, Node dest) {
        command.addCommand(pod.quantity, pod.getNodeOn().getId(), dest.getId());
        pod.setNodeOn(dest);
    }

    public void sendCommand() {
        System.out.println(command.getCommand());
        command.reset();
    }

    public void update(Graph map){
        checkMerge(map);
        checkBattle(map);
    }

    //DEBUG
    public void debug() {
        for (Pod p : pods) {
            System.err.println(p.toString());
        }
    }


}
