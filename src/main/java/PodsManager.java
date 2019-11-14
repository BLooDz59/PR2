package main.java;

import java.util.ArrayList;
import java.util.List;

public class PodsManager {
    List<Pod> pods;
    int creationId;
    Command command;

    public PodsManager() {
        pods = new ArrayList<>();
        creationId = 0;
        command = new Command();
    }

    public void createPod(Node coord, int quantity) {
        if(quantity != 0){
            pods.add(new Pod(coord, creationId, quantity));
            creationId++;
        }
    }

    public void checkMerge(Graph map){
        for (Node node : map.getNodesWithPods()) {
            if(getPodsOnNode(node).size() > 1){
                mergePods(getPodsOnNode(node), node);
            }
        }
    }

    /*public void mergePods(Pod pod1, Pod pod2, Node node) {
        createPod(node, pod1.getQuantity() + pod2.getQuantity());
        removePod(pod1);
        removePod(pod2);
    }*/

    public void mergePods(List<Pod> pods, Node node) {
        System.err.println("MergePods");
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

    public void movePod(Pod pod, Node dest) {
        command.addCommand(pod.quantity, pod.getNodeOn().getId(), dest.getId());
        //if(dest.getPodsNumber() > 0) {
        //    mergePods(pod, getPodOnNode(dest), dest);
        //}
        //else {
        pod.setNodeOn(dest);
        //}
    }

    public void movePod(Node src, Node dest) {
        command.addCommand(getPodOnNode(src).quantity, src.getId(), dest.getId());
        getPodOnNode(src).setNodeOn(dest);
    }

    public void sendCommand() {
        System.out.println(command.getCommand());
        command.reset();
    }

    //DEBUG
    public void debug() {
        for (Pod p : pods) {
            System.err.println(p.toString());
        }
    }


}
