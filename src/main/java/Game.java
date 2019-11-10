package main.java;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    Scanner IN;
    int PLAYER_COUNT;
    int MY_ID;
    int ZONE_COUNT;
    int LINK_COUNT;

    boolean DEBUG = false;

    Graph graph;
    Random r;

    public Game(){
        IN = new Scanner(System.in);
        r = new Random(); //Just to test random movement, delete it in the future
    }

    public void setup(){
        PLAYER_COUNT = IN.nextInt(); // the amount of players (always 2)
        MY_ID = IN.nextInt(); // my player ID (0 or 1)
        ZONE_COUNT = IN.nextInt(); // the amount of zones on the map
        graph = new Graph(ZONE_COUNT);
        LINK_COUNT = IN.nextInt(); // the amount of links between all zones
        for (int i = 0; i < ZONE_COUNT; i++) {
            int zoneId = IN.nextInt(); // this zone's ID (between 0 and zoneCount-1)
            int platiniumSource = IN.nextInt(); // Because of the fog, will always be 0
            graph.addNode(new Node(zoneId, platiniumSource));
        }
        for (int i = 0; i < LINK_COUNT; i++) {
            int zone1 = IN.nextInt();
            int zone2 = IN.nextInt();
            graph.addLinkBetweenNodesFromId(zone1, zone2);
        }
    }

    public void run(){
        while (true){
            Timer timer = new Timer(); // Comment this line if you want to disable the timer

            Command command = new Command();

            int myPlatinum = IN.nextInt(); // your available Platinum
            for (int i = 0; i < ZONE_COUNT; i++) {
                int zId = IN.nextInt(); // this zone's ID
                Node currentNode = graph.getNode(zId);
                int ownerId = IN.nextInt(); // the player who owns this zone (-1 otherwise)
                currentNode.setOwner(ownerId);
                int podsP0 = IN.nextInt(); // player 0's PODs on this zone
                int podsP1 = IN.nextInt(); // player 1's PODs on this zone
                if (MY_ID == 0) {
                    currentNode.setPodsNumber(podsP0);
                    currentNode.setEnemyPodsNumber(podsP1);
                } else {
                    currentNode.setPodsNumber(podsP1);
                    currentNode.setEnemyPodsNumber(podsP0);
                }
                if (currentNode.getPodsNumber() > 0) {
                    graph.addNodeWithPods(currentNode);
                }
                else {
                    graph.removeNodeWithPods(currentNode);
                }
                int visible = IN.nextInt(); // 1 if one of your units can see this tile, else 0
                currentNode.setVisibility(visible);
                int platinum = IN.nextInt(); // the amount of Platinum this zone can provide (0 if hidden by fog)
                currentNode.setPlatinumProduction(platinum);
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            //Just for test, move pods randomly
            Iterator<Node> itr = graph.getNodesWithPods().iterator();
            while (itr.hasNext()){
                Node node = itr.next();
                List<Node> neighboursNode = node.getLinkedNodes();
                command.addCommand(node.getPodsNumber(), node.getId(), neighboursNode.get(r.nextInt(neighboursNode.size())).getId());
            }

            if(DEBUG){
                timer.displayDeltaTime();
            }

            // first line for movement commands, second line no longer used (see the protocol in the statement for details)
            System.out.println(command.getCommand());
            System.out.println("WAIT");
        }
    }
}
