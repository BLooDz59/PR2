package main.java;

import java.util.*;

public class Game {

    private Scanner IN;
    private int MY_ID;
    private int ENEMY_ID;
    private int ZONE_COUNT;
    private final PodsManager PODS_MANAGER;
    private final StrategyManager STRATEGY_MANAGER;

    private Graph map;
    private boolean itsFirstTurn;

    private boolean DEBUG = false; //Change value for Debug TO DELETE
    private Random r; //TO DELETE
    private Timer timer;


    public Game(){
        IN = new Scanner(System.in); //Scanner to read input
        r = new Random(); //Just to test random movement, TO DELETE
        itsFirstTurn = true; // Status True if it's the first turn of the game
        PODS_MANAGER = PodsManager.getInstance();
        STRATEGY_MANAGER = StrategyManager.getInstance();
    }

    public void setup(){
        int PLAYER_COUNT = IN.nextInt(); // the amount of players (always 2)
        MY_ID = IN.nextInt(); // my player ID (0 or 1)
        ENEMY_ID = MY_ID ^ 1;
        PODS_MANAGER.setPlayerID(MY_ID); // Create the PodsManager needed to move all the pods
        ZONE_COUNT = IN.nextInt(); // the amount of zones on the map
        map = new Graph(ZONE_COUNT); //Create the map
        int LINK_COUNT = IN.nextInt(); // the amount of links between all zones
        for (int i = 0; i < ZONE_COUNT; i++) {
            map.addNode(new Node(IN.nextInt(), IN.nextInt())); //Add a new Node from the previous data to the map
        }
        for (int i = 0; i < LINK_COUNT; i++) {
            map.addLinkBetweenNodesFromId(IN.nextInt(), IN.nextInt()); //Update links between nodes in the map
        }
        STRATEGY_MANAGER.setGraph(map);
        STRATEGY_MANAGER.setStrategy(StrategyManager.Strategy.RUSH_QG);
    }

    public void run() {
        while (true) {
            if(DEBUG) { timer = new Timer(); }
            turnUpdate();
            runStrategyManager();
            runPodsManager();
            if(DEBUG) { timer.displayDeltaTime(); }
            if(itsFirstTurn) { itsFirstTurn = false; }
            sendCommand();
        }
    }

    private void turnUpdate() {
        int myPlatinum = IN.nextInt(); // your available Platinum
        for (int i = 0; i < ZONE_COUNT; i++) {
            Node currentNode = map.getNode(IN.nextInt());
            currentNode.setOwner(IN.nextInt());
            map.updatePods(MY_ID, currentNode, IN.nextInt(), IN.nextInt());
            if (itsFirstTurn) {
                if (currentNode.getPodsNumber() > 0) { map.setQG(currentNode); }
                if (currentNode.getOwnerID() == ENEMY_ID) { map.setEnemyQG(currentNode); }
            }
            if(currentNode.equals(map.getQG())){
                PODS_MANAGER.createPod(currentNode, currentNode.getPodsNumber() - PODS_MANAGER.getPodQuantityOnQG(currentNode));
            }
            currentNode.setVisibility(IN.nextInt());
            currentNode.setPlatinumProduction(IN.nextInt());
        }
    }

    private void runPodsManager() {
        PODS_MANAGER.update(map);
        if(DEBUG){
            PODS_MANAGER.debug();
        }
        PODS_MANAGER.movePods(map);
    }

    private void runStrategyManager() {
        STRATEGY_MANAGER.run();
    }

    private void sendCommand() {
        //First line for movement commands, second line no longer used (see the protocol in the statement for details)
        PODS_MANAGER.sendCommand();
        System.out.println("WAIT");
    }
}
