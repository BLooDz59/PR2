package main.java;

import java.util.*;

public class Game {

    private Scanner IN;
    private int MY_ID;
    private int ENEMY_ID;
    private int ZONE_COUNT;
    private final PodsManager PODS_MANAGER;
    private final StrategyManager STRATEGY_MANAGER;
    private final Strategy strategy = Strategy.TEST; //Change the strategy here

    private Graph map;
    private boolean itsFirstTurn;

    private boolean DEBUG = false; //Change value for Debug TO DELETE
    private Timer timer;


    public Game(){
        IN = new Scanner(System.in); //Scanner to read input
        itsFirstTurn = true; // Status True if it's the first turn of the game
        PODS_MANAGER = PodsManager.getInstance();
        STRATEGY_MANAGER = StrategyManager.getInstance();
    }

    /**
     * Setup the game
     */
    public void setup(){
        int PLAYER_COUNT = IN.nextInt(); // the amount of players (always 2)
        MY_ID = IN.nextInt(); // my player ID (0 or 1)
        ENEMY_ID = MY_ID ^ 1;
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
        STRATEGY_MANAGER.setStrategy(strategy);
        STRATEGY_MANAGER.setPlayerID(MY_ID);
        PODS_MANAGER.setGraph(map);
        PODS_MANAGER.setPlayerID(MY_ID); // Create the PodsManager needed to move all the pods
    }

    /**
     * Run the game
     */
    public void run() {
        while (true) {
            if(DEBUG) { timer = new Timer(); }
            turnUpdate();
            runManagers();
            if(DEBUG) { timer.displayDeltaTime(); }
            if(itsFirstTurn) { itsFirstTurn = false; }
            sendCommand();
        }
    }

    /**
     * Calls every game loop, gives updated map info
     */
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
                Pod p = PODS_MANAGER.createPod(currentNode, currentNode.getPodsNumber() - PODS_MANAGER.getPodQuantityOnQG(currentNode));
                PODS_MANAGER.addPod(p);
            }
            currentNode.setVisibility(IN.nextInt());
            currentNode.setPlatinumProduction(IN.nextInt());
        }
    }

    /**
     * Calls the differents managers events
     */
    private void runManagers() {
        PODS_MANAGER.update();
        STRATEGY_MANAGER.update();
        PODS_MANAGER.postUpdate();
        STRATEGY_MANAGER.run();
        if(DEBUG){
            PODS_MANAGER.debug();
        }
        PODS_MANAGER.movePods();
    }

    /**
     * Send command to the CodinGame game
     */
    private void sendCommand() {
        //First line for movement commands, second line no longer used (see the protocol in the statement for details)
        PODS_MANAGER.sendCommand();
        System.out.println("WAIT");
    }
}
