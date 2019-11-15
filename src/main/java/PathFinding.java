package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class PathFinding {
    public static ArrayList<Integer> BFS(Node goal, Node start){
        ArrayList<Node> exploratedNodes = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        start.setBFSdiscovered(true);
        queue.add(start);
        while (queue.size() != 0){
            Node node = queue.remove();
            if(node.equals(goal)){
                while (node != null) {
                    path.add(node.getId());
                    node = node.getBFSparent();
                }
            }
            else {
                for(Node n : node.getLinkedNodes()){
                    if(!n.isBFSdiscovered()){
                        exploratedNodes.add(n);
                        n.setBFSdiscovered(true);
                        n.setBFSparent(node);
                        queue.add(n);
                    }
                }
            }
        }
        for(Node n : exploratedNodes){
            n.setBFSdiscovered(false);
        }
        Collections.reverse(path);
        path.remove(0);
        return path;
    }
}
