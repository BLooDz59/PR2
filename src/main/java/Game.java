package main.java;

import java.util.*;

public class Game {

    Scanner IN;
    int PLAYER_COUNT;
    int MY_ID;
    int ENEMY_ID;
    int ZONE_COUNT;
    int LINK_COUNT;

    boolean DEBUG = false; //Change value for Debug TO DELETE

    Graph map;
    Random r;
    PodsManager podsManager;

    boolean firstTurn;
    ArrayList<Integer> QGPath; //TO DEBUG

    public Game(){
        IN = new Scanner(System.in); //Scanner to read input
        r = new Random(); //Just to test random movement, TO DELETE
        firstTurn = true; // Status True if it's the first turn of the game
    }

    public void setup(){
        PLAYER_COUNT = IN.nextInt(); // the amount of players (always 2)
        MY_ID = IN.nextInt(); // my player ID (0 or 1)
        ENEMY_ID = MY_ID ^ 1;
        podsManager = new PodsManager(MY_ID); // Create the PodsManager needed to move all the pods
        ZONE_COUNT = IN.nextInt(); // the amount of zones on the map
        map = new Graph(ZONE_COUNT); //Create the map
        LINK_COUNT = IN.nextInt(); // the amount of links between all zones
        for (int i = 0; i < ZONE_COUNT; i++) {
            int zoneId = IN.nextInt(); // this zone's ID (between 0 and zoneCount-1)
            int platiniumSource = IN.nextInt(); // Because of the fog, will always be 0
            map.addNode(new Node(zoneId, platiniumSource)); //Add a new Node from the previous data to the map
        }
        for (int i = 0; i < LINK_COUNT; i++) {
            int zone1 = IN.nextInt(); //id of the first zone
            int zone2 = IN.nextInt(); //id of the second zone
            map.addLinkBetweenNodesFromId(zone1, zone2); //Update links between nodes in the map
        }
    }

    public void run(){
        while (true){

            //Create a timer for debug TO DELETE
            Timer timer = new Timer(); // Comment this line if you want to disable the timer

            int myPlatinum = IN.nextInt(); // your available Platinum
            for (int i = 0; i < ZONE_COUNT; i++) {
                int zId = IN.nextInt(); // this zone's ID
                Node currentNode = map.getNode(zId);
                int ownerId = IN.nextInt(); // the player who owns this zone (-1 otherwise)
                currentNode.setOwner(ownerId);
                int podsP0 = IN.nextInt(); // player 0's PODs on this zone
                int podsP1 = IN.nextInt(); // player 1's PODs on this zone
                map.updatePods(MY_ID, currentNode, podsP0, podsP1);
                if (currentNode.getPodsNumber() > 0) {
                    if(firstTurn){
                        map.setQG(currentNode);
                    }
                    if(currentNode.equals(map.getQG())){
                        podsManager.createPod(currentNode, currentNode.getPodsNumber() - podsManager.getPodQuantityOnQG(currentNode));
                    }
                }
                int visible = IN.nextInt(); // 1 if one of your units can see this tile, else 0
                currentNode.setVisibility(visible);
                int platinum = IN.nextInt(); // the amount of Platinum this zone can provide (0 if hidden by fog)
                currentNode.setPlatinumProduction(platinum);
                if(firstTurn && currentNode.isVisible() == 1 && currentNode.getOwnerID() == ENEMY_ID){
                    map.setEnemyQG(currentNode);
                }
            }

            //Update podsManager
            podsManager.update(map);

            //DEBUG TO DELETE
            if(DEBUG) {
                podsManager.debug();
            }

            Iterator<Pod> itr = podsManager.pods.iterator();
            while (itr.hasNext()) {
                Pod p = itr.next();
                if(!p.getTarget()){
                    p.setTarget(map.getEnemyQG());
                }
                podsManager.movePod(p,map);
            }

            //DEBUG TO DELETE
            if(DEBUG){
                timer.displayDeltaTime();
            }

            //Update firstTurn status
            if(firstTurn){
                firstTurn = false;
            }

            //Send Command to the game
            //First line for movement commands, second line no longer used (see the protocol in the statement for details)
            podsManager.sendCommand();
            System.out.println("WAIT");
        }
    }
}
