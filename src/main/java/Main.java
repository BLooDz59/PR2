package main.java;

import java.util.*;

//Change Main by Player in Codingame IDE

public class Main {

    public static void main(String args[]) {

        Random r = new Random(); //Just to test random movement

        Scanner in = new Scanner(System.in);
        int playerCount = in.nextInt(); // the amount of players (always 2)
        int myId = in.nextInt(); // my player ID (0 or 1)
        int zoneCount = in.nextInt(); // the amount of zones on the map
        Graph graph = new Graph(zoneCount);
        int linkCount = in.nextInt(); // the amount of links between all zones
        for (int i = 0; i < zoneCount; i++) {
            int zoneId = in.nextInt(); // this zone's ID (between 0 and zoneCount-1)
            int platinumSource = in.nextInt(); // Because of the fog, will always be 0
            graph.addNode(new Node(zoneId, platinumSource));
        }
        for (int i = 0; i < linkCount; i++) {
            int zone1 = in.nextInt();
            int zone2 = in.nextInt();
            graph.addLinkBetweenNodesFromId(zone1, zone2);
        }

        // game loop
        while (true) {

            Timer timer = new Timer(); // Comment this line if you want to disable the timer

            int myPlatinum = in.nextInt(); // your available Platinum
            for (int i = 0; i < zoneCount; i++) {
                int zId = in.nextInt(); // this zone's ID
                Node currentNode = graph.getNode(zId);
                int ownerId = in.nextInt(); // the player who owns this zone (-1 otherwise)
                currentNode.setOwner(ownerId);
                int podsP0 = in.nextInt(); // player 0's PODs on this zone
                int podsP1 = in.nextInt(); // player 1's PODs on this zone
                if (myId == 0) {
                    currentNode.setPodsNumber(podsP0);
                    currentNode.setEnemyPodsNumber(podsP1);
                } else {
                    currentNode.setPodsNumber(podsP1);
                    currentNode.setEnemyPodsNumber(podsP0);
                }
                int visible = in.nextInt(); // 1 if one of your units can see this tile, else 0
                currentNode.setVisibility(visible);
                int platinum = in.nextInt(); // the amount of Platinum this zone can provide (0 if hidden by fog)
                currentNode.setPlatinumProduction(platinum);
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            //Just for test, move randomly the first pods then crash when there is other pods I think
            Iterator<Node> itr = graph.getNodesWithPods().iterator();
            while (itr.hasNext()){
                Node node = itr.next();
                List<Node> neighboursNode = node.getLinkedNodes();
                System.out.println(String.format("%d %d %d", node.getPodsNumber(), node.getId() , neighboursNode.get(r.nextInt(neighboursNode.size())).getId()));
            }

            timer.displayDeltaTime();

            // first line for movement commands, second line no longer used (see the protocol in the statement for details)
            System.out.println("WAIT");
        }
    }
}
